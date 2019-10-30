package controller;


import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import usedemo.*;

@RestController
public class RocketController {
    @Autowired
    usedemo.Test Test;
    @Autowired
    RocketmqConsumerIinitializeEngine rocketmqConsumerIinitializeEngine;
	@GetMapping(value = "/tc")
    public String send2()  {
        try {
            RocketMQBean rocketMQBean = new RocketMQBean();
            rocketMQBean.setTopic("test");
            rocketMQBean.setTag("test-tag2");
            rocketMQBean.setGroupName("ProducerTest1");
            rocketMQBean.setNameServerAddr("127.0.0.1:9876");
            rocketmqConsumerIinitializeEngine.start(rocketMQBean, MessageModel.CLUSTERING,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
	@GetMapping(value = "/tp")
    public String send()  {
        try {
            RocketMQBean rocketMQBean = new RocketMQBean();
            rocketMQBean.setTopic("test");
            rocketMQBean.setTag("test-tag2");
            rocketMQBean.setGroupName("ProducerTest1");
            rocketMQBean.setNameServerAddr("127.0.0.1:9876");
            ABean aBean = new ABean();
            aBean.setAge(18);
            aBean.setName("ligang");
            rocketMQBean.setMessageExtBean(aBean);
            //启动一个生产者
            RocketmqProducer producer = new RocketmqProducer(rocketMQBean);
            producer.send(rocketMQBean);
            //生产消息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
