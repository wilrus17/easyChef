package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Recipe> mData;

    public RecyclerViewAdapter(Context mContext, List<Recipe> mData) {

        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_recipe,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_recipe_title.setText(mData.get(position).getTitle());
        holder.img_recipe_thumbnail.setImageResource(mData.get(position).getThumbnail());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_recipe_title;
        ImageView img_recipe_thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_recipe_title = (TextView) itemView.findViewById(R.id.recipe_title);
            img_recipe_thumbnail = (ImageView) itemView.findViewById(R.id.recipe_img);
        }
    }

}
