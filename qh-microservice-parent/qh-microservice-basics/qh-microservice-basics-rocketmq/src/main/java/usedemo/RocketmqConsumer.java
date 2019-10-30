package usedemo;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @Author qiaozhonghuai
 * @date 2019/10/30 12:28
 */
public interface RocketmqConsumer {
    public void initconsumer(MessageModel messageModel) throws Exception;
}
