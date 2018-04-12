package tastepad.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerIngredientAdapter extends RecyclerView.Adapter<RecyclerIngredientAdapter.MyViewHolder> {

    Context mContext;
    List<IngredientCard> mData;

    public RecyclerIngredientAdapter(Context mContext, List<IngredientCard> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_ingredient, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_quantity.setText(mData.get(position).getQuantity());

        // alternate ingredient colours
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAA8FD"));
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_quantity;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.ingredientName);
            tv_quantity = (TextView) itemView.findViewById(R.id.ingredientQuantity);
        }
    }
}
