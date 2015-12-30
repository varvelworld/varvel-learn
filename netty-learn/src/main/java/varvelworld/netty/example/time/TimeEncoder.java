package varvelworld.netty.example.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by varvelworld on 2015/12/30.
 */
public class TimeEncoder extends MessageToByteEncoder<UnixTime> {

    @Override
    public void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) throws Exception {
        out.writeInt((int)msg.value());
    }
}
