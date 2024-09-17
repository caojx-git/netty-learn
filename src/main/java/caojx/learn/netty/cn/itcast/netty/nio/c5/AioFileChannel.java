package caojx.learn.netty.cn.itcast.netty.nio.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static caojx.learn.netty.cn.itcast.netty.util.ByteBufferUtil.debugAll;

/**
 * 异步AIO
 *
 * @author caojx
 * @since 2024/8/11 19:15
 */
@Slf4j
public class AioFileChannel {
    public static void main(String[] args) throws IOException {

        // mac中报错？暂时未定位到原因
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ)) {
            // 参数1 ByteBuffer
            // 参数2 读取的起始位置
            // 参数3 附件(一次性读取不完时，后续继续读取)
            // 参数4 回调方法
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.debug("read begin...");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                /**
                 * read 成功
                 * @param result 读取到的实际字节数
                 *          The result of the I/O operation.
                 * @param attachment 一次性读取不完，用这个attachment来存储下一次的读取结果
                 *          The object attached to the I/O operation when it was initiated.
                 */
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    log.debug("read completed...{}", result);
                    attachment.flip();
                    debugAll(attachment);
                }

                // read 失败
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    log.error("read failed...", exc);
                }
            });
            log.debug("read end...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.in.read();
    }
}
