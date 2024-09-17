package caojx.learn.netty.cn.itcast.netty.nio.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * TestGatheringWrites 集中写入
 *
 * @author caojx
 * @since 2024/8/3 10:00
 */
public class TestGatheringWrites {

    public static void main(String[] args) {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello"); // 5个字节
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("world"); // 5个字节
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好"); // 6个字节（一个中文占用3个字节）

        try (FileChannel channel = new RandomAccessFile("testGatheringWrites.txt", "rw").getChannel()) {
            // 集中写入，一次性写入多个bytebuffer
            channel.write(new ByteBuffer[]{b1, b2, b3});
        } catch (IOException e) {
        }
    }
}
