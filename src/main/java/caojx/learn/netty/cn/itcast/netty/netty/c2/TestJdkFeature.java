package caojx.learn.netty.cn.itcast.netty.netty.c2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * jdk feature 测试
 *
 * @author caojx
 * @since 2024/8/13 21:49
 */
@Slf4j
public class TestJdkFeature {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 1.创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 2. 提交任务
        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return 50;
            }
        });

        // 3.主线程通过feature获取结果
        log.debug("等待结果");
        log.debug("结果是 {}", future.get()); // future.get() 阻塞等待结果
    }
}
