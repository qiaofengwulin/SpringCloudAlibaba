package usedemo;



/**
 * @Author qiaozhonghuai
 */
public class TestProducer {
    public static void main(String[] args) {
        try {

            Demo demo = new Demo();
            //启动一个生产者
            RocketmqProducer producer = new RocketmqProducer("test","127.0.0.1:9876","test-topic","test-tag");
            producer.send(demo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
