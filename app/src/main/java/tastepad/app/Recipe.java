package tastepad.app;

import java.util.ArrayList;

public class Recipe {

    private int _id;
    private String recipename;
    private String instructions;
    private float rating;
    private String servings;
    private ArrayList<Category> categories;

    // constructors
    public Recipe(int _id, String recipename, String instructions) {
        this._id = _id;
        this.recipename = recipename;
        this.instructions = instructions;
    }

    public Recipe(String recipename, String instructions) {
        this.recipename = recipename;
        this.instructions = instructions;
    }

    public Recipe(){

    }

    // setters
    public void set_id(int _id) {
        this._id = _id;
    }
    public void setRecipename(String recipename) {
        this.recipename = recipename;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public void setServings(String servings) {
        this.servings = servings;
    }
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    // getters
    public int get_id() {
        return _id;
    }
    public String getRecipename() {
        return recipename;
    }
    public String getInstructions() {
        return instructions;
    }
    public float getRating() {
        return rating;
    }
    public String getServings() {
        return servings;
    }
    public ArrayList<Category> getCategories() {
        return categories;
    }
}

