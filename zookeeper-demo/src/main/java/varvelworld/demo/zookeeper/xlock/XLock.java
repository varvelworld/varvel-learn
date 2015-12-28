package varvelworld.demo.zookeeper.xlock;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分布式互斥锁
 * Created by luzhonghao on 2015/12/22.
 */
public class XLock implements Watcher {
    public static class LockNode {
        private String path;
        private int lockNum;

        public LockNode(String path, int lockNum) {
            this.path = path;
            this.lockNum = lockNum;
        }
    }

    private ZooKeeper zk;
    private String lockNodePath;
    private LockNode currentLockNode;
    private LockNode currentWatcherLockNode;
    public XLock(String connectString, String lockNodePath) throws IOException {
        this.lockNodePath = lockNodePath;
        this.zk = new ZooKeeper(connectString, 10000, this);
    }
    final static Pattern LOCK_PATH_PATTERN = Pattern.compile(".*/lock-([0-9]+)");
    final static Pattern LOCK_PATTERN = Pattern.compile("lock-([0-9]+)");
    public void lock() throws KeeperException, InterruptedException {
        String currentLockPath = zk.create(lockNodePath + "/lock-", "lock".getBytes()
                , ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        {
            Matcher matcher = LOCK_PATH_PATTERN.matcher(currentLockPath);
            currentLockNode = matcher.matches()
                    ? new LockNode(currentLockPath, Integer.parseInt(matcher.group(1)))
                    : null;
        }
        while(true) {
            List<String> locks = zk.getChildren(lockNodePath, false);
            Optional<LockNode> lowerLockNode = locks.stream().map(s -> {
                Matcher matcher = LOCK_PATTERN.matcher(s);
                return matcher.matches()
                        ? new LockNode(lockNodePath + "/" + s, Integer.parseInt(matcher.group(1)))
                        : null;
            }).filter(Objects::nonNull).filter(n -> n.lockNum < currentLockNode.lockNum)
                    .max((x, y) -> Integer.compare(x.lockNum, y.lockNum));
            if (!lowerLockNode.isPresent()) {
                return;
            }
            synchronized (this) {
                currentWatcherLockNode = lowerLockNode.get();
                if(zk.exists(currentWatcherLockNode.path, true) != null) {
                    wait();
                }
            }
        }
    }

    public void unlock() throws KeeperException, InterruptedException {
        synchronized (this) {
            if(currentLockNode != null) {
                zk.delete(currentLockNode.path, -1);
                currentLockNode = null;
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected.equals(event.getState())) {
            if(Event.EventType.NodeDeleted.equals(event.getType())) {
                if(currentWatcherLockNode != null
                        && (currentWatcherLockNode.path).equals(event.getPath())) {
                    synchronized (this) {
                        currentWatcherLockNode = null;
                        notifyAll();
                    }
                }
            }
        }
    }
}
