package caojx.learn.netty.nettyinaction.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 代码清单 9-5 FrameChunkDecoder
 *
 * 应用程序通常需要执行比转换数据更加复杂的任务。例如，你可能需要处理格式不正确的输
 * 入或者过量的数据。在下一个示例中，如果所读取的字节数超出了某个特定的限制，我们将会抛
 * 出一个 TooLongFrameException。这是一种经常用来防范资源被耗尽的方法。
 *
 * 在图9-4中，最大的帧大小已经被设置为3字节。如果一个帧的大小超出了该限制，那么程序将会丢弃它的字节，
 * 并抛出一个 TooLongFrameException。位于 ChannelPipeline 中的其他ChannelHandler 可以选择在 exceptionCaught（）
 * 方法中处理该异常或者忽略它。
 *
 * @author caojx
 * @since 2024/7/24 12:55
 */
public class FrameChunkDecoder extends ByteToMessageDecoder { // 扩展 ByteToMessage-Decoder 以将入站字节解码沩消息

    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) { // 指定将要产生的帧的最大允许大小
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if(readableBytes > maxFrameSize) {
            // discard the bytes
            in.clear();
            throw  new TooLongFrameException(); // 如果该帧太大，则丢弃它并抛出一个 TooLongFrameException
        }
        ByteBuf buf = in.readBytes(readableBytes); // 否则，从ByteBuf中卖取一个新的帧
        out.add(buf); // 将该帧添加到解码消息的 List中
    }
}
