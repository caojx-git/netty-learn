package caojx.learn.netty.cn.itcast.netty.netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author caojx
 * @since 2024/8/12 20:40
 */
public class HelloServer {

    public static void main(String[] args) {
        // 1. 启动器，负责组装 netty 组件，启动服务器
        new ServerBootstrap()
                // 2. BossEventLoop, WorkerEventLoop(selector, thread)，Group 组，即NioEventLoopGroup中包含了线程和选择器
                .group(new NioEventLoopGroup())
                // 3. 选择 ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // 4. boss 负责处理连接，worker（child） 负责处理读写，如下的childHandler就是告诉 worker 要做什么事
                .childHandler(
                        // 5. channel 代表和客户端进行数据读写的通道，Initializer 初始化器用于添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                // 6.添加具体的handler
                                ch.pipeline().addLast(new StringDecoder()); // 将 ByteBuf 转换为字符串
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() { // 自定义handler
                                    // 处理读事件
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        // 打印上一步转换好的字符串
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                // 7. 绑定监听端口
                .bind(8080);
    }
}
