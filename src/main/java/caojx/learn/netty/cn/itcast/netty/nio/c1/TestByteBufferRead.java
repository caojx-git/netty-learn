package caojx.learn.netty.cn.itcast.netty.nio.c1;

import caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/3 09:36
 */
public class TestByteBufferRead {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip(); // 切换到读模式

//  1.rewind 从头开始读
//        buffer.get(new byte[4]); // 将数据全部读取到字节数组中
//        debugAll(buffer);
//        // 从头开始读
//        buffer.rewind();
//        System.out.println((char)buffer.get());

        // 2.mark & reset
        // mark 做一个标记，记录position位置， rest是将position重置到mark的位置
//        System.out.println((char)buffer.get()); // a
//        System.out.println((char)buffer.get()); // b
//        buffer.mark(); // 加标记，索引为2的位置
//        System.out.println((char)buffer.get()); // c
//        System.out.println((char)buffer.get()); // d
//
//        buffer.reset(); // 将 position 重置到索引 2
//        System.out.println((char)buffer.get()); // c
//        System.out.println((char)buffer.get()); // d


        // 3.get(i) 不会改变读索引的
        System.out.println((char)buffer.get(3)); // d
        ByteBufferUtil.debugAll(buffer);

    }
}
