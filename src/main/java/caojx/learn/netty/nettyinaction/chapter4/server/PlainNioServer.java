package caojx.learn.netty.nettyinaction.chapter4.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 代码清单 4-2 未使用 Netty 的异步网络编程
 *
 * @author caojx
 * @since 2024/7/22 15:04
 */
public class PlainNioServer {

    public void serve(int port) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        ServerSocket ssocket = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);  // 将服务器绑定到选定的端口
        ssocket.bind(address);

        Selector selector  = Selector.open();  // 打开Selector 来处理Channel
        serverChannel.register(selector, SelectionKey.OP_ACCEPT); // 将 ServerSocket 注册到Selector 以接受连接

        final ByteBuffer msg = ByteBuffer.wrap("Hi\r\n".getBytes());
        for (;;){
            try{
                selector.select();  // 等待需要处理的新事件；阻塞将一直持续到下一个传入事件
            }catch (IOException ex){
                ex.printStackTrace();
                // handle exception
                break;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();  // 获取所有接收事件的 Selection-Key 实例
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try{
                    if (key.isAcceptable()){ // 检查事件是否是一个新的已经就绪可以被接受的连接
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, msg.duplicate()); // 接受客户端，并将它注册到选择器
                        System.out.println("Accepted connection from " + client);
                    }

                    if (key.isWritable()){ // 检查套接字是否已经准备好写数据
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()){
                            if(client.write(buffer) == 0){ // 将数据写到已连接的客户端
                                break;
                            }
                        }
                        client.close();// 关闭连接
                    }
                }catch (IOException ex){
                    key.cancel();
                    try{
                        key.channel().close();
                    }catch (IOException cex){
                        // ignore on close
                    }
                }
            }

        }
    }
}
