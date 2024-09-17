package caojx.learn.netty.nettyinaction.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 代码清单 9-3 AbsIntegerEncoder
 *
 * 测试出站消息的处理过程和刚才所看到的类似。在下面的例子中，我们将会展示如何使用
 * EmbeddedChannel 来测试一个编码器形式的 ChannelOutboundHandler，编码器是一种
 * 将一种消息格式转换为另一种的组件。你将在下一章中非常详细地学习编码器和解码器，所以
 * 现在我们只需要简单地提及我们正在测试的处理器—AbsIntegerEncoder，它是 Netty 的
 * MessageToMessageEncoder的一个特殊化的实现，用于将负值整数转换为绝对值。
 * 该示例将会按照下列方式工作：
 * - 持有 AbsIntegerEncoder的 EmbeddedChannel将会以 4 字节的负整数的形式写出站数据；
 * - 编码器将从传入的 ByteBuf 中读取每个负整数，并将会调用 Math.abs()方法来获取其绝对值；
 * - 编码器将会把每个负整数的绝对值写到 ChannelPipeline中。
 *
 *
 *
 * @author caojx
 * @since 2024/7/24 12:36
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> { // 扩展 MessageToMessageEncoder 以 将一个消息编码另外一种格式

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= 4) { // 检查是否有足够的字节用来编码
            int value = Math.abs(in.readInt()); // 从输入的ByteBuf中读取下一个整数，并且计算其绝对值
            out.add(value); // 将该整数写人到编码消息的 List 中
        }
    }
}
