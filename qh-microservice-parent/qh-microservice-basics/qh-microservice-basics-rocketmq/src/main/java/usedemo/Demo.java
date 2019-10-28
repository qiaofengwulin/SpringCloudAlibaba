package usedemo;

import java.util.Date;

public class Demo {
    private Date date = new Date();
    private Integer integer = 15;
    private String string = "你好";

    @Override
    public String toString() {
        return "Demo{" +
                "date=" + date +
                ", integer=" + integer +
                ", string='" + string + '\'' +
                '}';
    }
}
