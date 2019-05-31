class Photo {
    private int id;
    private String name;
    private String intro;
    private byte[] photo;
    Photo(String intro, byte[] photo){
        this.intro = intro;
        this.photo = photo;
    }
}
