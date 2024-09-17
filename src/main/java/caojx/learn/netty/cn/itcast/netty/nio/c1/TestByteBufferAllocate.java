package caojx.learn.netty.cn.itcast.netty.nio.c1;

import java.nio.ByteBuffer;


public class TestByteBufferAllocate {
    public static void main(String[] args) {
        // class java.nio.HeapByteBuffer  java堆内存，读写效率较低，受到垃圾回收的影响，垃圾回收涉及内存整理，可能会让数据来回搬迁
        System.out.println(ByteBuffer.allocate(16).getClass());
        // class java.nio.DirectByteBuffer java直接内存，读写效率高（少一次数据的拷贝），不会受到GC的影响，分配内存的效率低，需要及时释放内存，如果没有及时释放内存，可能会发生内存泄漏
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
    }
}
