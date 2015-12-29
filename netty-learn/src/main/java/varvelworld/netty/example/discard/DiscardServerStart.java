package varvelworld.netty.example.discard;

import varvelworld.netty.example.NettyServer;

public class DiscardServerStart {

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new NettyServer(port, DiscardServerHandler::new).run();
    }
}
