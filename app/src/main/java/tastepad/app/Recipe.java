package tastepad.app;

public class Recipe {

    private int _id;
    private String recipename;
    private String description;


    // create new recipe
    public Recipe(String recipename) {
        this.recipename = recipename;
    }

    //set column values
    public void set_id(int _id) {
        this._id = _id;
    }
    public void setRecipename(String recipename) {
        this.recipename = recipename;
    }
    public void setDescription(String description) { this.description = description; }



    //get column values
    public int get_id() { return _id; }
    public String getRecipename() {
        return recipename;
    }
    public String getDescription() { return description; }

}

