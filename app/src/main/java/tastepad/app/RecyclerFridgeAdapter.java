package tastepad.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.ISODateTimeFormat;
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
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mPantryList.get(position).setDate(sdf.format(myCalendar.getTime()));
                holder.tv_date.setText(mPantryList.get(position).getDate());
                Date today = new Date();
                Log.i("DATECHECK", "setItemDate: "+ sdf.format(myCalendar.getTime()));
                String input2 = sdf.format(myCalendar.getTime());
                String input1 = sdf.format(today);
                DateTimeFormatter f = DateTimeFormatter.ofPattern( "dd/MM/uuuu" );
                long days = ChronoUnit.DAYS.between(
                        LocalDate.parse( input1 , DateTimeFormatter.ofPattern( "dd/MM/yyyy" ) ) ,
                        LocalDate.parse( input2 , DateTimeFormatter.ofPattern( "dd/MM/yyyy" ) )
                );
                Log.i("DATECHECK", "set: "+ input1);
                Log.i("DATECHECK", "today: "+ input2);
                Log.i("DATECHECK", "daysbetween= "+ days);

                if(days < 2){
                    holder.tv_date.setTextColor(Color.parseColor("#ff8c00"));
                    notifyDataSetChanged();
                } else holder.tv_date.setTextColor(R.color.darkgrey);
            }
        };

        holder.tv_date.setText(mPantryList.get(position).getDate());

        if(holder.tv_date.getText().toString().length() > 0) {
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date today = new Date();
            String y = sdf.format(today);
            String x = mPantryList.get(position).getDate();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/uuuu");
            long days = ChronoUnit.DAYS.between(
                    LocalDate.parse(y, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    LocalDate.parse(x, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
            if (days < 2) {
                holder.tv_date.setTextColor(Color.parseColor("#ff8c00"));
            }
        }


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
