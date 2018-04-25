package tastepad.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.*;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecyclerFridgeAdapter extends RecyclerView.Adapter<RecyclerFridgeAdapter.MyViewHolder> implements DatePickerDialog.OnDateSetListener {
    private ArrayList<PantryItem> mPantryList;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_date;
        public Button buttonDelete;
        public Button buttonDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.pantryItemName);
            tv_date = (TextView) itemView.findViewById(R.id.pantryDateText);
            buttonDelete = (Button) itemView.findViewById(R.id.button_deletePantryItem);
            buttonDate = (Button) itemView.findViewById(R.id.button_setDate);
        }
    }


    public RecyclerFridgeAdapter(ArrayList<PantryItem> pantryList) {
        mPantryList = pantryList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mPantryList.get(position).setDate(sdf.format(myCalendar.getTime()));
                holder.tv_date.setText(mPantryList.get(position).getDate());
            }

        };
        holder.tv_date.setText(mPantryList.get(position).getDate());
        holder.tv_name.setText(mPantryList.get(position).getName());


        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPantryList.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPantryList.size();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    }
}
