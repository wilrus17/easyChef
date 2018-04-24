package tastepad.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.view.ActionMode.Callback;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.Subject;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> listRecipe;
    private ArrayList<Recipe> filteredList;
    private MyDBHandler db;
    private ActionMode mActionmode;
    public int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public RecyclerViewAdapter(ArrayList<Recipe> listRecipe, Context mContext) {

        this.mContext = mContext;
        this.listRecipe = listRecipe;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewRecipeTitle;
        public ImageView img_RecipeThumbnail;
        public View view;
        public RatingBar ratingBar;


        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            CardView cardView;

            textViewRecipeTitle = (AppCompatTextView) itemView.findViewById(R.id.recipe_title);
            img_RecipeThumbnail = (ImageView) itemView.findViewById(R.id.recipe_img);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarCard);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view (cardview)
        View itemView;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        itemView = mInflater.inflate(R.layout.cardview_item_recipe, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        MyDBHandler db = new MyDBHandler(mContext);

        holder.textViewRecipeTitle.setText(listRecipe.get(position).getRecipename());
        float rating = db.getRating(listRecipe.get(position).get_id());
        if (rating != 0.0f) {
            holder.ratingBar.setRating(rating);
        } else holder.ratingBar.setVisibility(RatingBar.INVISIBLE);

        // get image for recipe cardView
        // holder.img_recipe_thumbnail.setImageResource(mData.get(position).getThumbnail());


        // go to clicked recipe
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // fetch ingredients w/ quantity & unit and put into an array
                int id = listRecipe.get(position).get_id();
                Log.d("id", "clicked recipe id: " + id);

                if (mActionmode != null) {
                    mActionmode.finish();
                }

                MyDBHandler db = new MyDBHandler(mContext);

                String[][] ingredients = db.getRecipeIngredients(id);

                // passing title & instructions to RecipeActivity
                Intent i = new Intent(mContext, RecipeActivity.class);
                i.putExtra("Title", listRecipe.get(position).getRecipename());
                i.putExtra("Instructions", listRecipe.get(position).getInstructions());
                i.putExtra("RecipeId", listRecipe.get(position).get_id());
                i.putExtra("Ingredients", ingredients);
                i.putExtra("Rating", listRecipe.get(position).getRating());
                i.putExtra("Servings", listRecipe.get(position).getServings());
                i.putExtra("RECIPE OBJECT", new Gson().toJson(listRecipe.get(position)));
                Log.i("ingredientsRecycler", Arrays.deepToString(ingredients));

                mContext.startActivity(i);
            }
        });

        // long click item to show contextual menu
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                // get id of clicked recipe
                id = listRecipe.get(position).get_id();
                setId(id);

                mActionmode = ((AppCompatActivity) v.getContext()).startSupportActionMode(mActionModeCallback);
                return true;
            }
        });

    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.example_menu, menu);
            mode.setTitle("choose your option");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                // delete selected recipe
                case R.id.option_1:
                    MyDBHandler db = new MyDBHandler(mContext);
                    db.deleteRecipe(getId());
                    db.close();
                    notifyItemRemoved(id);
                    ((MyRecipes) mContext).finish();
                    Intent intent = new Intent(mContext, MyRecipes.class);
                    mContext.startActivity(intent);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionmode = null;
        }
    };

    @Override
    public int getItemCount() {
        return listRecipe.size();
    }

    public void filterList(ArrayList<Recipe> filteredList) {
        this.filteredList = filteredList;


        listRecipe = filteredList;
        notifyDataSetChanged();
    }


}



