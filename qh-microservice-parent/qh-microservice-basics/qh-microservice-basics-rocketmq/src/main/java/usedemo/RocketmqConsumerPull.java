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

    // 记录每个队列的消费进度
    private static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();
    public RocketmqConsumerPull(RocketMQBean rocketMQBean,MessageModel messageModel)throws Exception {
        this.consumerGroupName = rocketMQBean.getGroupName();
        this.nameServerAddr = rocketMQBean.getNameServerAddr();
        this.topicName = rocketMQBean.getTopic();
        this.tags = rocketMQBean.getTag();
        initconsumer(messageModel);
    }
    private void initconsumer(MessageModel messageModel) throws Exception {
        //创建一个消息消费者，并设置一个消息消费者组
        consumer = new DefaultMQPullConsumer(consumerGroupName);
        //指定 NameServer 地址
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.setMessageModel(messageModel);
        //开起消费者
        consumer.start();

        try {
            // 获取Topic的所有队列
            Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues(topicName);
            //  遍历所有队列
            for (MessageQueue mq : mqs) {
                System.out.println("当前主题的消息-----------------------------" + mq);
                //  拉取消息， arg1=消息队列，arg2=消息tag过滤表达式，arg3= 消费组offset（从哪里开始拉去），arg4=一次最大拉去消息数量 ,atg5=超时时间
                PullResultExt pullResult = (PullResultExt) consumer.pull(mq, tags, getMessageQueueOffset(mq), 32,10000);
                putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                switch (pullResult.getPullStatus()) {
                    case FOUND:// 找到消息，输出
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
                    case NO_MATCHED_MSG:// 没有匹配tag的消息
                        System.out.println("无匹配消息");
                        break;
                    case NO_NEW_MSG://该队列没有新消息，消费offset=最大offset
                        System.out.println("没有新消息");
                        break ;
                    case OFFSET_ILLEGAL://offset不合法
                        System.out.println("Offset不合法");
                        break;
                    default:
                        break;
                }
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将消费进度更新到Hash表
     * @param mq 消息队列
     * @param offset offset
     */
    private static void putMessageQueueOffset (MessageQueue mq,long offset){
        offsetTable.put(mq, offset);
    }

    /**
     * 从Hash表中获取当前队列的消费offset
     * @param mq 消息队列
     * @return long类型 offset
     */
    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offsetTable.get(mq);
        if (offset != null)
            return offset;
        return 0;
    }

    public void destroy(){
        consumer.shutdown();
    }

    public DefaultMQPullConsumer getConsumer() {
        return consumer;
    }

}
