package varvelworld.netty.example.time;

import varvelworld.netty.example.NettyServer;

/**
 * Created by varvelworld on 2015/12/29.
 */
public class TimeServerStart {
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new NettyServer(port, TimeEncoder::new, TimeServerHandler::new).run();
    }
}
