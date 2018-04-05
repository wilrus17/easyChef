package tastepad.app;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MyRecipes extends AppCompatActivity {

    private MyDBHandler db;
    private List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);


        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // addtoolbar back arrow
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        RecyclerView myrv = findViewById(R.id.myrecipes_recyclerview);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, recipes);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setAdapter(myAdapter);
            db = new MyDBHandler(this);
            recipes = new ArrayList<>();
            recipes = db.getRecipes();


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
