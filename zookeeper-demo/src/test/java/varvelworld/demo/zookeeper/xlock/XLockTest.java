package varvelworld.demo.zookeeper.xlock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by luzhonghao on 2015/12/28.
 */
@RunWith(JUnit4.class)
public class XLockTest {
    static AtomicInteger count = new AtomicInteger(0);

    Logger logger = LoggerFactory.getLogger(XLockTest.class);

    @Test
    public void test() throws IOException, InterruptedException {
        final int COUNT_OF_THREADS = 10; // 总线程数
        final int COUNT_OF_PER_THREADS_TIMES = 10; // 每线程计数量
        Thread[] threads = new Thread[COUNT_OF_THREADS];
        for(int i = 0; i < COUNT_OF_THREADS; ++i) {
            final XLock xLock = new XLock("127.0.0.1:2181", "/mylock");
            threads[i] = new Thread(() -> {
                try {
                    for(int j = 0 ; j < COUNT_OF_PER_THREADS_TIMES; ++j) {
                        xLock.lock();
                        int tmp = count.get();
                        Thread.sleep(200);
                        count.set(tmp + 1);
                        xLock.unlock();
                        logger.info("count is {}", count);
                    }
                } catch (Exception e) {
                    logger.error("process error", e);
                }
            }, "process" + i);
        }
        for(int i = 0; i < COUNT_OF_THREADS; ++i) {
            threads[i].start();
        }
        for(int i = 0; i < COUNT_OF_THREADS; ++i) {
            threads[i].join();
        }
        Assert.assertEquals(COUNT_OF_THREADS * COUNT_OF_PER_THREADS_TIMES, count.get());
    }
}
