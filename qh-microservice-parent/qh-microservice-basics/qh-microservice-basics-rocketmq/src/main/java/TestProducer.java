import bean.RocketMqBean;
import config.RocketMqProduceConfig;
import engine.RocketMqEngine;

/**
 * @Author qiaozhonghuai
 */

public class TestProducer {

    public static void main(String[] args) {
        RocketMqProduceConfig config = new RocketMqProduceConfig();
        config.setGroup("Test-MQ");
        config.setAddress("192.168.0.125:9876");
        RocketMqEngine.engine().startProducer(config);

        while (true) {
            RocketMqBean bean = new RocketMqBean();
            bean.setTopic("Topic");
            bean.setTag("Tag");
            bean.setBean(new MyTestBean());

            RocketMqEngine.engine().publish(config.getGroup(), bean);

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

