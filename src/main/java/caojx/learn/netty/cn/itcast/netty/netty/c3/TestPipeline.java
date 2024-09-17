package caojx.learn.netty.cn.itcast.netty.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/14 22:27
 */
@Slf4j
public class TestPipeline {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 1.通过channel 拿到 pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // 2.添加入站处理器，netty会自动添加头和末尾的handler  head-> h1->h2->h3 ->tail
                        // 入站的数据从前往后传 1->2->3
                        pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("1");

                                ByteBuf buf = (ByteBuf) msg;
                                String name = buf.toString(Charset.defaultCharset());
                                super.channelRead(ctx, name);

                                //  ctx.fireChannelRead(msg); // 将数据传递给下个 handler，如果不调用，调用链会断开
                            }
                        });
                        pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("2");
                                Student student = new Student(msg.toString());
                                super.channelRead(ctx, student);
                            }
                        });
                        pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("3, 结果 {}, class:{}", msg, msg.getClass());
                                super.channelRead(ctx, msg);
                                // 为了触发出站处理器，写入一些数据
                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes())); // 从尾部开始查找出站处理器
                                // ctx.channel().write(msg);
                            }
                        });


                        // 添加出站处理器 head-> h1->h2->h3 ->h4->h5->h6->tail
                        // 出站的数据传递顺序时从后往前传，即 6->5->4
                        pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("4");
                                super.write(ctx, msg, promise);
                                //  ctx.write(msg, promise); //  是从当前节点找上一个出站处理器
                            }
                        });
                        pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("5");
                                super.write(ctx, msg, promise); // 从当前节点找上一个出站处理器
                            }
                        });
                        pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("6");
                                super.write(ctx, msg, promise); // 从当前节点找上一个出站处理器
                            }
                        });

                    }
                }).bind(8080);
    }

    @Data
    @AllArgsConstructor
    static class Student {
        private String name;
    }
}
