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
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 代码清单 4-4 使用 Netty 的异步网络处理
 *
 * 代码清单 4-4 和代码清单 4-3 几乎一模一样，除了高亮显示的那两行。这就是从阻塞（OIO）
 * 传输切换到非阻塞（NIO）传输需要做的所有变更。
 * 因为 Netty 为每种传输的实现都暴露了相同的 API，所以无论选用哪一种传输的实现，你的
 * 代码都仍然几乎不受影响。在所有的情况下，传输的实现都依赖于 interface Channel、
 * ChannelPipeline和 ChannelHandler。
 *
 * Netty 为它所有的传输实现提供了一个通用 API，这使得这种转换比你直接使用 JDK
 * 所能够达到的简单得多。 所产生的代码不会被实现的细节所污染，而你也不需要在你的整个代码
 * 库上进行广泛的重构。简而言之，你可以将时间花在其他更有成效的事情上。
 *
 * @author caojx
 * @since 2024/7/22 15:19
 */
public class NettyNioServer {

    public void serve(int port) throws Exception {

        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));

        EventLoopGroup group = new NioEventLoopGroup(); // 为非阻塞模式使用NioEventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap(); // 创建 Server-Bootstrap
            b.group(group)
                    .channel(NioServerSocketChannel.class) // NioServerSocketChannel
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
