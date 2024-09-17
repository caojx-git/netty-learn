package caojx.learn.netty.chapter9;

import caojx.learn.netty.nettyinaction.chapter9.FrameChunkDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/7/24 12:58
 */
public class FrameChunkDecoderTest {

    @Test
    public void testFrameChunkDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i); // 创建一个 ByteBuf，并向它写入9字节
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3)); // 创建一个 EmbeddedChannel，并向其安装一个帧大小 3字节的 FixedLengthFrameDecoder
        System.out.println(input.readerIndex());

        assertTrue(channel.writeInbound(input.readBytes(2))); // 向它写人2字节，并断言它们将会产生一个新帧
        System.out.println(input.readerIndex());

        try {
            channel.writeInbound(input.readBytes(4)); // 写人一个4字节大小的帧，并捕获预期的TooLongFrameException
            System.out.println(input.readerIndex());
            Assert.fail(); // 如果上面没有抛出异常，那 么就会到达这 个断言，并且 测试失败
        } catch (TooLongFrameException e) {
            // expected exception
        }

        assertTrue(channel.writeInbound(input.readBytes(3))); // 写人剩余的2字节，并断言将会产生一 个有效帧
        System.out.println(input.readerIndex());
        assertTrue(channel.finish()); // 将该 Channel 标记为已完成状态

        // 读取产生的消息，并且验证值
        // read frames
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(2), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();
        buf.release();
    }
}
