package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewRecipe extends AppCompatActivity {

    EditText textIn;
    EditText quantityIn;
    Spinner unitIn;
    Button buttonAdd;
    LinearLayout container;
    Button buttonClear;
    Button buttonSave;
    EditText recipeTitle;
    MyDBHandler db;
    EditText instructions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // defining recipe title and instructions
        recipeTitle = (EditText) findViewById(R.id.title_input);
        instructions = (EditText) findViewById(R.id.instructionsIn);

        // defining each ingredient field and the layout
        textIn = (EditText) findViewById(R.id.textin);
        quantityIn = (EditText) findViewById(R.id.quantityin);
        buttonAdd = (Button) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.new_ingredient_layout);
        unitIn = (Spinner) findViewById(R.id.unitin);
        buttonClear = (Button) findViewById(R.id.clear);
        buttonSave = (Button) findViewById(R.id.save);
        db = new MyDBHandler(this);

        // clear top ingredient field
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textIn.setText("");
                quantityIn.setText("");

            }
        });

        // ingredient add, remove row
        buttonAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.cardview_new_ingredient, null);

                // remove an ingredient row
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });
                // add an ingredient row
                container.addView(addView);
            }
        });

        // save recipe to database
        buttonSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v2) {

                // save recipe title and instructions to recipes_table
                Recipe recipe = new Recipe();
                recipe.setRecipename(recipeTitle.getText().toString());
                recipe.setInstructions(instructions.getText().toString());
                db.createRecipe(recipe);

                // save recipe ingredients to table
                ViewGroup viewGroup = (ViewGroup) container;
                for (int j = 0; j < viewGroup.getChildCount(); j++){
                    View child = viewGroup.getChildAt(j);
                    ViewGroup group = (ViewGroup) child;
                    ((EditText)group.getChildAt(0)).getText().toString();
                    ((EditText)group.getChildAt(1)).getText().toString();
                    ((Spinner)group.getChildAt(2)).getSelectedItem().toString();
                }


                // close this activity and go back to MyRecipes
                finish();








            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }






}


