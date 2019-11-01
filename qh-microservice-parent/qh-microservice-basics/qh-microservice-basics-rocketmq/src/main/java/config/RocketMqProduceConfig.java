package config;

/**
 * @Author qiaozhonghuai
 */

public class RocketMqProduceConfig {
    //消息生产组
    private String group;
    //nameServer地址
    private String address;


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
}
