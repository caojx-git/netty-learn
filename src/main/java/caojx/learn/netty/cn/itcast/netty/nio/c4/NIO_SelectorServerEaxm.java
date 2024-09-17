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
import java.util.Set;

import static caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil.debugAll;

/**
 * 处理消息边界问题
 *
 * @author caojx
 * @since 2024/8/5 22:28
 */
@Slf4j
public class NIO_SelectorServerEaxm {

    public static void main(String[] args) throws IOException {
        // 1.创建 selector，管理多个channel
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // 2. 建立 selector 和 channel 的联系（注册）
        // SelectionKey 就是将来事件发生后，通过他可以知道事件和那个channel的事件
        // 第二个参数：0 表示不关注任何事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // key 只关注 accept 事例
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key:{}", sscKey);

        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            // 3.select 方法，没有事件发生，线程阻塞，有事件，线程恢复运行
            // select 在时间未处理时，他不会阻塞，事件发生后要么处理，要么取消，不能置之不理
            selector.select();

            // 4.处理事件，selectionKeys 内部包含了所有发生的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator(); // accept, read
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                // 必理key 时，要从 selectedKeys 集合中删除，否则下次处理就会有问题
                iterator.remove();
                log.debug("key: {}", key);

                // 5. 区分事件类型
                if (key.isAcceptable()) { // 如果是accept
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept(); // 相当与处理了事件

                    // 建立 selector 和 SocketChannel 的联系
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);

                    // 将一个 byteBuffer 作为附件关联到 selectionKey 上
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                } else if (key.isReadable()) { // 如果是 read
                    try {
                        SocketChannel channel = (SocketChannel) key.channel(); // 拿到触发事件的channel

                        // 获取 SelectionKey 关联的附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);
                        if (read == -1) { // 如果是正常断开，read 的方法的返回值是：正1
                            key.cancel();
                            channel.close();
                        } else {
                            // 处理消息边界，按照分割符号拆分 \n
                            split(buffer);
                            // 如果缓冲区太小，就进行扩容
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                // 将旧buffer中的内容放入新的buffer中
                                buffer.flip();
                                newBuffer.put(buffer);
                                // 将新buffer作为附件放到key中
                                key.attach(newBuffer);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel(); //  因为客户端异常（强制）断开了，因此需要将 key 取消（从 selector 的 keys 集合中真正删除 key）
                    }
                }
            }
        }
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 找到一条完整消息
            if (source.get(i) == '\n') {
                // 把完整消息存入ByteBuffer
                int length = i + 1 - source.position(); // 计算消息的长度， 换行符号位置+1-起始位置
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从source读取，向target写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        source.compact();
    }

}
