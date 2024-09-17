package caojx.learn.netty.nettyinaction.chapter4.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 代码清单 4-3 使用 Netty 的阻塞网络处理
 *
 * @author caojx
 * @since 2024/7/22 15:19
 */
public class NettyOioServer {

    public void serve(int port) throws Exception {

        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));

        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // 创建 Server-Bootstrap
            b.group(group)
                    .channel(OioServerSocketChannel.class) // 使用 OioEventLoopGroup以允许阻塞模式（旧的I/O)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 指定 Channel-Initializer， 对于每个已接受的连接都调用它
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() { // 添加一个 Channel-InboundHandler-Adapter 以拦截和处理事件
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ctx.writeAndFlush(buf.duplicate())
                                            .addListener(ChannelFutureListener.CLOSE); // 将消息写到客户端，并添加 ChannelFutureListener， 以便消息一被写完就关闭连接
                                }
                            });
                        }
                    });

            ChannelFuture f = b.bind().sync(); // 邦定服务器从接受连接
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync(); // 释放所有的资源
        }
    }
}
