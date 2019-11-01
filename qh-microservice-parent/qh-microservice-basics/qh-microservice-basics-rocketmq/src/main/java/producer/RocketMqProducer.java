package producer;

import bean.RocketMqBean;
import com.alibaba.fastjson.JSON;
import config.RocketMqProduceConfig;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author qiaozhonghuai
 * 每个生产组只允许一个生产实例
 */
public class RocketMqProducer {

    private static final String SEND_OK = "SEND_OK";

    private Logger logger = LoggerFactory.getLogger(RocketMqProducer.class);

    private DefaultMQProducer producer;
    private RocketMqProduceConfig config;

    public RocketMqProducer(RocketMqProduceConfig config){
        this.config = config;
    }

    /**
     * 启动消息生产者
     * @return
     */
    public boolean start(){

        if(null != producer){
            return true;
        }

        producer = new DefaultMQProducer(config.getGroup());
        producer.setNamesrvAddr(config.getAddress());
        /**
         * 决定是否使用VIP通道，即高优先级
         */
        producer.setVipChannelEnabled(false);
        logger.error("[MQ] 生产者启动...");
        try {
            producer.start();
            logger.error("[MQ] 生产者启动成功.");
            return true;
        } catch (MQClientException e) {
            logger.error("[MQ] Start MQ with " + config.getGroup() + "-" + config.getAddress() + " fail:" + e.getErrorMessage(), e);
        }
        return false;
    }

    /**
     * 生产消息
     * @param message
     * @throws MQClientException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQBrokerException
     */
    public boolean send(RocketMqBean message) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {

        /**
         * 发送消息
         *
         * 消息发送失败Producer会重试，最多重试三次如果发送失败则会转到下一个broker，总耗时不超过10s
         * 但并不保证一定成功，对于重要消息在使用消息队列是要考虑，消失丢失的情况，所以重要消息需要在消息发送不成功的时候，建议，
         * 先将消息存储到DB由后台线程定时重试保证消息一定到达broker
         */
        if(producer != null) {
            //设置消息参数
            System.err.println(JSON.toJSONString(message).getBytes().toString());
            Message msg = new Message(message.getTopic(), message.getTag(), JSON.toJSONString(message).getBytes());
            logger.info("[MQ] 开始生产消息------------------>>>>"+msg.toString());
            SendResult result = producer.send(msg);
            if(result.getSendStatus() == SendStatus.SEND_OK){
                logger.info("[MQ] 消息发送成功----------------------------");
                return true;
            }
        }

        return false;
    }

    private void destroy(){
        try {
            if (producer != null) {
                producer.shutdown();
            }
        }
        catch (Exception e){
            logger.warn("[MQ] Stop produce fail: " + e.getMessage(), e);
        }
    }
}
