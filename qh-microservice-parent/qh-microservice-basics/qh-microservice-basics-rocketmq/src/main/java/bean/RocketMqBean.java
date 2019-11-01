package bean;

/**
 * @Author qiaozhonghuai
 */
public class RocketMqBean {
    /**
     * 消息主题
     */
    private String topic;
    /**
     * 消息标签
     */
    private String tag;
    /**
     * 消息体
     */
    private Object bean;

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

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    public boolean equals(Object obj) {
        if(null == obj){
            return false;
        }

        RocketMqBean other = (RocketMqBean)obj;
        if(null == other){
            return false;
        }

        return topic.equalsIgnoreCase(other.getTopic()) && tag.equalsIgnoreCase(other.getTag());
    }

    @Override
    public String toString() {
        return "RocketMqBean{" +
                "topic='" + topic + '\'' +
                ", tag='" + tag + '\'' +
                ", bean=" + bean +
                '}';
    }
}
