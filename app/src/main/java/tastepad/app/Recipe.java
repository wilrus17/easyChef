package tastepad.app;

public class Recipe {

    private int ID;
    private String Title;
    private String Category;
    private int Thumbnail;

    public Recipe() {
    }

    public Recipe(int id, String title, String category, int thumbnail){
        ID = id;
        Title = title;
        Category = category;
        Thumbnail = thumbnail;
    }



    public Integer getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getCategory() {
        return Category;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setThumbnail(int thumbnail){
        Thumbnail = thumbnail;
    }


}
