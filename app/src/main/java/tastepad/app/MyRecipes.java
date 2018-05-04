package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyRecipes extends AppCompatActivity {

    private AppCompatActivity activity = MyRecipes.this;
    Context context = MyRecipes.this;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Recipe> listRecipe;
    private RecyclerView recyclerViewRecipe;
    public DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigation;
    public Spinner spinner;
    public String selectedCategory;
    private String selectedIngredients;

    MyDBHandler db = new MyDBHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Recipe Book");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        setNavView();

        initialiseViews();
        initialiseObjects();


        final EditText searchbar = (EditText) findViewById(R.id.searchbar);

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEmpty(searchbar);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("selected@TextChange", "selected category: " + selectedCategory);
                setString(s.toString());
                if (s.toString().length() == 0) {
                    filterCategory(selectedCategory);
                } else
                    filter(selectedIngredients, selectedCategory);

            }
        });

    }


    private void initialiseViews() {
        recyclerViewRecipe = (RecyclerView) findViewById(R.id.myrecipes_recyclerview);

    }

    private void initialiseObjects() {
        listRecipe = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(listRecipe, this);
        recyclerViewRecipe.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewRecipe.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecipe.setHasFixedSize(true);
        recyclerViewRecipe.setHasFixedSize(true);
        recyclerViewRecipe.setItemViewCacheSize(20);
        recyclerViewRecipe.setDrawingCacheEnabled(true);
        recyclerViewRecipe.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerViewRecipe.setAdapter(recyclerViewAdapter);
        recyclerViewRecipe.setHasFixedSize(true);


        getDataFromSQLite();
    }


    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listRecipe.clear();
                listRecipe.addAll(db.getRecipes());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recyclerViewAdapter.notifyDataSetChanged();
            }


        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.menuplus, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        final Spinner spinner = (Spinner) item.getActionView();

        // fill spinner with categories
        MyDBHandler db = new MyDBHandler(getApplicationContext());
        List<String> categories = db.getAllCategories();
        categories.add(0, "All");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = spinner.getSelectedItem().toString();
                setSelectedCategory(item);

                if (selectedIngredients != null) {
                    filter(selectedIngredients, selectedCategory);
                } else {
                    filterCategory(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }

    public void onBackPressed() {
        if (mDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerlayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        switch (item.getItemId()) {
            // click + to create new recipe
            case R.id.plus_button:
                Intent i = new Intent(this, NewRecipe.class);
                this.startActivity(i);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // refresh with any new recipes
        getDataFromSQLite();
    }

    public boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            etText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear_icon, 0);
            return false;
        }
        etText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0);
        return true;
    }

    private boolean filter(String text, String selectedCategory) {

        if (text == null) {
            filterCategory(selectedCategory);
            return true;
        }
        text = text.toLowerCase();
        String text2 = text.replaceAll("^[,\\s]+", "");

        Log.i("selected@filter", "selected category: " + selectedCategory);

        // early return
        if (text2.isEmpty()) {
            filterCategory(selectedCategory);
            return true;
        }

        // ingredients as list
        List<String> inputList = Arrays.asList((text2.split("[,\\s]+")));

        // filter recipe ids
        ArrayList<Integer> filteredRecipeId = db.getFilteredRecipes(inputList, selectedCategory);
        ArrayList<Recipe> newList = new ArrayList<>();
        System.out.println("Arraylist contains: " + filteredRecipeId.toString());


        for (Recipe recipe : listRecipe) {
            if (filteredRecipeId.contains(recipe.get_id())) {
                newList.add(recipe);

            }
        }
        Log.i("filtered added recipes: ", newList.toString());
        recyclerViewAdapter.filterList(newList);
        return true;
    }

    private boolean filterCategory(String categoryName) {

        Log.i("selected@filterCategory", "selected category: " + selectedCategory);

        if (categoryName == "All") {
            recyclerViewAdapter.filterList(listRecipe);
            return true;
        }

        ArrayList<Integer> filteredRecipesByCategory = db.getCategoryRecipes(categoryName);
        ArrayList<Recipe> newList = new ArrayList<>();
        for (Recipe recipe : listRecipe) {
            if (filteredRecipesByCategory.contains(recipe.get_id())) {
                newList.add(recipe);
            }
        }
        Log.i("filtered by category recipes: ", newList.toString());
        recyclerViewAdapter.filterList(newList);
        return true;
    }


    public void setNavView() {
        navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.sList:
                        Intent i = new Intent(MyRecipes.this, ShoppingList.class);
                        startActivity(i);
                        break;
                    case R.id.pantry:
                        Intent x = new Intent(MyRecipes.this, MyFridge.class);
                        startActivity(x);
                        break;
                    case R.id.recipes:
                        mDrawerlayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
        Log.i("selected", "selected category: " + selectedCategory);
    }

    public void setString(String selectedIngredients) {
        this.selectedIngredients = selectedIngredients;
        Log.i("selected", "selected ingredients: " + selectedIngredients);

    }


}



