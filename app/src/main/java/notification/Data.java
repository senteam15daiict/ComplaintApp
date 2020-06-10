package notification;

public class Data {
    private String user;
    private Integer icon;
    private String body;
    private String title;
    private String sented;
    private String receiver_user_type;
    private String complaint_id;

    public Data() {
    }

    public Data(String user, Integer icon, String body, String title, String sented, String receiver_user_type, String complaint_id) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.receiver_user_type = receiver_user_type;
        this.complaint_id = complaint_id;
    }

    public String getUser() {
        return user;
    }

    public int getIcon() {
        return icon;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getSented() {
        return sented;
    }

    public String getReceiver_user_type() {
        return receiver_user_type;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public void setReceiver_user_type(String receiver_user_type) {
        this.receiver_user_type = receiver_user_type;
    }
}
