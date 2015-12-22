package varvelworld.demo.zookeeper.xlock;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luzhonghao on 2015/12/22.
 */
public class XLock implements Watcher {
    private ZooKeeper zk;
    private String lockNodePath;
    private Integer currentLockNum;
    private Integer currentWatcherLockNum;
    private Object lock = new Object();
    public XLock(String connectString, String lockNodePath) throws IOException {
        this.lockNodePath = lockNodePath;
        this.zk = new ZooKeeper(connectString, 10000, this);
    }
    final static Pattern LOCK_PATTERN = Pattern.compile("lock-([0-9]+)");
    public void lock() throws KeeperException, InterruptedException {
        String currentLockPath = zk.create(lockNodePath + "/lock-", "lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,  CreateMode.EPHEMERAL_SEQUENTIAL);
        {
            Matcher matcher = LOCK_PATTERN.matcher(currentLockPath);
            currentLockNum = Integer.parseInt(matcher.group(1));
        }
        List<String> locks = zk.getChildren(lockNodePath, false);
        Optional<Integer> lowerNum = locks.stream().map(s -> {
            Matcher matcher = LOCK_PATTERN.matcher(s);
            if(matcher.matches()) {
                return Integer.parseInt(matcher.group(1));
            }
            else {
                return null;
            }
        }).filter(Objects::nonNull).filter(n -> n < currentLockNum).max(Integer::compare);
        if(!lowerNum.isPresent()) {
            return;
        }
        currentWatcherLockNum = lowerNum.get();
        zk.exists(lockNodePath + "/lock-" + currentWatcherLockNum, true);
        lock.wait();
    }

    public void unlock() throws KeeperException, InterruptedException {
        zk.delete(lockNodePath + "/lock-" + currentLockNum, -1);
    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected.equals(event.getState())) {
            if(Event.EventType.NodeDeleted.equals(event.getType())) {
                if((lockNodePath + "/lock-" + currentWatcherLockNum).equals(event.getPath())) {
                    lock.notify();
                }
            }
        }
    }
}
