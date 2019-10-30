package usedemo;

/**
 * @Author qiaozhonghuai
 * @date 2019/10/28 16:40
 */
public class RocketMQBean {
    //消息主题
    private String topic;
    //消息标签
    private String tag;
    //消息正文
    private Object messageExtBean;
    //分组id
    private String GroupName;
    //broker地址
    private String NameServerAddr;

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
