package tastepad.app;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import tastepad.app.R;

public class ItemViewHolder extends RecyclerView.ViewHolder{
    public TextView itemContent;
    public ItemViewHolder(View itemView) {
        super(itemView);
        itemContent = (TextView)itemView.findViewById(R.id.textView1);
    }
}