package caojx.learn.netty.cn.itcast.netty.netty.advance.c5;

import caojx.learn.netty.cn.itcast.netty.netty.advance.chat.protocol.Serializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestGson {
    public static void main(String[] args) {
        // Exception in thread "main" java.lang.UnsupportedOperationException: Attempted to serialize java.lang.Class: java.lang.String. Forgot to register a type adapter?
//        System.out.println(new Gson().toJson(String.class));

        // 需要注册class类型转换器之后才可以成功
        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new Serializer.ClassCodec()).create();
        System.out.println(gson.toJson(String.class));
    }
}
