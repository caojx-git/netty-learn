package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupQuitResponseMessage extends AbstractResponseMessage {
    public GroupQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupQuitResponseMessage;
    }
}
