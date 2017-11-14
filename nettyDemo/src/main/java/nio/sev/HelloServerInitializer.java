package nio.sev;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

	 //处理耗时的任务，避免io线程EvenLoop阻塞
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

     @Override
     protected void initChannel(SocketChannel ch) throws Exception {
         ChannelPipeline pipeline = ch.pipeline();
 
         // 以("\n")为结尾分割的 解码器
         pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
 
         // 字符串解码 和 编码
         pipeline.addLast("decoder", new StringDecoder());
         pipeline.addLast("encoder", new StringEncoder());
 
         // 自己的逻辑Handler
         pipeline.addLast(group,"handler", new HelloServerHandler());
         pipeline.addLast(group,"handler_next", new HelloServerHandler_next());
     }

}
