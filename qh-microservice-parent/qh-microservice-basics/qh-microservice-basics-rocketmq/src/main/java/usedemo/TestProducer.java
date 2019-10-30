package usedemo;


/**
 * @Author qiaozhonghuai
 */
public class TestProducer {
    public static void main(String[] args) {
        try {
            RocketMQBean rocketMQBean = RocketMQBean.getRocketMQBean();
            rocketMQBean.setTopic("test");
            rocketMQBean.setTag("test-tag2");
            ABean aBean = new ABean();
            aBean.setAge(18);
            aBean.setName("ligang");
            rocketMQBean.setMessageExtBean(aBean);
//            aBean.setA(156666);
             //启动一个生产者
            RocketmqProducer producer = new RocketmqProducer("test1","127.0.0.1:9876", rocketMQBean.getTopic(), rocketMQBean.getTag());

//            System.out.println(JSON.toJSONString(rocketMQBean));
            producer.send(rocketMQBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
