package caojx.learn.netty.cn.itcast.netty.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/14 23:19
 */
public class TestByteBuffer {
    public static void main(String[] args) {
        // 创建
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(); // 不指定初始容量为256，且可以动态扩容
//        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();
//        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        System.out.println(buf); // PooledUnsafeDirectByteBuf(ridx: 0, widx: 0, cap: 256)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            sb.append("a");
        }
        buf.writeBytes(sb.toString().getBytes());
        log(buf); // PooledUnsafeDirectByteBuf(ridx: 0, widx: 300, cap: 512)
    }


    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE); // io.netty.util.internal.StringUtil.NEWLINE
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
