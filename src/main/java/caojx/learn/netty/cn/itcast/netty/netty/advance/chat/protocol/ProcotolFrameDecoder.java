package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 处理粘包半包
 */
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProcotolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public static void main(String[] args) {

    }
}
