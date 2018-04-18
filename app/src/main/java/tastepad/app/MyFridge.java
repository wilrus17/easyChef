package tastepad.app;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MyFridge extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private ArrayList<fridgeItem> mFridgeList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge);

        createFridgeList();
        buildRecyclerView();

        final EditText itemName = (EditText) findViewById(R.id.newItem);
        final TextView itemDate = (TextView) findViewById(R.id.dateText);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Inventory");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // set use by date with calendar
        Button dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        // add item
        Button addItemButton = (Button) findViewById(R.id.newItemFridge);
        addItemButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String item = itemName.getText().toString();
                String date = itemDate.getText().toString();
                insertItem(item, date);
            }
        });
    }

    public void createFridgeList() {
        mFridgeList = new ArrayList<>();
        mFridgeList.add(new fridgeItem("name","date"));
    }

    public void buildRecyclerView(){
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
    public void removeItem(int position){
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

        TextView textView = (TextView) findViewById(R.id.dateText);
        textView.setText(currentDateString);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
        }
    }
