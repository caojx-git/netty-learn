package caojx.learn.netty.cn.itcast.netty.nio.c1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *  利用Files.walk 遍历实现拷贝多级目录
 *
 * @author caojx
 * @since 2024/8/4 23:35
 */
public class TestFileCopy {


    public static void main(String[] args) throws IOException {
        String source = "D:\\Snipaste-1.16.2-x64";
        String target = "D:\\Snipaste-1.16.2-x64aaa";

        Files.walk(Paths.get(source)).forEach(path -> {
            try {
                String targetName = path.toString().replace(source, target);
                // 是目录
                if (Files.isRegularFile(path)) {
//                "D:\\Snipaste-1.16.2-x64\audio";
//                "D:\\Snipaste-1.16.2-x64aaa\audio";
                    Files.createDirectory(Paths.get(targetName));
                }
                // 是普通文件
                else if (Files.isRegularFile(path)) {
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
