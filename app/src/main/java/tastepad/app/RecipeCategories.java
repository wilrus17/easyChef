package tastepad.app;

public class RecipeCategories {

    private int recipe_id;
    private int category_id;

    public RecipeCategories(int recipe_id, int category_id) {
        this.recipe_id = recipe_id;
        this.category_id = category_id;
    }

    public RecipeCategories(){

    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public int getCategory_id() {
        return category_id;
    }
}
