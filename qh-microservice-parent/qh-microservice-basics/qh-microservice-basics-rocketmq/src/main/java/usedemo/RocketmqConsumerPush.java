package usedemo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @Author qiaozhonghuai
 * push
 * 实时性高，但增加服务端负载，消费端能力不同，如果push的速度过快，消费端会出现很多问题
 */
public class RocketmqConsumerPush extends RocketmqConsumerCommon{
    //推
    private DefaultMQPushConsumer consumer;
    public RocketmqConsumerPush(RocketMQBean rocketMQBean) {
        this.consumerGroupName = rocketMQBean.getGroupName();
        this.nameServerAddr = rocketMQBean.getNameServerAddr();
        this.topicName = rocketMQBean.getTopic();
        this.tags = rocketMQBean.getTag();
    }

    /**
     *
     * @param messageModel 集群模式MessageModel.CLUSTERING ;广播模式 MessageModel.BROADCASTING
     * @throws Exception
     */
    public void initconsumer(MessageModel messageModel) throws Exception {

        //创建一个消息消费者，并设置一个消息消费者组
        consumer = new DefaultMQPushConsumer(consumerGroupName);
        //指定 NameServer 地址
        consumer.setNamesrvAddr(nameServerAddr);
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //订阅指定 Topic 下的所有消息
        // 订阅topic，可以对指定消息进行过滤，例如："TopicTest","tagl||tag2||tag3",*或null表示topic所有消息
        consumer.subscribe(topicName, tags);
        /**
         * 集群模式
         * 设置消费端对象属性：MessageModel.CLUSTERING,这种方式可以达到类似于ActiveMQ水平扩展负责均衡消费消息的实现，
         * 但是不一样的是它是天然负载均衡的。该模式可以先启动生产端，再启动消费端，消费端仍然可以消费到生产端的消息，
         * 不过时间不一定.默认该模式
         *
         *  广播模式
         *  设置消费端对象属性：MessageModel.BROADCASTING,
         *  这种模式就是相当于生产端发送数据到MQ，
         *  多个消费端都可以获得到数据。
         *  这个模式消费端必须先开启
         */
        consumer.setMessageModel(messageModel);
        //注册消息监听器
        consumer.registerMessageListener(new MessageListener());
        // 消费者对象在使用之前必须要调用 start 初始化
        consumer.start();
    }

    public void destroy(){
        consumer.shutdown();
    }

    public DefaultMQPushConsumer getConsumer() {
        return consumer;
    }
}
