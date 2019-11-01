import bean.RocketMqBean;
import config.RocketMqConsumerConfig;
import engine.RocketMqEngine;
import handler.RocketMqHandler;

import java.util.Arrays;

/**
 * @Author qiaozhonghuai
 */

public class TestConsumer {
    public static void main(String []args){
        RocketMqConsumerConfig config = new RocketMqConsumerConfig();
        config.setGroup("Test-MQ");
        config.setAddress("192.168.0.125:9876");
        config.setTopic("Topic");
        config.setTags(Arrays.asList("Tag"));
        config.setPullMode(false);

        RocketMqEngine.engine().startConsumer(config);
        RocketMqEngine.engine().addHandler(config.getGroup(), new RocketMqHandler() {
            @Override
            public void handle(RocketMqBean bean) {
                System.err.println("handle bean......"+bean.toString());
            }

            @Override
            public String topic() {
                return "Topic";
            }

            @Override
            public String tag() {
                return "Tag";
            }
        });

        while(true) {

            try {
                Thread.sleep(1000);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}