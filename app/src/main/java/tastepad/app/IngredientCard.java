package tastepad.app;

public class IngredientCard {

    private String Name;
    private String Quantity;

    public IngredientCard(){

    }

    public IngredientCard(String name, String quantity) {
        Name = name;
        Quantity = quantity;
    }


    // getters

    public String getName() {
        return Name;
    }

    public String getQuantity() {
        return Quantity;
    }

    // setters

    public void setName(String name) {
        Name = name;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
