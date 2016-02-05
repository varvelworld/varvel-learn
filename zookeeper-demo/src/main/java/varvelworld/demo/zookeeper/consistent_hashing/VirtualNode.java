package varvelworld.demo.zookeeper.consistent_hashing;

import org.apache.zookeeper.ZooKeeper;

/**
 * 虚拟节点
 * Created by Hao on 2016/2/4.
 */
public class VirtualNode {
    private long id;        // 当前节点编号
    private long pre;       // 前驱节点编号
    private String path;
    private ZooKeeper zk;

    public VirtualNode(ZooKeeper zk, String path, long id, long pre) {
        this.id = id;
        this.pre = pre;
    }

    public long getId() {
        return id;
    }

    public Long getPre() {
        return pre;
    }

    public void setPre(long pre) {
        this.pre = pre;
    }
}
