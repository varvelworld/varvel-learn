package varvelworld.demo.zookeeper.master_slave;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import varvelworld.demo.zookeeper.master_slave.common.Candidate;
import varvelworld.demo.zookeeper.master_slave.common.Listener;

@Controller
@EnableAutoConfiguration
public class SampleController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return String.format("Hello World!\n{target port is %s}", machineAddress);
    }
    static ConfigurableApplicationContext ctx;

    static String machineAddress;


    public static void main(final String[] args) throws Exception {
        machineAddress = args[0];
        Candidate candidate = new Candidate("127.0.0.1:2181", "/leader", machineAddress, new Listener<Void>() {
            @Override
            public void on(Void v) {
                ctx = SpringApplication.run(SampleController.class);
            }
        }, new Listener<Void>() {
            @Override
            public void on(Void v) {
                if(ctx != null) {
                    SpringApplication.exit(ctx);
                }
            }
        });
        candidate.election();
        while(true) {
            Thread.sleep(10000);
        }
    }
}