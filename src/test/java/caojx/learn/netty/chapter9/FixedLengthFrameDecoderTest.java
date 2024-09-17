package caojx.learn.netty.chapter9;

import caojx.learn.netty.nettyinaction.chapter9.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * 代码清单 9-2 测试 FixedLengthFrameDecoder
 *
 * 测试入站消息
 *
 * @author caojx
 * @since 2024/7/24 11:27
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void testFrameDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) { // 创建一个 ByteBuf，并存储 9 字节
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        // 创建一个 EmbeddedChannel，并添加一个 FixedLengthFrameDecoder，其将以 3 字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        // 将数据写入Embedded-Channel
        assertTrue(channel.writeInbound(input.retain()));

        assertTrue(channel.finish()); //  标记 Channel已完成状态

        // 读取所生成的消息，并且验证是否有 3 帧（切片），其中每帧（切片）都为 3 字节
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }


    @Test
    public void testFrameDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) { // 创建一个 ByteBuf，并存储 9 字节
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        // 创建一个 EmbeddedChannel，并添加一个 FixedLengthFrameDecoder，其将以 3 字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        // 将数据写入Embedded-Channel
        assertFalse(channel.writeInbound(input.readBytes(2))); // 返回 false，因为没有一个完整的可供读取的帧
        assertTrue(channel.writeInbound(input.readBytes(7)));

        assertTrue(channel.finish()); //  标记 Channel已完成状态

        // 读取所生成的消息，并且验证是否有 3 帧（切片），其中每帧（切片）都为 3 字节
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }
}
