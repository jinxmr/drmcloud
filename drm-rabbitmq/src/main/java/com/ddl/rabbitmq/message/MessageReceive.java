package com.ddl.rabbitmq.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * 接收RabbitMQ消息的接收方
 */

@Slf4j
@Component
public class MessageReceive {



    /**
     *  queues指定对列名，需要先手工在RabbitMQ上建立队列artisanQueue
     * @param message
     */

    @RabbitListener(queues = "artisanQueue")
    public void processReceivedMsg(String message){
        log.info("artisanQueue Received MSG : {}", message);
    }
}
