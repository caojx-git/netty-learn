package caojx.learn.netty.cn.itcast.netty.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/2 22:42
 */
@Slf4j
public class TestByteBuffer {

    public static void main(String[] args) {
        // 获取FileChannel的方式 1.输入输出流 2.RandomAccessFile
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            // 准备缓存区 10个字节
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                // 从channel 读取数据，向buffer写入(读取到的数据暂存到bytebuffer中)
                int len = channel.read(buffer); // 返回值表示读取到的实际字节数
                log.debug("读取到的字节数 {}", len);
                if (len == -1) { // 没有内容了
                    break;
                }

                // 打印buffer的内容
                buffer.flip(); // buffer切换为读模式，切换模式 position=0, limit=capacity
                while (buffer.hasRemaining()) { // 是否还有剩余未读数据
                    byte b = buffer.get();  // 每次读取一个字节
                    log.debug("实际字节 {}", (char) b);
                }
                buffer.clear(); // 切换为写模式，position=0, limit=capacity
            }
        } catch (IOException e) {
        }
    }
}
