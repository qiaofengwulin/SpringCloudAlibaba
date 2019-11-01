package consumer;

import config.RocketMqConsumerConfig;
import engine.RocketMqEngine;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.impl.consumer.PullResultExt;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @Author qiaozhonghuai
 * pull
 * 消费者从server端拉消息，主动权在消费端，可控性好，
 * 但是时间间隔不好设置，间隔太短，则空请求会多，
 * 浪费资源，间隔太长，则消息不能及时处理
 */
public class RocketMqConsumerPull {

    private Logger logger = LoggerFactory.getLogger(RocketMqConsumerPull.class);

    private RocketMqConsumerConfig config;

    private DefaultMQPullConsumer consumer;

    private ExecutorService executor;

    private static final ConcurrentHashMap<MessageQueue, Long> offsetTable = new ConcurrentHashMap<MessageQueue, Long>();


    public RocketMqConsumerPull(RocketMqConsumerConfig config) {

        this.config = config;
    }

    public boolean start(){
        try{
            /**
             * 创建一个消息消费者，并设置一个消息消费者组
             */
            consumer = new DefaultMQPullConsumer(config.getGroup());
            /**
             * 指定 NameServer 地址
             */
            consumer.setNamesrvAddr(config.getAddress());
            consumer.setVipChannelEnabled(false);
            /**
             * 开起消费者
             */
            consumer.start();

            /**
             * 拉取
             */
            Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues(config.getTopic());


//            /**
//             * 开启线程池
//             */
//            int poolSize = Math.max(1, Math.min(mqs.size(), Runtime.getRuntime().availableProcessors() * 2));
//            BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(512);
//            RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardPolicy();
//            executor = new ThreadPoolExecutor(poolSize,
//                    poolSize,
//                    0,
//                    TimeUnit.SECONDS,
//                    queue,
//                    policy);
//
//            String tag = "*";
//            List<String> tags = config.getTags();
//            if (tags != null && !tags.isEmpty()) {
//                tag = StringUtils.join(tags, "||");
//            }

            for(MessageQueue mq : mqs){
//                String finalTag = tag;
//                executor.submit(() -> {
                    /**
                     * 同步非阻塞，超时时间5秒
                     */
                    try {
                        PullResultExt pullResult = (PullResultExt) consumer.pull(mq,
                                "*",
                                getMessageQueueOffset(mq),
                                32,5000);
                        process(mq, pullResult);
                    }
                    catch (Exception e){
                        logger.warn("[MQ] Process pull message fail:" + e.getMessage(), e);
                    }
//                });
            }

            return true;
        }
        catch (Exception e){
            logger.warn("[MQ] Start Pull consumer fail:" + e.getMessage(), e);
        }
        return false;
    }

    private void process(MessageQueue mq, PullResultExt pullResult){
        putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
        switch (pullResult.getPullStatus()) {
            case FOUND:
                List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                RocketMqEngine.engine().handle(config.getGroup(), messageExtList);
                break;
            case NO_MATCHED_MSG:
                break;
            case NO_NEW_MSG:
                break;
            case OFFSET_ILLEGAL:
                break;
            default:
                break;
        }
    }

//    @Override
//    public void initconsumer(MessageModel messageModel) throws Exception {
//        //创建一个消息消费者，并设置一个消息消费者组
//        consumer = new DefaultMQPullConsumer(consumerGroupName);
//        //指定 NameServer 地址
//        consumer.setNamesrvAddr(nameServerAddr);
//        //开起消费者
//        consumer.start();
//
//        try {
//            //拉取的主题
//            Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues(topicName);
//            for (MessageQueue mq : mqs) {
//                System.out.println("当前主题的消息-----------------------------" + mq);
//                //消息未到达默认是阻塞10秒，
//                PullResultExt pullResult = (PullResultExt) consumer.pull(mq, tags, getMessageQueueOffset(mq), 32,5000);
//                putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
//                switch (pullResult.getPullStatus()) {
//                    case FOUND:
//                        List<MessageExt> messageExtList = pullResult.getMsgFoundList();
//                        for (MessageExt ext : messageExtList) {
//                            //拉取到消息进行处理
//                            String message = new String(ext.getBody(), "UTF-8");
//                            System.out.println("拉取到消息------------->>>"+message);
//                            JSONObject json = JSON.parseObject(message);
//                            String topic = json.getString("topic");
//                            String tag = json.getString("tag");
//                            RocketMqBean rocketMQBean =  new RocketMqBean();
//                            rocketMQBean.setMessageExtBean(message);
//                            rocketMQBean.setTag(tag);
//                            rocketMQBean.setTopic(topic);
//                            engine.RocketMqEngine.getRocketmqConsumerIinitializeEngine().handle(rocketMQBean);
//                        }
//                        break;
//                    case NO_MATCHED_MSG:
//                        break;
//                    case NO_NEW_MSG:
//                        break;
//                    case OFFSET_ILLEGAL:
//                        break;
//                    default:
//                        break;
//                }
//            }
//        } catch (MQClientException e) {
//            e.printStackTrace();
//        }
//    }

    private static void putMessageQueueOffset (MessageQueue mq,long offset){
        offsetTable.put(mq, offset);
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offsetTable.get(mq);
        if (offset != null) {
            return offset;
        }
        return 0;
    }

}
