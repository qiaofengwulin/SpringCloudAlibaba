package usedemo;

/**
 * @Author qiaozhonghuai
 */
public class TestConsumer {
    public static void main(String[] args) throws Exception {
        //创建一个消息监听器
        MessageListener messageListener = new MessageListener();
        //启动一个消费者
        RocketmqConsumer rocketmqConsumer = new RocketmqConsumer("test_consumer_group","127.0.0.1:9876","test-topic",messageListener,"test-tag");
        rocketmqConsumer.initconsumer();
    }
}
