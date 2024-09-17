package caojx.learn.netty.cn.itcast.netty.netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/13 21:54
 */
@Slf4j
public class TestNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();

        // 得到netty的Future
        Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                Thread.sleep(100);
                return 70;
            }
        });
//
//        // 1.同步阻塞获取结果
//        log.debug("等待结果");
//        log.debug("结果是 {}", future.get()); // future.get() 阻塞等待结果

        // 2.异步方式获取结果
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                log.debug("接收结果:{}", future.getNow());
            }
        });
    }
}
