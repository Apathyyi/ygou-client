package sy.bishe.ygou.bean;

public class MyMessage {
    private String target_name;
    private String from_name;
    private String text;
    private String time;
    private String type;

    public MyMessage() {
    }

    public MyMessage(String target_name, String from_name, String text, String time, String type) {
        this.target_name = target_name;
        this.from_name = from_name;
        this.text = text;
        this.time = time;
        this.type = type;
    }

    public String getTarget_name() {
        return target_name;
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "target_name='" + target_name + '\'' +
                ", from_name='" + from_name + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
