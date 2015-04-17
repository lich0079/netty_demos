package io.netty.example.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
    	ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { 
                System.out.print((char) in.readByte());
                System.out.flush();
            }// also can  System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII))
            
            ctx.write(msg); // Netty releases msg for you when it is written out to the wire.
            ctx.flush(); // you could call ctx.writeAndFlush(msg) for brevity.
            ctx.close();
        } finally {
//            ReferenceCountUtil.release(msg); // Alternatively, you could do in.release() here.
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}