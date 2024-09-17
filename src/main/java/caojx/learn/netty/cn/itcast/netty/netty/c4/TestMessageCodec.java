package caojx.learn.netty.cn.itcast.netty.netty.c4;

import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.LoginRequestMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.protocol.MessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * 测试消息编解码器
 */
public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        // 消息编解码器测试
        EmbeddedChannel channel = new EmbeddedChannel(
                // 日志打印
                new LoggingHandler(),
                // 避免出现粘包，半包问题
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                // 自定义编解码器
                new MessageCodec()
        );
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        // 消息出站 encode
        channel.writeOutbound(message);


        // 解码消息测试 decode
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf);
        // 入站
        channel.writeInbound(buf);

        // 测试粘包半包
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf2);
        ByteBuf s1 = buf2.slice(0, 100);
        ByteBuf s2 = buf2.slice(100, buf.readableBytes() - 100);
        s1.retain(); // 引用计数 2
        channel.writeInbound(s1); // release 1
        channel.writeInbound(s2);
    }
}
