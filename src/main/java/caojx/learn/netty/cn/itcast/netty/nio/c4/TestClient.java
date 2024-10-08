package caojx.learn.netty.cn.itcast.netty.nio.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/11 13:02
 */
public class TestClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));
        sc.write(Charset.defaultCharset().encode("1234567890abcdef"));
        System.in.read();
    }
}
