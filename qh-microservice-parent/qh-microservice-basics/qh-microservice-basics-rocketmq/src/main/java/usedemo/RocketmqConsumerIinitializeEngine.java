package usedemo;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author qiaozhonghuai
 * @date 2019/10/28 16:43
 */
@Component
public class RocketmqConsumerIinitializeEngine implements ApplicationContextAware {

    private static RocketmqConsumerIinitializeEngine rocketmqConsumerIinitializeEngine = new RocketmqConsumerIinitializeEngine();
    private static ConcurrentHashMap<String, RocketMQBean> handler = new ConcurrentHashMap<>();
    private static Map<String, MQHandler> containerContext;
    private static ApplicationContext applicationContext;
    public static RocketmqConsumerIinitializeEngine getRocketmqConsumerIinitializeEngine(){
        return rocketmqConsumerIinitializeEngine;
    }



    public void handle(RocketMQBean bean){
        handler.put(bean.getTopic(),bean);
        System.out.println("handlerMap存储消息了"+handler.toString());
        //获取spring容器的上下文
        containerContext = applicationContext.getBeansOfType(MQHandler.class);

        containerContext.forEach((k, v) -> {
            if (k.equals(bean.getTopic())) {
                v.handle(handler.get(bean.getTopic()));
                return;
            }
        });
    }

    /**
     *
     * @param bean
     * @param messageModel 集群模式MessageModel.CLUSTERING ;广播模式 MessageModel.BROADCASTING
     * @param isToPull 是否开启拉取模式，true开启Pull。 false为 Push推送模式
     */
    public void start(RocketMQBean bean, MessageModel messageModel, Boolean isToPull){
        try {

            if(isToPull){
                //启动一个主动拉取模式的消费者
                RocketmqConsumer rocketmqConsumerClustering = new RocketmqConsumerPull(bean);
                rocketmqConsumerClustering.initconsumer(messageModel);
            }else {
                //启动一个推送模式的消费者
                RocketmqConsumer rocketmqConsumerClustering = new RocketmqConsumerPush(bean);
                rocketmqConsumerClustering.initconsumer(messageModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
