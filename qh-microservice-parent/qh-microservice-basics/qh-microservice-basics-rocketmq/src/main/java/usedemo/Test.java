package usedemo;

import org.springframework.stereotype.Service;

@Service
public class Test implements MQHandler{

    @Override
    public void handle(RocketMQBean bean) {
        System.out.println("拿到消息内容"+bean.getMessageExtBean());
    }
}
