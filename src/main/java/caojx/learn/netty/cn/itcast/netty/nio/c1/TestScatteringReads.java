package caojx.learn.netty.cn.itcast.netty.nio.c1;

import caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Scattering Reads  分散读取
 *
 * @author caojx
 * @since 2024/8/3 09:57
 */
public class TestScatteringReads {
    public static void main(String[] args) {
        try (FileChannel channel = new RandomAccessFile("3parts.txt", "r").getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(5);

            // 一次性将数据读到多个bytebuffer中
            channel.read(new ByteBuffer[]{b1, b2, b3});
            b1.flip();
            b2.flip();
            b3.flip();

            ByteBufferUtil.debugAll(b1);
            ByteBufferUtil.debugAll(b2);
            ByteBufferUtil.debugAll(b3);
        } catch (IOException e) {
        }
    }
}
