package tastepad.app;

public class Ingredient {
    private int _id;
    private String ingredientname;

    // constructors
    public Ingredient(int _id, String ingredientname) {
        this._id = _id;
        this.ingredientname = ingredientname;
    }

    public Ingredient(String ingredientname){
        this.ingredientname = ingredientname;
    }

    public Ingredient(){

    }

    // setters
    public void set_id(int _id) {
        this._id = _id;
    }
    public void setIngredientname(String ingredientname) {
        this.ingredientname = ingredientname;
    }

    // getters
    public int get_id() {
        return _id;
    }
    public String getIngredientname() {
        return ingredientname;
    }
}