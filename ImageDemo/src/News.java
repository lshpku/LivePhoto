class News {
    private int id;
    private String title;
    private String intro;
    private Photo[] photos;
    private int accountId;

    public News(String title, String intro){
        this.title = title;
        this.intro = intro;
    }
    public void setPhoto(Photo[] photots){
        this.photos = photots;
    }
}
