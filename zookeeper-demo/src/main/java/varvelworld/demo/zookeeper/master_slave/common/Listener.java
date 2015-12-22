package varvelworld.demo.zookeeper.master_slave.common;

/**
 * Created by luzhonghao on 2015/12/18.
 */
public interface Listener<P> {
    void on(P param);
}
