package caojx.learn.netty.nettyinaction.chapter2.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 2-1 EchoServerHandler
 *
 * @author caojx
 * @since 2024/7/15 12:57
 */
@ChannelHandler.Sharable // 标示一个 ChannelHandler 可以被多Channel 安全地 共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入的消息都要调用；
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        ctx.write(in); // 将接收到的消息写给发送者，而不冲刷出站消息
    }

    /**
     * 通知 ChannelInboundHandler最后一次对 channelRead()的调用是当前批量读取中的最后一条消息；
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决消息冲刷到远程节点，并且关闭该 Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间，有异常抛出时会调用。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();// 异常打印
        ctx.close(); // 关闭channel
    }
}
