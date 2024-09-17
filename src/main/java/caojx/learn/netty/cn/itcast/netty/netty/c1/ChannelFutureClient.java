package caojx.learn.netty.cn.itcast.netty.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/13 20:55
 */
@Slf4j
public class ChannelFutureClient {
    public static void main(String[] args) throws InterruptedException {

        // 2.带有 Future，Promise 的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    // 建立连接后被调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 1.连接到服务器
                // 异步非阻塞，main 发起了调用，真正执行 connect是nio线程
                .connect(new InetSocketAddress("localhost", 8080)); // 发起连接，可能需要1s后才会建立好连接，即这里不会阻塞

//        // 2.1 使用 sync 方法同步处理结果
//        channelFuture.sync(); // 阻塞住，等连接建立好了才会向后运行
//        Channel channel = channelFuture.channel();
//        log.debug("{}", channel);
//        channel.writeAndFlush("hello, world");

        // 2.2. 使用addListener（回调对象） 方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            // 在 nio 线程述接建立好之后，会调用 operationComplete
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("{}", channel);
                channel.writeAndFlush("hello, world");
            }
        });
    }
}
