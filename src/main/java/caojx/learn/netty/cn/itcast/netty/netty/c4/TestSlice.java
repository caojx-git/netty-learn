package caojx.learn.netty.cn.itcast.netty.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil.log;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/15 22:20
 */
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        log(buf);

        // 切片过程中没有发生数据复制
        // 'a', 'b', 'c', 'd', 'e',
        ByteBuf f1 = buf.slice(0, 5);
        // 'f', 'g', 'h', 'i', 'j'
        ByteBuf f2 = buf.slice(5, 5);
        log(f1);
        log(f2);

        // 修改的还是原来的ByteBuf
        System.out.println("========================");
        f1.setByte(0,'b'); // 可以修改原有索引下的值
        log(f1);
        log(buf);

        // 切片后不允许追加新的内容
//        f1.writeByte('x'); // java.lang.IndexOutOfBoundsException

        // 释放原有byteBuf 内存
        System.out.println("释放原有byteBuf内存");
        f1.retain(); // 引用计数+1
        buf.release(); // 释放原始内存
        log(f1);
    }
}
