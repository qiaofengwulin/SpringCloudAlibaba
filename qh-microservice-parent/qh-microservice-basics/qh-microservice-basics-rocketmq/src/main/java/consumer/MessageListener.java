package consumer;

import engine.RocketMqEngine;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Author qiaozhonghuai
 */

public class MessageListener implements MessageListenerConcurrently {

    private String group;

    public MessageListener(String group){
        this.group = group;
    }

    /**
     * 消息监听器在接收到消息后执行具体的处理逻辑。
     * @param list
     * @param consumeConcurrentlyContext
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if (list != null) {
            RocketMqEngine.engine().handle(this.group, list);
        }
        // 标记该消息已经被成功消费
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}