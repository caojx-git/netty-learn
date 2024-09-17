package caojx.learn.netty.cn.itcast.netty.nio.c1;

import caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil;

import java.nio.ByteBuffer;

public class TestByteBufferReadWrite {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 向buffer中写入1个字节的数据
//        buffer.put((byte)97);
        buffer.put((byte) 0x61); // 'a'
        // 使用工具类，查看buffer状态
        ByteBufferUtil.debugAll(buffer);

        // 向buffer中写入4个字节的数据
        buffer.put(new byte[]{98, 99, 100}); // b  c  d
        ByteBufferUtil.debugAll(buffer);

//        System.out.println(buffer.get());

        // 获取数据
        buffer.flip();
        System.out.println(buffer.get());
        ByteBufferUtil.debugAll(buffer); // 十六进制61 =》转为十进制97
        // 0110 0001  =>  转为十进制 97


        // 使用compact切换到写模式，且把未读取的内容向前移动
        buffer.compact();
        ByteBufferUtil.debugAll(buffer);

        // 再次写入
        buffer.put((byte)102);
        buffer.put((byte)103);
        ByteBufferUtil.debugAll(buffer);
    }
}
