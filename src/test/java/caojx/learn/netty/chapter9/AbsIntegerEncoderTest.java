package caojx.learn.netty.chapter9;

import caojx.learn.netty.nettyinaction.chapter9.AbsIntegerEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * 代码清单 9-4 测试 AbsIntegerEncoder
 *
 * @author caojx
 * @since 2024/7/24 12:39
 */
public class AbsIntegerEncoderTest {

    /**
     * 1.将 4 字节的负整数写到一个新的 ByteBuf中。
     * 2.创建一个 EmbeddedChannel，并为它分配一个 AbsIntegerEncoder。
     * 3.调用 EmbeddedChannel上的 writeOutbound()方法来写入该 ByteBuf。
     * 4.标记该 Channel为已完成状态。
     * 5.从 EmbeddedChannel的出站端读取所有的整数，并验证是否只产生了绝对值。
     */
    @Test
    public void testEncoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 10; i++) { // 创建一个 ByteBuf，并且写人9个负整数
            buf.writeInt(i * -1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder()); // 创建一个EmbeddedChannel，并安装一个要测试的 AbsIntegerEncoder
        assertTrue(channel.writeOutbound(buf)); // 写人 ByteBuf，并断言调用readOutbound()方法将会产生数据
        assertTrue(channel.finish()); // 将该Channel 标记 已 完成状态


        // read bytes
        for (int i = 0; i < 10; i++) {
            assertEquals(Integer.valueOf(i), channel.readOutbound()); // 读取所产生的消息，并断言它们包含了对 应的绝对值
        }
        assertNull(channel.readOutbound());
    }
}
