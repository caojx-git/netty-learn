package caojx.learn.netty.cn.itcast.netty.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil.debugAll;


@Slf4j
public class MultiThreadServer_multiWorker {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        // boss线程专门负责accept事件
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));


        // 1.创建固定数量的worker 并初始化
        Worker[] workers = new Worker[10];
//        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }

        AtomicInteger index = new AtomicInteger();
        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("connected...{}", sc.getRemoteAddress());
                    // 2. 关联 selector
                    log.debug("before register...{}", sc.getRemoteAddress());
                    // boss关联worker selector
                    // round robin
                    Worker worker = workers[index.getAndIncrement() % workers.length];
                    worker.register(sc); // boss调用初始化 selector，启动worker-0

                    log.debug("after register...{}", sc.getRemoteAddress());
                }
            }
        }
    }


    /**
     * 专门监测读写事件
     */
    static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean start = false; // 还未初始化
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();  // 队列用于两个线程之间通信

        public Worker(String name) {
            this.name = name;
        }

        // 初始化线程 和 selector
        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                this.thread = new Thread(this, name);
                this.selector = Selector.open();
                this.thread.start();
                this.start = true;
            }
            selector.wakeup(); // 唤醒 select 方法 boss
            sc.register(this.selector, SelectionKey.OP_READ, null); // boss

        }

        @Override
        public void run() {
            while (true) {
                try {
                    this.selector.select(); // worker-0 阻塞
                    Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel sc = (SocketChannel) key.channel();
                            log.debug("read...{}", sc.getRemoteAddress());
                            sc.read(buffer);
                            buffer.flip();
                            debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    }
}
