package usedemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 消息监听器
 *  @Author qiaozhonghuai
 */

public class MessageListener implements MessageListenerConcurrently {



    //消息监听器在接收到消息后执行具体的处理逻辑。
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if (list != null) {
            for (MessageExt ext : list) {
                try {
                    /**
                     * 在消费消消息时候，需要考虑到消息的幂等性。
                     */
                    String message = new String(ext.getBody(), "UTF-8");
                    System.out.println("消息监听器监听到消息------------->>>"+message);
                    JSONObject json = JSON.parseObject(message);
                    String topic = json.getString("topic");
                    String tag = json.getString("tag");
                    RocketMQBean rocketMQBean =  new RocketMQBean();
                    rocketMQBean.setMessageExtBean(message);
                    rocketMQBean.setTag(tag);
                    rocketMQBean.setTopic(topic);
                    RocketmqConsumerIinitializeEngine.getRocketmqConsumerIinitializeEngine().handle(rocketMQBean);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        }
        // 标记该消息已经被成功消费
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}