package com.netty.protocol;

import com.netty.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 1. 魔数, 用来在第一时间判定是否是无效数据包
 * 2. 版本号, 可以支持协议的升级
 * 3. 序列化算法, 消息正文到底采用哪种序列化反序列化方式, 可由此扩展
 * 4. 指令类型, 是登录、注册、单聊、群聊、 ... 跟业务相关
 * 5. 请求序号, 为了双工通信, 提供异步能力
 * 6. 正文长度
 * 7. 消息正文
 */
public class MessageCodec extends ByteToMessageCodec<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 1. 4 字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 字节的版本
        out.writeByte(1);
        // 3. 1 字节的序列化方式  jdk 0, json 1
        out.writeByte(0);
        // 4. 1 字节的指令类型
        out.writeByte(msg.getMessageType());
        // 5. 4 字节
        // 无意义, 只为对齐填充
        out.writeByte(0xff);
        out.writeInt(msg.getSequenceId());
        // 6. 获取内容的字节数据
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();

        // 7. 长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
