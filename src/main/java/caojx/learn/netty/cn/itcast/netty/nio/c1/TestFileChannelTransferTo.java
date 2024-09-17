package caojx.learn.netty.cn.itcast.netty.nio.c1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/4 22:50
 */
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try (FileChannel from = new FileInputStream("data.txt").getChannel();
             FileChannel to = new FileOutputStream("to.txt").getChannel();) {
            // Channel.transferTo 将数据从一个channel传递到另外一个channel 效率高
            // 底层会利用操作系统的零拷贝进行优化
//            from.transferTo(0, from.size(), to);

            // transferTo传输有限制，每次最多2g，所以可以分多次传输
            long size = from.size();
            // left 变量代表还剩余多少字节
            for(long left = size; left>0;){
                System.out.println("position:" + (size - left) + " left:" + left);
                left -= from.transferTo((size - left), left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
