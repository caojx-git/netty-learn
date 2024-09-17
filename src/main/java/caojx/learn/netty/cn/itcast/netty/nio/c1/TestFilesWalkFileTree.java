package caojx.learn.netty.cn.itcast.netty.nio.c1;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件夹遍历
 *
 * @author caojx
 * @since 2024/8/4 23:17
 */
public class TestFilesWalkFileTree {
    public static void main(String[] args) throws IOException {
        m1();
    }

    /**
     * 解决：Files.delete(targetDir);
     * 如果目录还有内容，会抛异常 DirectoryNotEmptyException 问题
     */
    private static void m3() throws IOException {
        Files.walkFileTree(Paths.get("D:\\Snipaste-1.16.2-x64- 副本"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("==> 进入" + dir);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);


                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("==> 退出" + dir);
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    /**
     * 遍历jar包数量
     */
    private static void m2() throws IOException {
        // 获取jar包数量
        AtomicInteger jarCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("/Library/Java/JavaVirtualMachines/jdk1.8.0_341.jdk"),
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toString().endsWith(".jar")) {
                            System.out.println(file);
                            jarCount.incrementAndGet();
                        }
                        return super.visitFile(file, attrs);
                    }
                }
        );
        System.out.println("jar count:" + jarCount);
    }

    /**
     * 遍历文件夹和文件数量
     */
    private static void m1() throws IOException {
        // 文件夹个数
        AtomicInteger dirCount = new AtomicInteger();
        // 文件数
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("/Library/Java/JavaVirtualMachines/jdk1.8.0_341.jdk"),
                new SimpleFileVisitor<Path>() {

                    // 进入文件夹之前
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        System.out.println("==>" + dir);
                        dirCount.incrementAndGet();
                        return super.preVisitDirectory(dir, attrs);
                    }

                    // 进入文件
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        System.out.println(file);
                        fileCount.incrementAndGet();
                        return super.visitFile(file, attrs);
                    }
                }
        );

        System.out.println("dir count:" + dirCount);
        System.out.println("file count:" + fileCount);
    }
}
