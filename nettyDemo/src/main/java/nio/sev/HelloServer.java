package nio.sev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *  博客：https://yq.aliyun.com/articles/97235
 *  ChannelInboundHandlerAdapter
	ChannelInboundHandlerAdapter是ChannelInboundHandler的一个简单实现，默认情况下不会做任何处理，只是简单的将操作通过fire*方法传递到ChannelPipeline中的下一个ChannelHandler中让链中的下一个ChannelHandler去处理。

	需要注意的是信息经过channelRead方法处理之后不会自动释放（因为信息不会被自动释放所以能将消息传递给下一个ChannelHandler处理）。

	SimpleChannelInboundHandler
	SimpleChannelInboundHandler支持泛型的消息处理，默认情况下消息处理完将会被自动释放，无法提供fire*方法传递给ChannelPipeline中的下一个ChannelHandler,如果想要传递给下一个ChannelHandler需要调用ReferenceCountUtil#retain方法。

	channelRead0方法在将来将会重命名为messageReceived
	
	博客：https://www.cnblogs.com/Anders888/p/5769016.html
	一般用netty来发送和接收数据都会继承SimpleChannelInboundHandler和ChannelInboundHandlerAdapter这两个抽象类，那么这两个到底有什么区别呢？

	其实用这两个抽象类是有讲究的，在客户端的业务Handler继承的是SimpleChannelInboundHandler，而在服务器端继承的是ChannelInboundHandlerAdapter。

	最主要的区别就是SimpleChannelInboundHandler在接收到数据后会自动release掉数据占用的Bytebuffer资源(自动调用Bytebuffer.release())。
	而为何服务器端不能用呢，因为我们想让服务器把客户端请求的数据发送回去，而服务器端有可能在channelRead方法返回前还没有写完数据，因此不能让它自动release。
 * @Description:TODO
 * @author:hsj qq:2356899074
 * @time:2017年11月14日 下午4:38:26
 */
public class HelloServer {
	private final static Logger LOGGER = LoggerFactory.getLogger(HelloServer.class); 
    private static final int portNumber = 7878;
    
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            //udp .channel(NioDatagramChannel.class);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new HelloServerInitializer());

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(portNumber).sync();
            LOGGER.info("serv listen into {} port！",portNumber);
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();

            // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
