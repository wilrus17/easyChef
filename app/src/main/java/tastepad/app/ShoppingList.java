package tastepad.app;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mItemEdit;
    private Button mAddButton;
    private ArrayList<ShoppingItem> mShoppingList;
    private Button buttonInsert;
    private EditText editTextInsert;
    private Context mContext;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        loadData();
        buildRecyclerView();

        editTextInsert = (EditText) findViewById(R.id.newItem);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Shopping List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ShoppingListAdapter(mShoppingList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    // save shared preferences
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mShoppingList);
        editor.putString("shopping list", json);
        editor.apply();
    }

    // load shared preferences
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


    // toolbar back arrow click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}








