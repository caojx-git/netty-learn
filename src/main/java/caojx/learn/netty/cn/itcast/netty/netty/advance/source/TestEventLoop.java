package caojx.learn.netty.cn.itcast.netty.netty.advance.source;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 类注释，描述 //TODO
 *
 * @author caojx
 * @since 2024/8/26 22:07
 */
public class TestEventLoop {
    public static void main(String[] args) {
        EventLoop eventLoop = new NioEventLoopGroup().next();
        eventLoop.execute(()->{
            System.out.println("hello");
        });
    }
}
