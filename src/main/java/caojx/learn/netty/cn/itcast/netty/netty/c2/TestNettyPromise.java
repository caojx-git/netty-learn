package caojx.learn.netty.cn.itcast.netty.netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/13 22:08
 */
@Slf4j
public class TestNettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1.准备EventLoop
        EventLoop eventLoop = new NioEventLoopGroup().next();

        // 2.可以主动创建promise
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            //3. 任意一个线程执行计算，计算完毕后向 promise 填充结果
            System.out.println("开始计算...");
            try {
                Thread.sleep(1000);
                promise.setSuccess(80);
                int i = 1 / 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
        }).start();

        // 4.接收结果
        log.debug("等待结果");
        log.debug("结果是 {}", promise.get());
    }
}
