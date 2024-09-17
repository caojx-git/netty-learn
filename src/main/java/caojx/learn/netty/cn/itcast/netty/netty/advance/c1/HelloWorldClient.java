package caojx.learn.netty.cn.itcast.netty.netty.advance.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/19 22:02
 */
@Slf4j
public class HelloWorldClient {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            send();
        }
        System.out.println("finish");
    }

    private static void send() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        // 会在连接channel 建立成功后，会触发active事件
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 发10次，每次16个字节
//                            for (int i = 0; i < 10; i++) {
//                                ByteBuf buf = ctx.alloc().buffer(16);
//                                buf.writeBytes(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
//                                ch.writeAndFlush(buf);
//                            }


//                            ByteBuf buffer = ctx.alloc().buffer();
//                            for (int i = 0; i < 10; i++) {
//                                buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
//                            }
//                            ctx.writeAndFlush(buffer);

                            // 粘包半包解决方案1：短连接方式，发送完成后就断开
                            ByteBuf buffer = ctx.alloc().buffer();
                            buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});
                            ctx.writeAndFlush(buffer);
                            ctx.channel().close();

                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error");
        } finally {
            worker.shutdownGracefully();
        }
    }
}
