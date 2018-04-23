package tastepad.app;

import android.widget.CheckBox;

public class Category {

    private String Name;


    public Category() {

    }
    public Category(String name) {
        Name = name;
            }

    // getters
    public String getName() {
        return Name;
    }



    // setters
    public void setName(String name) {
        Name = name;
    }

   }
