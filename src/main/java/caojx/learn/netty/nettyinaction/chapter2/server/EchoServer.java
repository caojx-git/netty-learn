package caojx.learn.netty.nettyinaction.chapter2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 代码清单 2-2 EchoServer 类
 *
 * @author caojx
 * @since 2024/7/15 13:05
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        int port = 9999;
//        if (args.length <= 1) {
//            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
//        }else{
//            port = Integer.parseInt(args[0]); // 设置端口值（如果端口参数的格式不正确，则抛出一个NumberFormatException）
//        }
        new EchoServer(port).start(); // 调用服务器的 start()方法
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup(); // 1.创建EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap();  // 2.创建 Server-Bootstrap
            b.group(group)
                    .channel(NioServerSocketChannel.class)  // 3.指定所使用的 NIO 传输 Channel
                    .localAddress(new InetSocketAddress(port)) // 4.使用指定的端口设置套接字地址

                    // 你使用了一个特殊的类——ChannelInitializer。这是关键。当一个新的连接被接受时，一个新的子 Channel 将会被创建，
                    // 而 ChannelInitializer 将会把一个你的EchoServerHandler的实例添加到该 Channel的 ChannelPipeline中。
                    // 正如我们之前所解释的，这个 ChannelHandler将会收到有关入站消息的通知。
                    .childHandler(new ChannelInitializer<SocketChannel>() {  // 5.添加一个EchoServer-Handler 到子 Channel 的 ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);   // EchoServerHandler 被标注次@Shareable，所以我们可以总是使用同样的实例
                        }
                    });

            ChannelFuture f = b.bind().sync();  // 6.异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
            f.channel().closeFuture().sync();  // 7.获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
        } finally {
            group.shutdownGracefully().sync(); // 8.关闭 EventLoopGroup，释放所有的资源
        }

    }
}
