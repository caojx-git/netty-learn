package caojx.learn.netty.cn.itcast.netty.netty.advance.c1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/20 23:17
 */
public class TestLengthFieldDecoder {

    public static void main(String[] args) {
        EmbeddedChannel channel =
                new EmbeddedChannel(
                        new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 0),
                        new LoggingHandler(LogLevel.DEBUG)
                        );

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        send(buf, "hello, world");
        send(buf, "Hi!");
        channel.writeInbound(buf);
    }

    /**
     * 消息格式：长度字段占用4个字节，版本号，实际内容
     */
    private static void send(ByteBuf buf, String content) {
        byte[] bytes = content.getBytes();// 实际内容
        int length = bytes.length; // 实际内容长度
        buf.writeInt(length); // int 占用4个字节
        buf.writeByte(1); // 1个字节作为版本号
        buf.writeBytes(bytes); // 写入内容
    }
}
