package tastepad.app;

public class IngredientCard {

    private String Name;
    private float Quantity;
    private String Unit;

    public IngredientCard(){

    }

    public IngredientCard(String name, float quantity, String unit) {
        Name = name;
        Quantity = quantity;
        Unit = unit;
    }


    // getters

    public String getName() {
        return Name;
    }

    public float getQuantity() {
        return Quantity;
    }

    public String getUnit() {
        return Unit;
    }
    // setters

    public void setName(String name) {
        Name = name;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }
}
