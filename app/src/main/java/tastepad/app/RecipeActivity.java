package tastepad.app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tastepad.app.FragmentInstructions.*;

public class
RecipeActivity extends AppCompatActivity {

    MyPagerAdapter adapter;
    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ShoppingList shoppingList;
    private ArrayList<ShoppingItem> mShoppingList;
    MyDBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_screen);

        db = new MyDBHandler(this);

        // setup viewpager swipe tabs
        List<Fragment> fragmentList = getFragments();
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager_id);
        pager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_id);
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);

        // some recipe info and the recipe object
        Bundle extras2 = new Bundle();
        extras2 = getIntent().getExtras();
        String title = (String) extras2.getString("Title");
        final float recipeRating = (float) extras2.getFloat("Rating");
        final int recipeId = (Integer) extras2.getInt("RecipeId");

        String recipeObject = extras2.getString("RECIPE OBJECT");
        final Recipe recipe = new Gson().fromJson(recipeObject, Recipe.class);
        float x = db.getRating(recipe.get_id());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        ratingbar.setRating(x);

        // update rating in database
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.i("rating", "onRatingChanged: " + rating);
                db.addRating(recipe.get_id(), rating);
                Log.i("rating", "onRatingChanged: " + recipe.get_id());
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText("Ingredients");
        tabLayout.getTabAt(1).setText("Instructions");
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        Bundle extras = new Bundle();
        extras = getIntent().getExtras();
        String[][] recipeIngredients = (String[][]) extras.getSerializable("Ingredients");
        String recipeInstructions = (String) extras.getString("Instructions");
        String servings = (String) extras.getString("Servings");

        Log.i("Servings", "RecipeActivity: " + servings);

        // add fragments with instructions and ingredients
        fList.add(FragmentIngredients.newInstance(recipeIngredients, servings));
        fList.add(FragmentInstructions.newInstance(recipeInstructions));
        Log.i("ingredients", Arrays.deepToString(recipeIngredients));
        Log.i("instructions", Arrays.deepToString(recipeIngredients));
        return fList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.menuedit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            // edit current recipe
            case R.id.edit:
                Bundle extras3 = new Bundle();
                extras3 = getIntent().getExtras();
                String[][] recipeIngredients = (String[][]) extras3.getSerializable("Ingredients");
                String recipeInstructions = (String) extras3.getString("Instructions");
                String recipeTitle = (String) extras3.getString("Title");
                int recipeId = (Integer) extras3.getInt("RecipeId");

                Intent i = new Intent(this, EditRecipe.class);
                i.putExtra("Title", recipeTitle);
                i.putExtra("Ingredients", recipeIngredients);
                i.putExtra("Instructions", recipeInstructions);
                i.putExtra("RecipeId", recipeId);
                Log.i("ingredientsToEdit", Arrays.deepToString(recipeIngredients));
                this.startActivity(i);

                return true;

            // add all ingredients to shopping list
            case R.id.addShopping:
                Bundle extras = new Bundle();
                extras = getIntent().getExtras();
                String[][] recipeIngredient = (String[][]) extras.getSerializable("Ingredients");

                for (int x = 0; x < recipeIngredient.length; x++) {

                    String ingredient = recipeIngredient[x][0];
                    String quantity = recipeIngredient[x][1];
                    String unit = recipeIngredient[x][2];
                    ShoppingList.listItems.add(quantity + unit + " " + ingredient);
                }

                Toast.makeText(getApplicationContext(), " Added ingredients to shopping list", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

