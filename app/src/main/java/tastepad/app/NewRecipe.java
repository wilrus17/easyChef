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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class NewRecipe extends AppCompatActivity {

    EditText textIn;
    EditText quantityIn;
    Spinner unitIn;
    Button buttonAdd;
    LinearLayout container;
    Spinner unitSpinner;

    Button buttonClear;

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

        // defining each ingredient field and the layout
        textIn = (EditText) findViewById(R.id.textin);
        quantityIn = (EditText) findViewById(R.id.quantityin);
        buttonAdd = (Button) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.new_ingredient_layout);
        unitIn = (Spinner) findViewById(R.id.unitin);
        buttonClear = (Button) findViewById(R.id.clear);
        unitSpinner = (Spinner) findViewById(R.id.unitin);

        // Tag for the number of new ingredient rows added
        buttonAdd.setTag(1);

        // clears top ingredient field
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textIn.setText("");
                quantityIn.setText("");

            }
        });

        // Add ingredient row
        buttonAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final int rowsAdded = (Integer)arg0.getTag();
                arg0.setTag(rowsAdded+1);

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.cardview_new_ingredient, null);

                EditText textOut = (EditText) addView.findViewById(R.id.new_ingredient_text);
                EditText quantityOut = (EditText) addView.findViewById(R.id.new_ingredient_quantity);
                Spinner unitOut = (Spinner) addView.findViewById(R.id.new_ingredient_unit);

                // Remove an ingredient row
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        v.setTag(rowsAdded-1);
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });

                container.addView(addView);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tick_button:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


