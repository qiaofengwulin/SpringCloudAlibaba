package usedemo;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @Author qiaozhonghuai
 */

public class TestConsumer {



    public static void main(String[] args) throws Exception {
        try {
            RocketMQBean rocketMQBean = RocketMQBean.getRocketMQBean();
            rocketMQBean.setTopic("test");
            rocketMQBean.setTag("test-tag2");
            RocketmqConsumerIinitializeEngine.getRocketmqConsumerIinitializeEngine().start(rocketMQBean, MessageModel.BROADCASTING.CLUSTERING,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
