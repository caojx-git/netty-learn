package caojx.learn.netty.nettyinaction.chapter4.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 代码清单 4-1 不通过 Netty 使用 OIO和 NIO
 *
 * @author caojx
 * @since 2024/7/22 14:58
 */
public class PlainOioServer {

    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port); // 将服务器绑定到指定端口

        try {
            final Socket clientSocket = socket.accept(); // 接受连接
            System.out.println("Accepted connection from " + clientSocket);
            new Thread(new Runnable() { // 创建一个新的线程来处理该连接
                @Override
                public void run() {
                    OutputStream out;
                    try {
                        out = clientSocket.getOutputStream();
                        out.write("Hi\r\n".getBytes(Charset.forName("UTF-8"))); // 将消息写给已连接的客户端
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close(); // 关闭连接
                        } catch (Exception ex) {
                            // ignore on close
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
