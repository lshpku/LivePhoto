package DBInterface;

public class Photo {

    public int id;
    public String intro;
    public byte[] photo;

    Photo(int id, String intro, byte[] photo){
        this.id = id;
        this.intro = intro;
        this.photo = photo;
    }
}
