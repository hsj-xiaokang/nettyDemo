package nio.sev;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(HelloServerHandler.class);
	
	private final static String HSJ = "hsj";

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 收到消息直接打印输出
		LOGGER.info("{} Say : {}",ctx.channel().remoteAddress(),msg);
		// 返回客户端消息 - 我已经接收到了你的消息
//		ctx.writeAndFlush("Received your message !\n");
		if(HSJ.equals(((String)msg).trim())){
			ctx.writeAndFlush(String.format("Received your message i am hander=[%s],input value is [%s]!\n",this.getClass().getName(),msg.toString()));
		}else{
			ctx.fireChannelRead(msg);
		}
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		LOGGER.info("RamoteAddress : {} active !",ctx.channel().remoteAddress());
		ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.info("exception is general");
	}

}
