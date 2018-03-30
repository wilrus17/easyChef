package tastepad.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

public class MyRecipes extends AppCompatActivity {

    List<Recipe> Recipe1;

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


        Recipe1 = new ArrayList<>();
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));
        Recipe1.add(new Recipe(55,"Placeholder meal","Category Recipe", R.drawable.mac));

        RecyclerView myrv = findViewById(R.id.myrecipes_recyclerview);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, Recipe1);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setAdapter(myAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
