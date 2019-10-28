package usedemo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

/**
 * @Author qiaozhonghuai
 * push
 * 实时性高，但增加服务端负载，消费端能力不同，如果push的速度过快，消费端会出现很多问题
 *
 * pull
 * 消费者从server端拉消息，主动权在消费端，可控性好，
 * 但是时间间隔不好设置，间隔太短，则空请求会多，
 * 浪费资源，间隔太长，则消息不能及时处理
 */
public class RocketmqConsumer {
    private String consumerGroupName ;
    private String nameServerAddr ;
    private String topicName ;
    private MessageListenerConcurrently messageListenerConcurrently;
    //消息标签
    private  String tags ;
    private DefaultMQPushConsumer consumer;
    //private DefaultMQPullConsumer consumer;

    public RocketmqConsumer(String consumerGroupName, String nameServerAddr, String topicName, MessageListenerConcurrently messageListenerConcurrently, String tags) {
        this.consumerGroupName = consumerGroupName;
        this.nameServerAddr = nameServerAddr;
        this.topicName = topicName;
        this.messageListenerConcurrently = messageListenerConcurrently;
        this.tags = tags;
    }

    public void initconsumer() throws Exception {

        //创建一个消息消费者，并设置一个消息消费者组
        consumer = new DefaultMQPushConsumer(consumerGroupName);
        //指定 NameServer 地址
        consumer.setNamesrvAddr(nameServerAddr);
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //订阅指定 Topic 下的所有消息
        // 订阅topic，可以对指定消息进行过滤，例如："TopicTest","tagl||tag2||tag3",*或null表示topic所有消息
        consumer.subscribe(topicName, tags);
/*
     consumer.setMessageModel(MessageModel.BROADCASTING);
      广播模式
      设置消费端对象属性：MessageModel.BROADCASTING,
      这种模式就是相当于生产端发送数据到MQ，
      多个消费端都可以获得到数据。
      这个模式消费端必须先开启

     consumer.setMessageModel(MessageModel.CLUSTERING);
     设置消费端对象属性：MessageModel.CLUSTERING,这种方式可以达到类似于ActiveMQ水平扩展负责均衡消费消息的实现，
     但是不一样的是它是天然负载均衡的。该模式可以先启动生产端，再启动消费端，消费端仍然可以消费到生产端的消息，
     不过时间不一定.默认该模式

*/
        //注册消息监听器
        consumer.registerMessageListener(messageListenerConcurrently);
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
