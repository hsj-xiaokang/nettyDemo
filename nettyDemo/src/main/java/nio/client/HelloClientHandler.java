package nio.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelloClientHandler extends SimpleChannelInboundHandler<String>  {
	
	 private final static Logger LOGGER = LoggerFactory.getLogger(HelloClientHandler.class);
	 
    @Override
     protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
         
         LOGGER.info("Server say : {}",msg);
     }
     
     @Override
     public void channelActive(ChannelHandlerContext ctx) throws Exception {
         LOGGER.info("Client active ");
         super.channelActive(ctx);
     }
 
     @Override
     public void channelInactive(ChannelHandlerContext ctx) throws Exception {
         LOGGER.info("Client close");
         super.channelInactive(ctx);
     }
}
