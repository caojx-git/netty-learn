package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message;

public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
