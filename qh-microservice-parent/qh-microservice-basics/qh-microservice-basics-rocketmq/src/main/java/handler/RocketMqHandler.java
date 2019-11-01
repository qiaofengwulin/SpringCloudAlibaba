package handler;


import bean.RocketMqBean;

/**
 * @Author qiaozhonghuai
 */
public interface RocketMqHandler {

    /**
     * 消息处理
     * @param bean
     */
    void handle(RocketMqBean bean);

    /**
     * topic
     * @return
     */
    String topic();

    /**
     * tag
     * @return
     */
    String tag();
}
