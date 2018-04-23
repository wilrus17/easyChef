package tastepad.app;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import tastepad.app.R;

public class ItemViewHolder extends RecyclerView.ViewHolder{
    public TextView itemContent;
    public CheckBox checkBox;
    public Button deleteButton;
    public ItemViewHolder(View itemView) {
        super(itemView);
        itemContent = (TextView)itemView.findViewById(R.id.textView1);
        checkBox = (CheckBox)itemView.findViewById(R.id.checkBox);
        deleteButton = (Button)itemView.findViewById(R.id.deleteItem);
    }
}