package varvelworld.demo.zookeeper.master_slave.common;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * 平民类
 * Created by luzhonghao on 2015/12/18.
 */
public class Citizen implements Watcher {
    private ZooKeeper zk;
    private String leaderNodePath;
    private Listener<String> newLeaderListener;

    /**
     * @param connectString         zk连接字符串
     * @param leaderNodePath        总统zk节点路径（仅支持父路径已创建）
     * @param newLeaderListener     新总统上任监听器
     * @throws IOException
     */
    public Citizen(String connectString, String leaderNodePath, Listener<String> newLeaderListener) throws IOException {
        this.leaderNodePath = leaderNodePath;
        this.newLeaderListener = newLeaderListener;
        this.zk = new ZooKeeper(connectString, 1000, this);
    }

    public String watch() throws KeeperException, InterruptedException {
       Stat stat = zk.exists(leaderNodePath, true);
       if(stat == null) {
           return null;
       }
       else {
           return new String(zk.getData(leaderNodePath, true, new Stat()));
       }
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            if (Event.KeeperState.SyncConnected.equals(event.getState())) {
                if (Event.EventType.NodeCreated.equals(event.getType())) {
                    newLeaderListener.on(new String(zk.getData(leaderNodePath, true, new Stat())));
                } else if (Event.EventType.NodeDeleted.equals(event.getType())) {
                    Stat stat = zk.exists(leaderNodePath, true);
                    if (stat != null) {
                        newLeaderListener.on(new String(zk.getData(leaderNodePath, true, new Stat())));
                    }
                }
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
