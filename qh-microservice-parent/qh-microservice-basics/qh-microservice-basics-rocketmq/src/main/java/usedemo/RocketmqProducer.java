package usedemo;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @Author qiaozhonghuai
 * 每个生产组只允许一个生产实例
 */
public class RocketmqProducer {

    //消息生产组
    private  String producerGroupName ;
    //nameServer地址
    private  String nameServerAddr ;
    //消息主题
    private  String topic;
    //消息标签
    private  String tags;
    private SendResult sendResult;
    private DefaultMQProducer producer;

    public RocketmqProducer(String producerGroupName, String nameServerAddr, String topic, String tags) {
        this.producerGroupName=producerGroupName;
        this.nameServerAddr=nameServerAddr;
        this.topic=topic;
        this.tags=tags;
    }

    /**
     * 生产消息
     * @param message
     * @throws MQClientException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQBrokerException
     */
    public void send(Object message) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        producer = new DefaultMQProducer(producerGroupName);
        producer.setNamesrvAddr(nameServerAddr);
        //决定是否使用VIP通道，即高优先级
        producer.setVipChannelEnabled(false);
        System.out.println("生产者启动----------------------------------------------");
        producer.start();
        //设置消息参数
        Message msg = new Message(topic, tags, JSON.toJSONString(message).getBytes());
        System.out.println("开始生产消息------------------>>>>"+msg.toString());
        //发送消息
        sendResult = producer.send(msg);
        if(sendResult.getSendStatus().toString().equals("SEND_OK")){
            System.out.println("消息发送成功----------------------------");
        }else {
            /**
             *
             *
             * 消息发送失败Producer会重试，最多重试三次如果发送失败则会转到下一个broker，总耗时不超过10s
               但并不保证一定成功，对于重要消息在使用消息队列是要考虑，消失丢失的情况，所以重要消息需要在消息发送不成功的时候，建议，
               先将消息存储到DB由后台线程定时重试保证消息一定到达broker
             */
        }
    }
}
