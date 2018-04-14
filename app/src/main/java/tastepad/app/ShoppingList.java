package tastepad.app;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        loadData();
        buildRecyclerView();

        editTextInsert = findViewById(R.id.shoppingItemText);
        editTextInsert.addTextChangedListener(addItemWater);

        /*
        buttonInsert =  findViewById(R.id.shoppingItemAdd);




        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = editTextInsert.getText().toString();
                insertItem(ingredient);
                editTextInsert.setText("");
                Toast.makeText(getApplicationContext(), ingredient + " added", Toast.LENGTH_SHORT).show();
                saveData();
            }
        }); */



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);





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
        mAdapter= new ShoppingListAdapter(mShoppingList);

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
        Type type = new TypeToken<ArrayList<ShoppingItem>>() {}.getType();
        mShoppingList = gson.fromJson(json, type);

        if (mShoppingList == null) {
            mShoppingList = new ArrayList<>();
        }
    }


    // disable button if no input text
    private TextWatcher addItemWater = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String itemInput = editTextInsert.getText().toString().trim();

            buttonInsert.setEnabled(!itemInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    // toolbar back arrow click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}






