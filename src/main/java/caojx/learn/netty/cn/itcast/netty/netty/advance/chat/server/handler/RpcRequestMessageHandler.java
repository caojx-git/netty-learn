package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.handler;

import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.RpcRequestMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.RpcResponseMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.service.HelloService;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.service.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) {
        RpcResponseMessage response = new RpcResponseMessage();

        // 设置请求的序列化，表明服务器在响应那个客户端的请求
        response.setSequenceId(message.getSequenceId());
        try {
            // 利用RpcRequestMessage通过反射获取真正的实现对象
            HelloService service = (HelloService) ServicesFactory.getService(Class.forName(message.getInterfaceName()));
            // 获取要调用的方法
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());

            // 调用方法
            Object invoke = method.invoke(service, message.getParameterValue());

            // 调用成功
            response.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            // 调用异常，直接返回异常栈，会导致消息超长，LengthFieldBasedFrameDecoder会报错
//             response.setExceptionValue(e);

            // 只返回原始异常信息
            String msg = e.getCause().getMessage();
             // 调用异常
            response.setExceptionValue(new Exception("远程调用出错:" + msg));
        }
        // 返回结果
        ctx.writeAndFlush(response);
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 有了RpcRequestMessage我们就可以利用反射调用对应service的方法
        RpcRequestMessage message = new RpcRequestMessage(
                1,
                "caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.service.HelloService",
                "sayHello",
                String.class,
                new Class[]{String.class},
                new Object[]{"张三"}
        );
        HelloService service = (HelloService)
                ServicesFactory.getService(Class.forName(message.getInterfaceName()));
        Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
        Object invoke = method.invoke(service, message.getParameterValue());
        System.out.println(invoke);
    }
}
