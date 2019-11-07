package com.ddl.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MsgController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping("/sendMsg2ArtisanQueue")
    public void sendMsg(){
        log.info("begin to send msg to artisanQueue ....");
        this.amqpTemplate.convertAndSend("artisanQueue","[artisanQueue] I send one msg to u with RabbitMQ");
    }
}
