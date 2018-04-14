package tastepad.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ShoppingItem {
    private String mText1;


    public ShoppingItem(String itemName) {
        mText1 = itemName;

    }

    public String getItemName() {
        return mText1;
    }

}

