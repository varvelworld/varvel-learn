package varvelworld.demo.zookeeper.consistent_hashing;

import java.util.TreeSet;

/**
 * 真实节点
 * Created by Hao on 2016/2/4.
 */
public class RealNode {
    private TreeSet<VirtualNode> virtualNodes; // 虚拟节点映射

    public RealNode(TreeSet<VirtualNode> virtualNodes) {
        this.virtualNodes = virtualNodes;
    }

    public TreeSet<VirtualNode> getVirtualNodes() {
        return virtualNodes;
    }
}
