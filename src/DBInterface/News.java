package DBInterface;

public class News {

    public int id;
    public String account;
    public String title;
    public String intro;
    public Photo[] photos;
    public String stTime;

    News(int id, String account, String title, String intro, String stTime) {
        this.id = id;
        this.account = account;
        this.title = title;
        this.intro = intro;
        this.stTime = stTime;
    }

    void setPhoto(Photo[] photos){
        this.photos = photos;
    }
    
}
