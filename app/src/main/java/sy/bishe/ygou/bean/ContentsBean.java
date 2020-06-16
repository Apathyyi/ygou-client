package sy.bishe.ygou.bean;

public class ContentsBean {
    private int contents_id;

    private String contents_name;

    private String contents_to_name;

    private String contents_text;

    private int contents_order_id;

    public ContentsBean() {
    }

    public ContentsBean(int contents_id, String contents_name, String contents_to_name, String contents_text, int contents_order_id) {
        this.contents_id = contents_id;
        this.contents_name = contents_name;
        this.contents_to_name = contents_to_name;
        this.contents_text = contents_text;
        this.contents_order_id = contents_order_id;
    }

    public int getContents_id() {
        return contents_id;
    }

    public void setContents_id(int contents_id) {
        this.contents_id = contents_id;
    }

    public String getContents_name() {
        return contents_name;
    }

    public void setContents_name(String contents_name) {
        this.contents_name = contents_name;
    }

    public String getContents_to_name() {
        return contents_to_name;
    }

    public void setContents_to_name(String contents_to_name) {
        this.contents_to_name = contents_to_name;
    }

    public String getContents_text() {
        return contents_text;
    }

    public void setContents_text(String contents_text) {
        this.contents_text = contents_text;
    }

    public int getContents_order_id() {
        return contents_order_id;
    }

    public void setContents_order_id(int contents_order_id) {
        this.contents_order_id = contents_order_id;
    }
}
