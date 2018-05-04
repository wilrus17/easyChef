package tastepad.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyFridge extends AppCompatActivity implements Serializable {

    public static ArrayList<PantryItem> mPantryList;
    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
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

        createPantryList();
        buildRecyclerView();
        loadData();

        final EditText itemName = (EditText) findViewById(R.id.newItem);

        // add item
        Button addItemButton = (Button) findViewById(R.id.newItemFridge);
        addItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String item = itemName.getText().toString();
                insertItem(item);
                itemName.setText("");
            }
        });

        // add item on enter key press
        itemName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        String item = itemName.getText().toString();
                        insertItem(item);
                        itemName.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

    }

    // creates pantry items sent from shopping list
    public void createPantryList() {
        mPantryList = new ArrayList<>();
        ArrayList<String> toPantryList = HeaderRecyclerViewSection.toPantryList;
        if(toPantryList!= null) {
            for (String itemName : toPantryList) {
                PantryItem pantryItem = new PantryItem();
                pantryItem.setName(itemName);
                mPantryList.add(pantryItem);
                toPantryList.remove(itemName);
            }
        }
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.inventory_View);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerFridgeAdapter(mPantryList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    // add item to pantry
    public static void insertItem(String name) {
        Log.i("pantry", "inserting item: " + name);
        mPantryList.add(new PantryItem(name));
        mAdapter.notifyDataSetChanged();
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

    // save to shared preferences
    public void saveData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();

        Gson gson = new Gson();
        String json = gson.toJson(mPantryList);
        Log.i("JSON", "saveData: " + json);
        editor.putString("pantry list", json);
        editor.apply();
    }

    // load from shared preferences

    public boolean loadData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences1.getString("pantry list", null);
        Type type = new TypeToken<ArrayList<PantryItem>>() {
        }.getType();
        Log.i("JSON", "loadData: " + json);
        // put data into new list, if item is checked add to list
        ArrayList<PantryItem> list = new ArrayList<>();
        if(json != null) {
            list.addAll((ArrayList<PantryItem>) gson.fromJson(json, type));
            mPantryList.addAll(list);
            return true;
        } else {
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
}




