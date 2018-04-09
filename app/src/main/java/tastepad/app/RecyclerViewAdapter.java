package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> listRecipe;
    private ArrayList<Recipe> mFilteredList;
    private MyDBHandler db;

    public RecyclerViewAdapter(ArrayList<Recipe> listRecipe, Context mContext) {

        this.mContext = mContext;
        this.listRecipe = listRecipe;
        this.mFilteredList = listRecipe;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewRecipeTitle;
        public ImageView img_RecipeThumbnail;
        public View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            CardView cardView;

            textViewRecipeTitle = (AppCompatTextView) itemView.findViewById(R.id.recipe_title);
            img_RecipeThumbnail = (ImageView) itemView.findViewById(R.id.recipe_img);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view (cardview)
        View itemView;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        itemView = mInflater.inflate(R.layout.cardview_item_recipe,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.textViewRecipeTitle.setText(listRecipe.get(position).getRecipename());
        // get image for recipe cardView
        // holder.img_recipe_thumbnail.setImageResource(mData.get(position).getThumbnail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // passing title & instructions to RecipeActivity
                Intent i = new Intent(mContext,RecipeActivity.class);
                i.putExtra("Title",listRecipe.get(position).getRecipename());
                i.putExtra("Instructions",listRecipe.get(position).getInstructions());
                mContext.startActivity(i);

                // fetching ingredients & send to Recipe Activity


            }
        });

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
}


