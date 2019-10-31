package usedemo;

/**
 * @Author qiaozhonghuai
 * @date 2019/10/30 12:39
 */
public abstract class RocketmqConsumerCommon{
    protected String consumerGroupName ;
    protected String nameServerAddr ;
    protected String topicName ;
    //消息标签
    protected  String tags ;
}
