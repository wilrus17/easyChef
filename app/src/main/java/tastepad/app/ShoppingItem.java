package tastepad.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ShoppingItem {
    private String mText1;
    private Boolean isChecked;

    public ShoppingItem(String itemName) {
        mText1 = itemName;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getItemName() {
        return mText1;
    }

    public String getmText1() {
        return mText1;
    }

    public Boolean getChecked() {
        return isChecked;
    }
}


