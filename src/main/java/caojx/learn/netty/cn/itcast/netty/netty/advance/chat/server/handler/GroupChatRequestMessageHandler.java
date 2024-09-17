package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.handler;


import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.GroupChatRequestMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.GroupChatResponseMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        // 获取所有群成员的channel
        List<Channel> channels = GroupSessionFactory.getGroupSession()
                .getMembersChannel(msg.getGroupName());

        // 向所有群成员发送消息
        for (Channel channel : channels) {
            channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
    }
}
