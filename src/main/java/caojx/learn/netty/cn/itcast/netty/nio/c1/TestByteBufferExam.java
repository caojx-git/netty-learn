package caojx.learn.netty.cn.itcast.netty.nio.c1;

import java.nio.ByteBuffer;

import static caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil.debugAll;

/**
 * 现象
 * 网络上有多条数据发送给服务端，数据之间使用 \n 进行分隔
 * 但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有3条为
 * - Hello,world\n
 * - I'm zhangsan\n
 * - How are you?\n
 * 变成了下面的两个 byteBuffer (粘包，半包)
 * - hello,world\nI'm zhangsan\nHo
 * - w are you?\n
 * 出现原因
 * 粘包
 * 发送方在发送数据时，并不是一条一条地发送数据，而是将数据整合在一起，当数据达到一定的数量后再一起发送。这就会导致多条信息被放在一个缓冲区中被一起发送出去
 * 半包
 * 接收方的缓冲区的大小是有限的，当接收方的缓冲区满了以后，就需要将信息截断，等缓冲区空了以后再继续放入数据。这就会发生一段完整的数据最后被截断的现象
 *
 * 解决办法
 * - 通过get(index)方法遍历ByteBuffer，遇到分隔符时进行处理。注意：get(index)不会改变position的值
 * - 记录该段数据长度，以便于申请对应大小的缓冲区
 * - 将缓冲区的数据通过get()方法写入到target中
 * - 调用compact方法切换模式，因为缓冲区中可能还有未读的数据
 *
 * @author caojx
 * @since 2024/8/3 10:07
 */
public class TestByteBufferExam {

    public static void main(String[] args) {
        /*
        网络上有多条数据发送给服务端，数据之间使用\n 进行分隔
        但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有了3条为
        Hello,world\n
        I'm zhangsan\n
        How are you?\n

        变成了下面的两个 byteBuffer（粘包，半包）
        hello,world\nI'm zhangsan\nHo
        w are you?\n
        现在要求你编写程序，将错乱的数据恢复成原始的按 \n 分隔的数据


        粘包，半包的产生：
        粘包产生：由于发送数据时，是将多条数据合在一起发送的，这样不用一条一条发送，这样效率会高一点，但是这样容易产生粘包现象。
        半包产生：服务器缓冲区大小是有限的，服务器接收数据时可能只能接受一部分，不能保证一次性全部读取完成，所以会产生半包。
        */

        // 如下模拟分两次接收消息
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 找到一条完整消息
            if (source.get(i) == '\n') {
                // 把完整消息存入ByteBuffer
                int length = i + 1 - source.position(); // 计算消息的长度， 换行符号位置+1-起始位置
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从source读取，向target写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        source.compact();
    }
}
