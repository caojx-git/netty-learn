package caojx.learn.netty.cn.itcast.netty.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author caojx
 * @since 2024/8/12 20:50
 */
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {
        // 1.启动类
        Channel channel = new Bootstrap()
                // 2. 添加EventLoop
                .group(new NioEventLoopGroup())
                // 3.选择客户端channel实现
                .channel(NioSocketChannel.class)
                // 4.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    // 建立连接后被调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 9.
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 5.连接到服务器
                .connect(new InetSocketAddress("localhost", 8080))
                .sync() // 6
                .channel();// 7
        System.out.println(channel);
        // 8.向服务器发送数据
        channel.writeAndFlush("hello, world");
    }
}
