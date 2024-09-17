package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.handler;


import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.LoginRequestMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.LoginResponseMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.service.UserServiceFactory;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            message = new LoginResponseMessage(true, "登录成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message);
    }
}
