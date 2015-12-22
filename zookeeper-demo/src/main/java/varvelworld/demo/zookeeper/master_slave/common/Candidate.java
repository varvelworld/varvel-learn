package varvelworld.demo.zookeeper.master_slave.common;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 候选人
 * Created by luzhonghao on 2015/12/18.
 */
public class Candidate implements Watcher {
    private Listener<Void> upListener;
    private Listener<Void> downListener;
    private ZooKeeper zk;
    private String leaderNodePath;
    private String leaderNodeData;
    private AtomicBoolean isLeader = new AtomicBoolean(false);

    /**
     *
     * @param connectString     zk连接字符串
     * @param leaderNodePath    总统zk节点路径（仅支持父路径已创建）
     * @param leaderNodeData    总统zk节点数据内容
     * @param upListener        上任监听器
     * @param downListener      卸任监听器
     * @throws IOException
     */
    public Candidate(String connectString, String leaderNodePath, String leaderNodeData
            , Listener<Void> upListener, Listener<Void> downListener)
            throws IOException {
        this.leaderNodePath = leaderNodePath;
        this.leaderNodeData = leaderNodeData;
        this.upListener = upListener;
        this.downListener = downListener;
        this.zk = new ZooKeeper(connectString, 10000, this);
    }

    public void election() throws InterruptedException, KeeperException {
        try {
            this.zk.create(leaderNodePath, leaderNodeData.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            isLeader.set(true);
            upListener.on(null); //竞选成功
        } catch (KeeperException e) {
            this.zk.getData(leaderNodePath, true, new Stat()); // 竞选失败
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.Disconnected.equals(event.getState())) {
            if(isLeader.get()) {
                // 总统下台
                isLeader.set(false);
                downListener.on(null);
            }
        }
        else if(Event.KeeperState.SyncConnected.equals(event.getState())){
            if(Event.EventType.NodeDeleted.equals(event.getType())) {
                if(leaderNodePath.equals(event.getPath())) {
                    // 总统缺失
                    try {
                        election(); // 竞选总统
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
