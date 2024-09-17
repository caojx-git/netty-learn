package caojx.learn.netty.cn.itcast.netty.netty.advance.chat.server.service;

public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
