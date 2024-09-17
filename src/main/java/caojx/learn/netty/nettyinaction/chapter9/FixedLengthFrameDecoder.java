package caojx.learn.netty.nettyinaction.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * 代码清单 9-1 FixedLengthFrameDecoder
 *
 * 扩展 ByteToMessageDecoder 以处理入 站字节，并将它们解码为消息
 *
 * FixedLengthFrameDecoder 一个简单的 ByteToMessageDecoder实现。给定足够的数据，这个实现将
 * 产生固定大小的帧。如果没有足够的数据可供读取，它将等待下一个数据块的到来，并将再次检查是否能够产生一个新的帧。
 * 这个特定的解码器将产生固定为 3 字节大小的帧。因此，它可能会需要多个事件来提供足够的字节数以产生一个帧。
 *
 * @author caojx
 * @since 2024/7/24 11:22
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if(frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be greater than 0, frameLength："+frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= frameLength) { // 检查是否有足够的字节可以被读取，以生成下一个帧
            ByteBuf byteBuf = in.readBytes(frameLength); // 从ByteBuf中读取一个新帧
            out.add(byteBuf); // 将该帧添加到已被解码的消息列表中
        }
    }
}
