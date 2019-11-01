package engine;

import bean.RocketMqBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import config.RocketMqConsumerConfig;
import config.RocketMqProduceConfig;
import consumer.RocketMqConsumerPull;
import consumer.RocketMqConsumerPush;
import handler.RocketMqHandler;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producer.RocketMqProducer;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author qiaozhonghuai
 */
public class RocketMqEngine {

    private Logger logger = LoggerFactory.getLogger(RocketMqEngine.class);

    private static RocketMqEngine engine = new RocketMqEngine();

    private static ConcurrentHashMap<String, List<RocketMqHandler>> _handler = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, RocketMqProducer> _producers = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, RocketMqConsumerPull> _consumerPull = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, RocketMqConsumerPush> _consumerPush = new ConcurrentHashMap<>();

    public static RocketMqEngine engine(){
        return engine;
    }

    public void addHandler(String group, RocketMqHandler handler){
        if(_handler.contains(group)){
            _handler.get(group).add(handler);
        }
        else{
            _handler.put(group, Arrays.asList(handler));
        }

    }

    //发布任务
    public void publish(String group, RocketMqBean bean){
        try {
            RocketMqProducer producer = _producers.get(group);
            if(producer != null) {
                producer.send(bean);
            }
        }
        catch (Exception e){
            logger.warn("[MQ] Send Message fail", e);
        }
    }
    /**
     * 启动消息生产者
     * @param config
     * @return
     */
    public boolean startProducer(RocketMqProduceConfig config){
        if(null == config){
            return false;
        }

        String group = config.getGroup();
        if(null == group || group.trim().equalsIgnoreCase("")){
            return false;
        }

        if(_producers.contains(group)){
            return true;
        }
        else{
            RocketMqProducer producer = new RocketMqProducer(config);
            producer.start();

            _producers.put(group, producer);
            return true;
        }
    }

    public boolean startConsumer(RocketMqConsumerConfig config){
        if(null == config){
            return false;
        }
        try {
            String group = config.getGroup();
            if(null == group || group.trim().equalsIgnoreCase("")){
                return false;
            }

            if(config.isPullMode()){
                /**
                 * 启动一个主动拉取模式的消费者
                 */
                if(_consumerPull.contains(group)){
                    return true;
                }
                else {
                    RocketMqConsumerPull consumer = new RocketMqConsumerPull(config);
                    consumer.start();

                    _consumerPull.put(group, consumer);
                }
            }
            else {
                /**
                 * 启动一个推送模式的消费者
                 */
                if(_consumerPush.contains(group)){
                    return true;
                }
                else {
                    RocketMqConsumerPush consumer = new RocketMqConsumerPush(config);
                    consumer.start();

                    _consumerPush.put(group, consumer);
                }
            }

            return true;
        }
        catch (Exception e) {
            logger.warn("[MQ] Start consumer fail:" + e.getMessage(), e);
        }

        return false;
    }

    public void handle(String group, List<MessageExt> messages){
        if(messages == null || messages.isEmpty()){
            return;
        }
        for (MessageExt message : messages) {
            RocketMqBean bean = parse(message);
            if(null != bean){
                process(group, bean);
            }
        }
    }

    private RocketMqBean parse(MessageExt message){
        String string = null;
        try {
            string = new String(message.getBody(), "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            logger.warn("[MQ] 解析消息异常：" + e.getMessage(), e);
            return null;
        }

        JSONObject json = JSON.parseObject(string);

        String topic = json.getString("topic");
        String tag = json.getString("tag");
        Object bean = json.get("bean");
        logger.info("[MQ] 接受到bean------------->>>"+bean.toString());
        RocketMqBean q =  new RocketMqBean();
        q.setBean(bean);
        q.setTag(tag);
        q.setTopic(topic);
        return q;
    }

    private void process(String group, RocketMqBean bean){
        if(_handler.isEmpty()){
            return;
        }

        _handler.forEach((k, v)->{
            if(k.equalsIgnoreCase(group)){
                List<RocketMqHandler> handles = v;
                handles.forEach(handler -> {
                    if(handler.topic().equalsIgnoreCase(bean.getTopic())
                            && handler.tag().equalsIgnoreCase(bean.getTag())){
                        handler.handle(bean);
                    }
                });
            }
        });
    }
}
