package usedemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.consumer.PullResultExt;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author qiaozhonghuai
 * @date 2019/10/30 12:41
 * pull
 * 消费者从server端拉消息，主动权在消费端，可控性好，
 * 但是时间间隔不好设置，间隔太短，则空请求会多，
 * 浪费资源，间隔太长，则消息不能及时处理
 */
public class RocketmqConsumerPull extends RocketmqConsumerCommon {
    //拉
    private DefaultMQPullConsumer consumer;
    private static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();
    public RocketmqConsumerPull(String consumerGroupName, String nameServerAddr, String topicName, String tags) {
        this.consumerGroupName = consumerGroupName;
        this.nameServerAddr = nameServerAddr;
        this.topicName = topicName;
        this.tags = tags;
    }
    @Override
    public void initconsumer(MessageModel messageModel) throws Exception {
        //创建一个消息消费者，并设置一个消息消费者组
        consumer = new DefaultMQPullConsumer(consumerGroupName);
        //指定 NameServer 地址
        consumer.setNamesrvAddr(nameServerAddr);
        //开起消费者
        consumer.start();

        try {
            //拉取的主题
            Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues(topicName);
            for (MessageQueue mq : mqs) {
                System.out.println("当前主题的消息-----------------------------" + mq);
                //消息未到达默认是阻塞10秒，
                PullResultExt pullResult = (PullResultExt) consumer.pullBlockIfNotFound(mq, tags, getMessageQueueOffset(mq), 32);
                putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                switch (pullResult.getPullStatus()) {
                    case FOUND:
                        List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                        for (MessageExt ext : messageExtList) {
                            //拉取到消息进行处理
                            String message = new String(ext.getBody(), "UTF-8");
                            System.out.println("拉取到消息------------->>>"+message);
                            JSONObject json = JSON.parseObject(message);
                            String topic = json.getString("topic");
                            String tag = json.getString("tag");
                            RocketMQBean rocketMQBean =  new RocketMQBean();
                            rocketMQBean.setMessageExtBean(message);
                            rocketMQBean.setTag(tag);
                            rocketMQBean.setTopic(topic);
                            RocketmqConsumerIinitializeEngine.getRocketmqConsumerIinitializeEngine().handle(rocketMQBean);
                        }
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
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    private static void putMessageQueueOffset (MessageQueue mq, long offset){
        offsetTable.put(mq, offset);
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offsetTable.get(mq);
        if (offset != null)
            return offset;
        return 0;
    }

}
