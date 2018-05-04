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
import java.util.Collection;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ShoppingList extends AppCompatActivity {


    public DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mItemEdit;
    private Button mAddButton;
    private List<ShoppingItem> list;
    private Button buttonInsert;
    private EditText editTextInsert;
    private Context mContext;
    static ArrayList<String> listItems = new ArrayList<String>();
    NavigationView navigation;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ITEM = "item";
    private RecyclerView sectionHeader;
    public static SectionedRecyclerViewAdapter sectionAdapter;
    static HeaderRecyclerViewSection firstSection = new HeaderRecyclerViewSection("Unchecked", getDataSource());
    static HeaderRecyclerViewSection secondSection = new HeaderRecyclerViewSection("Checked", getDataSource());

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

        // shopping list with sections
        sectionHeader = (RecyclerView) findViewById(R.id.shopping_listView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShoppingList.this);
        sectionHeader.setLayoutManager(linearLayoutManager);
        sectionHeader.setHasFixedSize(true);

        sectionAdapter = new SectionedRecyclerViewAdapter();
        sectionAdapter.addSection(firstSection);
        sectionAdapter.addSection(secondSection);
        sectionHeader.setAdapter(sectionAdapter);

        loadData();

        editTextInsert = (EditText) findViewById(R.id.newItem);

        // from recipe page add all ingredients
        for (String s : listItems) {
            ShoppingItem newItem = new ShoppingItem(s);
            newItem.setChecked(false);
            firstSection.addItem(newItem);
            sectionAdapter.notifyDataSetChanged();
        }
        listItems.clear();

        // add item on enter key press
        editTextInsert.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        String ingredient = editTextInsert.getText().toString();

                        ShoppingItem item = new ShoppingItem(ingredient);
                        item.setChecked(false);
                        firstSection.addItem(item);
                        sectionAdapter.notifyDataSetChanged();

                        Toast.makeText(getApplicationContext(), ingredient + " added", Toast.LENGTH_SHORT).show();

                        editTextInsert.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        // add item
        Button addItemButton = (Button) findViewById(R.id.newItemShopping);
        addItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ingredient = editTextInsert.getText().toString();

                ShoppingItem item = new ShoppingItem(ingredient);
                item.setChecked(false);
                firstSection.addItem(item);
                sectionAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), ingredient + " added", Toast.LENGTH_SHORT).show();

                editTextInsert.setText("");
            }
        });


    }

    public static void moveItem(ShoppingItem item) {
        if (item.getChecked()) {
            secondSection.addItem(item);
        } else {
            firstSection.addItem(item);
        }
        sectionAdapter.notifyDataSetChanged();
    }

    public static void notifyAdapter() {
        sectionAdapter.notifyDataSetChanged();
    }

    public static void deleteItem() {
        sectionAdapter.notifyDataSetChanged();
    }

    // save to shared preferences
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        List<ShoppingItem> newList = new ArrayList<ShoppingItem>();
        newList.addAll(firstSection.list);
        newList.addAll(secondSection.list);

        String json = gson.toJson(newList);

        Log.i("JSON", "saveData: " + json);
        editor.putString("shopping list", json);

        editor.apply();
    }

    // load from shared preferences
    public void loadData() {
        secondSection.list.clear();
        firstSection.list.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("shopping list", null);
        String json2 = sharedPreferences.getString("list2", null);
        Log.i("JSON", "loadData: " + json);
        Type type = new TypeToken<ArrayList<ShoppingItem>>() {
        }.getType();
        // put data into new list, if item is checked add to list
        ArrayList<ShoppingItem> list = new ArrayList<>();
        if (json != null) {
            list.addAll((List<ShoppingItem>) gson.fromJson(json, type));
            for (ShoppingItem item : list) {
                if (item.getChecked()) {
                    secondSection.list.add(item);
                } else firstSection.list.add(item);
            }
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
                        saveData();
                        Intent i = new Intent(ShoppingList.this, MyFridge.class);
                        startActivity(i);
                        break;
                    case R.id.recipes:
                        saveData();
                        Intent x = new Intent(ShoppingList.this, MyRecipes.class);
                        startActivity(x);
                        break;
                }
                return false;
            }
        });
    }

    private static List<ShoppingItem> getDataSource() {
        List<ShoppingItem> data = new ArrayList<ShoppingItem>();
        return data;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
}








