package sy.bishe.ygou.bean;

public class AddressBean {
    private int address_id;
    private int address_user_id;
    private String address_name;
    private String address_phone;
    private String address_content;
    private String address_default;
    private String address_area;
    public AddressBean() {
    }

    public AddressBean(int address_id, int address_user_id, String address_name, String address_phone, String address_content, String address_default, String area) {
        this.address_id = address_id;
        this.address_user_id = address_user_id;
        this.address_name = address_name;
        this.address_phone = address_phone;
        this.address_content = address_content;
        this.address_default = address_default;
        this.address_area = area;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getAddress_user_id() {
        return address_user_id;
    }

    public void setAddress_user_id(int address_user_id) {
        this.address_user_id = address_user_id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_phone() {
        return address_phone;
    }

    public void setAddress_phone(String address_phone) {
        this.address_phone = address_phone;
    }

    public String getAddress_content() {
        return address_content;
    }

    public void setAddress_content(String address_content) {
        this.address_content = address_content;
    }

    public String getAddress_default() {
        return address_default;
    }

    public void setAddress_default(String address_default) {
        this.address_default = address_default;
    }

    public String getAddress_area() {
        return address_area;
    }

    public void setAddress_area(String address_area) {
        this.address_area = address_area;
    }
}
