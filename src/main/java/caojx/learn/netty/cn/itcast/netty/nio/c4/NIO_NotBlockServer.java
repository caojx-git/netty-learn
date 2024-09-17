package caojx.learn.netty.cn.itcast.netty.nio.c4;

import caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * NIO 非阻塞模式服务端测试
 *
 * @author caojx
 * @since 2024/8/5 22:28
 */
@Slf4j
public class NIO_NotBlockServer {
    public static void main(String[] args) throws IOException {
        // 使用 nio 来理非阻塞解阻塞模式，单线程处理
        // 0.ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); // 非阻塞模式

        // 2.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // 3.连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 4.accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            SocketChannel sc = ssc.accept(); // 非阻塞，线程还会继续运行，如果没有连接建立，但是sc会返回null
            if (sc != null) {
                log.debug("connected ...{}", sc);
                sc.configureBlocking(false); // 客户端也设置为非阻塞模式
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                // 4.接收客户端发送的数据
                int read = channel.read(buffer);// 非阻塞，线程任然会继续运行，如果没有数据 read返回0
                if (read > 0) {
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    buffer.clear();
                    log.debug("after read ...{}", sc);
                }
            }
        }

    }
}
