package tastepad.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class HeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView headerTitle;
    public Button clear;
    public Button toPantry;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        headerTitle = (TextView) itemView.findViewById(R.id.header_id);
        clear = (Button) itemView.findViewById(R.id.clearBtn);
        toPantry = (Button) itemView.findViewById(R.id.toPantryBtn);
    }
}