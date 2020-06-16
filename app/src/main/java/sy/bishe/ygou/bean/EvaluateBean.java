package sy.bishe.ygou.bean;

public class EvaluateBean {

    private int evl_id;

    private int evl_user_id;

    private int evl_order_id;

    private String evl_goods_name;

    private String evl_goods_price;

    private String evl_content;

    private String evl_goods_img;

    private String evl_location;

    private String evl_time;

    public EvaluateBean() {
    }

    public EvaluateBean(int evl_id, int evl_user_id, int evl_order_id, String evl_goods_name, String evl_goods_price, String evl_content, String evl_goods_img, String evl_location, String evl_time) {
        this.evl_id = evl_id;
        this.evl_user_id = evl_user_id;
        this.evl_order_id = evl_order_id;
        this.evl_goods_name = evl_goods_name;
        this.evl_goods_price = evl_goods_price;
        this.evl_content = evl_content;
        this.evl_goods_img = evl_goods_img;
        this.evl_location = evl_location;
        this.evl_time = evl_time;
    }

    public int getEvl_id() {
        return evl_id;
    }

    public void setEvl_id(int evl_id) {
        this.evl_id = evl_id;
    }

    public int getEvl_user_id() {
        return evl_user_id;
    }

    public void setEvl_user_id(int evl_user_id) {
        this.evl_user_id = evl_user_id;
    }

    public int getEvl_order_id() {
        return evl_order_id;
    }

    public void setEvl_order_id(int evl_order_id) {
        this.evl_order_id = evl_order_id;
    }

    public String getEvl_goods_name() {
        return evl_goods_name;
    }

    public void setEvl_goods_name(String evl_goods_name) {
        this.evl_goods_name = evl_goods_name;
    }

    public String getEvl_goods_price() {
        return evl_goods_price;
    }

    public void setEvl_goods_price(String evl_goods_price) {
        this.evl_goods_price = evl_goods_price;
    }

    public String getEvl_content() {
        return evl_content;
    }

    public void setEvl_content(String evl_content) {
        this.evl_content = evl_content;
    }

    public String getEvl_goods_img() {
        return evl_goods_img;
    }

    public void setEvl_goods_img(String evl_goods_img) {
        this.evl_goods_img = evl_goods_img;
    }

    public String getEvl_location() {
        return evl_location;
    }

    public void setEvl_location(String evl_location) {
        this.evl_location = evl_location;
    }

    public String getEvl_time() {
        return evl_time;
    }

    public void setEvl_time(String evl_time) {
        this.evl_time = evl_time;
    }
}
