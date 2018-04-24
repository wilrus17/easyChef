package tastepad.app;

import android.widget.CheckBox;

public class Category {

    private String Name;
    private Boolean isChecked;


    public Category() {

    }
    public Category(String name) {
        Name = name;
            }

    // getters
    public String getName() {
        return Name;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    // setters
    public void setName(String name) {
        Name = name;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
