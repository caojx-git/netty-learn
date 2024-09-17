package caojx.learn.netty.nettyinaction.chapter2.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 2-3 客户端的 ChannelHandler
 *
 * @author caojx
 * @since 2024/7/18 12:58
 */
@ChannelHandler.Sharable // 标记该类的实例可以被多个 Channel 共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 在到服务器的连接已经建立之后将被调用；
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知 Channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    /**
     * 当从服务器接收到一条消息时被调用；
     *
     * 每当接收数据时，都会调用这个方法。需要注
     * 意的是，由服务器发送的消息可能会被分块接收。也就是说，如果服务器发送了 5 字节，那么不
     * 能保证这 5 字节会被一次性接收。即使是对于这么少量的数据，channelRead0()方法也可能
     * 会被调用两次，第一次使用一个持有 3 字节的 ByteBuf（Netty 的字节容器），第二次使用一个
     * 持有 2 字节的 ByteBuf。作为一个面向流的协议，TCP 保证了字节数组将会按照服务器发送它
     * 们的顺序被接收。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8)); // 记录已接收消息的转储
    }

    /**
     * 在处理过程中引发异常时被调用。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();  // 在发生异常时，记录错误并关闭Channel
    }
}
