package com.netty.c7;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * Netty 默认的使用的 直接内存
 * ByteBuf  直接内存 VS 堆内存
 * 直接内存： 分配效率小于堆内存, 读写效率高
 * 堆内存： 分配效率高, 读写效率低
 *
 * 直接内存创建和销毁的代价昂贵, 但读写性能高(少了一次内存复制), 适合配合池化功能一起用
 * 直接内存对GC 压力小, 因为这部分内存不受 JVM垃圾回收的管理, 但也要注意及时主动释放
 *
 *
 * 池化 VS 非池化
 * 池化的最大意义在于可以重用ByteBuf
 * 1. 重用池中的ByteBuf 实例
 * 2. 高并发时, 池化功能更节约内存, 减少内存溢出的可能
 *
 * netty 4.1 之前, 池化功能还不成熟, 默认是非池化实现
 * 启动参数 -Dio.netty.allocator.type=unpooled
 */
public class TestByteBuf {

    static Logger logger = LoggerFactory.getLogger(TestByteBuf.class);

    public static void main(String[] args) {
        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
//        final ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();
        logger.info("buf class => {}", buf.getClass());
//        logger.info("buf ==> {}", buf);
        log(buf);

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append("a");
        }
        buf.writeBytes(sb.toString().getBytes());
//        logger.info("buf ==> {}", buf);
        log(buf);
    }


    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 1)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
