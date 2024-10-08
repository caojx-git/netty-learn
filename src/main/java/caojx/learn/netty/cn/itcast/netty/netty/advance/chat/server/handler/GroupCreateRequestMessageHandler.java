package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.handler;


import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.GroupCreateRequestMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.GroupCreateResponseMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session.Group;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session.GroupSession;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        // 群管理器
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group == null) {
            // 发生成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));
            // 发送拉群消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入" + groupName));
            }
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "已经存在"));
        }
    }
}
