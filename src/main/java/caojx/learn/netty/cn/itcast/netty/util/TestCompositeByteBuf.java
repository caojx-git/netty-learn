package caojx.learn.netty.cn.itcast.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import static caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil.log;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/15 22:34
 */
public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});

        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});

//        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
//        // 发生了两次数据的复制
//        buffer.writeBytes(buf1).writeBytes(buf2);
//        log(buffer);

        // 将多个小的byteBuf组合成一个大的byteBuf，不会发生内存的复制
        CompositeByteBuf buffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        buffer.addComponents(true, buf1, buf2);
        log(buffer);
    }
}
