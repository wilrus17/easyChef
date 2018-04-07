package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;


import java.util.ArrayList;
import java.util.List;

public class MyRecipes extends AppCompatActivity {

    private AppCompatActivity activity = MyRecipes.this;
    Context context = MyRecipes.this;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Recipe> listRecipe;
    private RecyclerView recyclerViewRecipe;
    private MyDBHandler db;
    SearchView searchBox;
    private ArrayList<Recipe> filteredList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);


        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // addtoolbar back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        initialiseViews();
        initialiseObjects();

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
        recyclerViewRecipe.setAdapter(recyclerViewAdapter);
        db = new MyDBHandler(this);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuplus, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.plus_button:
                Intent i = new Intent(this,NewRecipe.class);
                this.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






}
