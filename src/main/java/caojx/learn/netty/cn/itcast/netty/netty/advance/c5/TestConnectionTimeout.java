package caojx.learn.netty.cn.itcast.netty.netty.advance.c5;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestConnectionTimeout {
    public static void main(String[] args) {
        // 1. 客户端通过 .option() 方法配置参数 给 SocketChannel 配置参数

        // 2. 服务器端
//        new ServerBootstrap().option() // 是给 ServerSocketChannel 配置参数
//        new ServerBootstrap().childOption() // 给 SocketChannel 配置参数

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    // 如果客户端在指定(这里300)毫秒内无法连接，会抛出 timeout 异常
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 300)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler());
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080);
            future.sync().channel().closeFuture().sync(); // 断点
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("timeout");
        } finally {
            group.shutdownGracefully();
        }
    }
}
