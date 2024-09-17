package caojx.learn.netty.cn.itcast.netty.nio.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 阻塞模式客户端测试
 *
 * @author caojx
 * @since 2024/8/5 22:34
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        System.out.println("waiting...");
//        sc.write(Charset.defaultCharset().encode("hello\nworld\n"));
        sc.write(Charset.defaultCharset().encode("0123\n456789abcdef\n"));
        sc.write(Charset.defaultCharset().encode("0123456789abcdef3333\n"));
        sc.close();
    }
}
