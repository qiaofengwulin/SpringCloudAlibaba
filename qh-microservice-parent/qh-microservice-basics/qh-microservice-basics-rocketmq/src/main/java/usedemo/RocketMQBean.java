package usedemo;

/**
 * @Author qiaozhonghuai
 * @date 2019/10/28 16:40
 */
public class RocketMQBean {
    private String topic;
    private String tag;
    private Object messageExtBean;
    private String GroupName;
    private String NameServerAddr;
    private static RocketMQBean rocketMQBean = new RocketMQBean();
    public static RocketMQBean getRocketMQBean(){
        return rocketMQBean;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getMessageExtBean() {
        return messageExtBean;
    }

    public void setMessageExtBean(Object messageExtBean) {
        this.messageExtBean = messageExtBean;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getNameServerAddr() {
        return NameServerAddr;
    }

    public void setNameServerAddr(String nameServerAddr) {
        NameServerAddr = nameServerAddr;
    }
}
