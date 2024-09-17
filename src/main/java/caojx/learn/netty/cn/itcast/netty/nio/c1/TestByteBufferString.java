package caojx.learn.netty.cn.itcast.netty.nio.c1;

import caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/3 09:48
 */
public class TestByteBufferString {
    public static void main(String[] args) {
        // 字符串转为 ByteBuffer

        // 1.字符串转为 ByteBuffer
        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        buffer1.put("hello".getBytes());
        ByteBufferUtil.debugAll(buffer1);

        // 2.charset  会自动切换到读模式
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        ByteBufferUtil.debugAll(buffer2);

        // 3. wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        ByteBufferUtil.debugAll(buffer3);



        // bytebuffer 切换为字符串
        String str1 = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(str1);

        // 切换到读模式
        buffer1.flip();
        String str2 = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(str2);
    }
}
