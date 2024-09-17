package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message;

public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
