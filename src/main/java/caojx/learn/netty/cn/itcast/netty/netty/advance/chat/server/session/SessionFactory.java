package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session;

public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
