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
 *
 * Netty 采用了引用计数法来控制回收内存, 每个 ByteBuf 都实现了 ReferenceCounted接口
 * 1. 每个ByteBuf 对象的初始计数为 1
 * 2. 调用 release 方法计数减 1, 如果计数为 0, ByteBuf 内存被回收
 * 3. 调用 retain 方法计数加 1, 表示调用者没用完之前, 其它 handler 即使调用了 release 也不会造成回书
 * 4. 当计数为 0 时, 底层内存会被回收, 这时即使 ByteBuf 对象还在, 其各个方法均无法正常使用
 *
 *
 * 基本规则是, 谁是最后使用, 谁负责 release   ==> 源码
 */
public class TestByteBuf1 {

    static Logger logger = LoggerFactory.getLogger(TestByteBuf1.class);

    public static void main(String[] args) {
        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        logger.info("buf class => {}", buf.getClass());
        log(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        log(buf);

        buf.writeInt(5);
        log(buf);

        /**
         * 扩容
         * 扩容规则是
         * 1. 如果写入后数据大小未超过 512, 则选择下一个 16的整数倍
         * 2. 如果写入后的数据大小超过 512, 则选择下一个 2^n
         * 3. 扩容不能超过 max capacity 会报错, 默认 max capacity 是整数的最大值
         */
        buf.writeInt(6);
        log(buf);

        System.out.println(buf.readByte());
        System.out.println(buf.readByte());
        System.out.println(buf.readByte());
        System.out.println(buf.readByte());
        log(buf);

        // 重复读

    }


    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
