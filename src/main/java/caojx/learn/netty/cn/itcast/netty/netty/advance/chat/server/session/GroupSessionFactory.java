package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.session;

public abstract class GroupSessionFactory {

    private static GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}
