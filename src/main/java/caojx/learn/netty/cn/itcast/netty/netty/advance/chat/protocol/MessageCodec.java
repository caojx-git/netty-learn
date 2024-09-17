package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.protocol;

import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * message消息编解码器
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    /**
     * 编码消息
     */
    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 1. 4 字节的魔数，这里假设魔数内容为1234
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 字节的版本,
        out.writeByte(1);
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        out.writeByte(0);
        // 4. 1 字节的指令类型，消息类型
        out.writeByte(msg.getMessageType());
        // 5. 4 个字节的请求序列号
        out.writeInt(msg.getSequenceId());
        // 无意义，对齐填充，1个字节
        out.writeByte(0xff);
        // 6. 获取内容的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        // 7. 长度4个字节
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);
    }

    /**
     * 解码消息
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1.魔数 4个字节
        int magicNum = in.readInt();
        // 2. 1 字节的版本,
        byte version = in.readByte();
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        byte serializerType = in.readByte();
        // 4. 1 字节的指令类型，消息类型
        byte messageType = in.readByte();
        // 5. 4 个字节的请求序列号
        int sequenceId = in.readInt();
        // 无意义，对齐填充，1个字节
        in.readByte();

        // 7. 长度4个字节
        int length = in.readInt();

        // 读取内容，并发序列化为Message对象
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
//        if(serializerType == 0) {// jdk序列化
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Message message = (Message) ois.readObject();
//        }
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);

        // 解析出来的消息需要放入out list中，方便给下一个handler用
        out.add(message);
    }
}