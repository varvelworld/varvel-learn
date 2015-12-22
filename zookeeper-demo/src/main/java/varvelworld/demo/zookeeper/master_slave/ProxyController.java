package varvelworld.demo.zookeeper.master_slave;

import org.apache.http.client.fluent.Request;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import varvelworld.demo.zookeeper.master_slave.common.Citizen;
import varvelworld.demo.zookeeper.master_slave.common.Listener;

import java.io.IOException;

/**
 * Created by luzhonghao on 2015/12/18.
 */
@Controller
@EnableAutoConfiguration
public class ProxyController {

    static String targetAddress;

    @RequestMapping("/")
    @ResponseBody
    String home() throws IOException {
        return Request.Get("http://" + targetAddress + "/").execute().returnContent().asString();
    }


    public static void main(final String[] args) throws Exception {
        Citizen citizen = new Citizen("127.0.0.1:2181", "/leader", new Listener<String>() {
            @Override
            public void on(String param) {
                targetAddress = param;
            }
        });
        targetAddress = citizen.watch();
        SpringApplication.run(ProxyController.class);
    }
}
