package caojx.learn.netty.cn.itcast.netty.netty.c1;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/12 22:06
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        // 细分2：创建一个独立的EventLoopGroup处理那种耗时较长的事件
        EventLoopGroup group = new DefaultEventLoopGroup();

        new ServerBootstrap()
//                .group(new NioEventLoopGroup())
                // 如下两个参数代表：boss 和 worker
                // 细分1：boss 只负责 ServerSocketChannel 上 accept 事件， worker 只负责 socketChannel 上的读写，如下假设只有两个worker线程
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 使用worker线程来处理
                        ch.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        log.debug(buf.toString(Charset.defaultCharset()));
                                        ctx.fireChannelRead(msg); // 将消息床底给下一个handler(handler2)
                                    }
                                })
                                // 假设如下是一个耗时较长的任务，用的就是我们单独指定的 EventLoopGroup group 来处理
                                .addLast(group, "handler2", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        log.debug(buf.toString(Charset.defaultCharset()));
                                    }
                                });


                    }
                }).bind(8080);
    }
}
