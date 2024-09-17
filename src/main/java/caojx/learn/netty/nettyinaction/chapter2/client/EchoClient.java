package caojx.learn.netty.nettyinaction.chapter2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/7/19 12:49
 */
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // 创建 Bootstrap
            b.group(group)  // 指定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现
                    .channel(NioSocketChannel.class) // 查用于 NIO 传输的 Channel 类型
                    .remoteAddress(new InetSocketAddress(host, port)) // 设置服务器的InetSocketAddress
                    .handler(new ChannelInitializer<SocketChannel>() { // 在创建 Channel 时， 向 ChannelPipeline中添加一个 Echo-ClientHandler 实例
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture f = b.connect().sync(); // 连接到远程节点，阻塞等待直到连接完成
            f.channel().closeFuture().sync(); // 阻塞，直到Channel 关闭
        } finally {
            group.shutdownGracefully().sync(); // 关闭线程池并且释放所有的资源
        }
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            System.err.println("Usage: EchoClient" + EchoClient.class.getSimpleName() + " <host> <port>");
//            return;
//        }
//        String host = args[0];
//        int port = Integer.parseInt(args[1]);

        String host = "127.0.0.1";
        int port = 9999;
        new EchoClient(host, port).start();
    }
}
