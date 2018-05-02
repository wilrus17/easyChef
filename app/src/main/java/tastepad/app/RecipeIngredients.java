package tastepad.app;

public class RecipeIngredients {

    private int recipe_id;
    private int ingredient_id;
    private String quantity;
    private String unit;

    public RecipeIngredients(int recipe_id, int ingredient_id, String quantity, String unit) {
        this.recipe_id = recipe_id;
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
        this.unit = unit;
    }

    public RecipeIngredients(int recipe_id, int ingredient_id) {
        this.recipe_id = recipe_id;
        this.ingredient_id = ingredient_id;
    }

    public RecipeIngredients(int recipe_id, int ingredient_id, String quantity) {
        this.recipe_id = recipe_id;
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
    }

    public RecipeIngredients(){

    }

    // setters
    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // getters
    public int getRecipe_id() {
        return recipe_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
