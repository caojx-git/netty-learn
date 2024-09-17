package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author yihang
 */
@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends Message {
    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
