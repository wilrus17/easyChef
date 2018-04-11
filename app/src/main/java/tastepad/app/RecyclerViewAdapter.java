package tastepad.app;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.view.ActionMode.Callback;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> listRecipe;
    private ArrayList<Recipe> mFilteredList;
    private MyDBHandler mDbHelper;
    private ActionMode mActionmode;
    public int id;




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
        itemView = mInflater.inflate(R.layout.cardview_item_recipe, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.textViewRecipeTitle.setText(listRecipe.get(position).getRecipename());
        // get image for recipe cardView
        // holder.img_recipe_thumbnail.setImageResource(mData.get(position).getThumbnail());

        // go to clicked recipe
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // fetch ingredients and put into an array
                int recipeId = listRecipe.get(position).get_id();
                //Cursor c = fetchIngredientsById("");


                // passing title & instructions to RecipeActivity
                Intent i = new Intent(mContext, RecipeActivity.class);
                i.putExtra("Title", listRecipe.get(position).getRecipename());
                i.putExtra("Instructions", listRecipe.get(position).getInstructions());
                i.putExtra("RecipeId", listRecipe.get(position).get_id());
                mContext.startActivity(i);

            }
        });

        // hold to show contextual menu
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {


                // get ID
                Recipe r = listRecipe.get(position);
                int id = r.get_id();

                // delete from database
                MyDBHandler db = new MyDBHandler(mContext);
                db.deleteRecipe(id);
                db.close();
                notifyItemRemoved(position);


                if (mActionmode != null)  {
                    return false;
                }

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
                    return true;

                // edit selected recipe
                case R.id.option_2:
                    mode.finish();
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





}



