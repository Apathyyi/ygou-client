package sy.bishe.ygou.bean;

public class ReleaseBean {

    private int release_id ;

    private int release_user_id;

    private String release_type;

    private String release_title;

    private String release_desc;

    private int release_count;

    private Double release_price;

    private String release_contact;

    private String release_img;

    private String release_time;

    public ReleaseBean() {
    }

    public ReleaseBean(int id, int user_id, String release_typr, String release_title, String release_desc, int release_count, Double release_price, String release_contact, String release_img, String release_time) {
        this.release_id = id;
        this.release_user_id = user_id;
        this.release_type = release_typr;
        this.release_title = release_title;
        this.release_desc = release_desc;
        this.release_count = release_count;
        this.release_price = release_price;
        this.release_contact = release_contact;
        this.release_img = release_img;
        this.release_time = release_time;
    }

    public int getId() {
        return release_id;
    }

    public void setId(int id) {
        this.release_id = id;
    }

    public int getUser_id() {
        return release_user_id;
    }

    public void setUser_id(int user_id) {
        this.release_user_id = user_id;
    }

    public String getRelease_title() {
        return release_title;
    }

    public void setRelease_title(String release_title) {
        this.release_title = release_title;
    }

    public String getRelease_desc() {
        return release_desc;
    }

    public void setRelease_desc(String release_desc) {
        this.release_desc = release_desc;
    }

    public int getRelease_count() {
        return release_count;
    }

    public void setRelease_count(int release_count) {
        this.release_count = release_count;
    }

    public Double getRelease_price() {
        return release_price;
    }

    public void setRelease_price(Double release_price) {
        this.release_price = release_price;
    }

    public String getRelease_type() {
        return release_type;
    }

    public void setRelease_type(String release_type) {
        this.release_type = release_type;
    }

    public String getRelease_contact() {
        return release_contact;
    }

    public void setRelease_contact(String release_contact) {
        this.release_contact = release_contact;
    }

    public String getRelease_img() {
        return release_img;
    }

    public void setRelease_img(String release_img) {
        this.release_img = release_img;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }
}
