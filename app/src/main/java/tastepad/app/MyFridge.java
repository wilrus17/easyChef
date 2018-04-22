package tastepad.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyFridge extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ArrayList<fridgeItem> mFridgeList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pantry");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        setNavView();

        createFridgeList();
        buildRecyclerView();

        final EditText itemName = (EditText) findViewById(R.id.newItem);
        //final TextView itemDate = (TextView) findViewById(R.id.dateText);




        // add item
        Button addItemButton = (Button) findViewById(R.id.newItemFridge);
        addItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String item = itemName.getText().toString();
                //String date = itemDate.getText().toString();
                // insertItem(item, date);
            }
        });
    }


    public void createFridgeList() {
        mFridgeList = new ArrayList<>();
        mFridgeList.add(new fridgeItem("name", "date"));
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.inventory_View);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerFridgeAdapter(mFridgeList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    // add item to inventory list
    public void insertItem(String name, String date) {
        mFridgeList.add(new fridgeItem(name, date));
        mAdapter.notifyDataSetChanged();
    }

    // remove item from inventory list
    public void removeItem(int position) {
        mFridgeList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());

        //TextView textView = (TextView) findViewById(R.id.dateText);
        //textView.setText(currentDateString);
    }

    public void onBackPressed() {
        if (mDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerlayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setNavView() {
        navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.sList:
                        Intent i = new Intent(MyFridge.this, ShoppingList.class);
                        startActivity(i);
                        break;
                    case R.id.pantry:
                        mDrawerlayout.closeDrawers();
                        break;
                    case R.id.recipes:
                        Intent x = new Intent(MyFridge.this, MyRecipes.class);
                        startActivity(x);
                        break;
                }
                return false;
            }
        });
    }
}




