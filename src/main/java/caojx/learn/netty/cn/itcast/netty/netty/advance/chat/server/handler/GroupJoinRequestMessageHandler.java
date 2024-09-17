package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.handler;

import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.GroupJoinRequestMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.GroupJoinResponseMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session.Group;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().joinMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群加入成功"));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群不存在"));
        }
    }
}
