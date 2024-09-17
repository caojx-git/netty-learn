package caojx.learn.netty.cn.itcast.netty.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import static caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil.debugAll;


@Slf4j
public class MultiThreadServer {
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
//        for (int i = 0; i < 10; i++) {
        Worker worker = new Worker("worker-0");
//        }

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

            // 向队列中添加任务，用于注册读事件，但是这个任务并没有立刻执行
            queue.add(() -> {
                try {
                    sc.register(this.selector, SelectionKey.OP_READ, null); // boss
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            selector.wakeup(); // 唤醒 select 方法 boss
        }

        @Override
        public void run() {
            while (true) {
                try {
                    this.selector.select(); // worker-0 阻塞

                    // 确保读事件正确注册
                    Runnable task = this.queue.poll();
                    if (task != null) {
                        task.run(); // 执行了  sc.register(this.selector, SelectionKey.OP_READ, null)
                    }

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
