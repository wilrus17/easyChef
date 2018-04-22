package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ShoppingList extends AppCompatActivity {

    public DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mItemEdit;
    private Button mAddButton;
    public static ArrayList<ShoppingItem> mShoppingList;
    private Button buttonInsert;
    private EditText editTextInsert;
    private Context mContext;
    static ArrayList<String> listItems = new ArrayList<String>();
    NavigationView navigation;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private SectionedRecyclerViewAdapter sectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Shopping List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        setNavView();


        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        MySection mySection = new MySection();
        sectionAdapter.addSection(mySection);

        loadData();
        buildRecyclerView();

        // sections
        mySection.addItem(0, "BASKET");
        mySection.addItem(3, "BASKET");

        editTextInsert = (EditText) findViewById(R.id.newItem);



        for (String s : listItems) {
            mShoppingList.add(new ShoppingItem(s));
            mAdapter.notifyDataSetChanged();
        }

        // add item on enter key press
        editTextInsert.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        String ingredient = editTextInsert.getText().toString();
                        insertItem(ingredient);
                        Toast.makeText(getApplicationContext(), ingredient + " added", Toast.LENGTH_SHORT).show();
                        saveData();
                        editTextInsert.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void insertItem(String ingredient) {
        mShoppingList.add(new ShoppingItem(ingredient));
        mAdapter.notifyDataSetChanged();
    }

    public void createShoppingList() {
        mShoppingList = new ArrayList<>();
        mShoppingList.add(new ShoppingItem("EGG"));
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.shopping_listView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ShoppingListAdapter(mShoppingList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    // save to shared preferences
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mShoppingList);
        editor.putString("shopping list", json);
        editor.apply();
    }

    // load from shared preferences
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("shopping list", null);
        Type type = new TypeToken<ArrayList<ShoppingItem>>() {
        }.getType();
        mShoppingList = gson.fromJson(json, type);

        if (mShoppingList == null) {
            mShoppingList = new ArrayList<>();
        }
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
                        mDrawerlayout.closeDrawers();
                        break;
                    case R.id.pantry:
                        Intent i = new Intent(ShoppingList.this, MyFridge.class);
                        startActivity(i);
                        break;
                    case R.id.recipes:
                        Intent x = new Intent(ShoppingList.this, MyRecipes.class);
                        startActivity(x);
                        break;
                }
                return false;
            }
        });
    }
}








