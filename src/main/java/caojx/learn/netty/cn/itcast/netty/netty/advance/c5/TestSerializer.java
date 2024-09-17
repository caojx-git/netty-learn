package caojx.learn.netty.cn.itcast.netty.netty.advance.c5;

import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.config.Config;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.LoginRequestMessage;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.Message;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.protocol.MessageCodecSharable;
import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/24 22:43
 */
public class TestSerializer {

    public static void main(String[] args) {
        MessageCodecSharable codec = new MessageCodecSharable();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        EmbeddedChannel channel = new EmbeddedChannel(loggingHandler, codec, loggingHandler);

        LoginRequestMessage message = new LoginRequestMessage("zhagnsan", "123");

        // 出站（测试序列化）
//        channel.writeOutbound(message);

        // 入站(测试反序列化)
        ByteBuf buf = messageToByteBuf(message);
        channel.writeInbound(buf);
    }


    public static ByteBuf messageToByteBuf(Message msg) {
        int algorithm = Config.getSerializerAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1, 2, 3, 4});
        out.writeByte(1);
        out.writeByte(algorithm);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.values()[algorithm].serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }
}
