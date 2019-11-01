package config;

import java.util.List;

/**
 * @Author qiaozhonghuai
 */

public class RocketMqConsumerConfig {
    //消息消费组
    private String group;
    //nameServer地址
    private String address;

    private String topic;

    private List<String> tags;

    private boolean pullMode;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isPullMode() {
        return pullMode;
    }

    public void setPullMode(boolean pullMode) {
        this.pullMode = pullMode;
    }
}
