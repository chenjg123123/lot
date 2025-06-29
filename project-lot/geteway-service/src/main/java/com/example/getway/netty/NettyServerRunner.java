package com.example.getway.netty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "netty.enabled", havingValue = "true")
public class NettyServerRunner implements ApplicationRunner {

    @Autowired
    private NettyWebSocketServer nettyWebSocketServer;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            try {
                nettyWebSocketServer.start(8081);  // 端口自己设
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

