package tastepad.app;

import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.MyViewHolder> {

    private final LayoutInflater mInflater;
    Context mContext;
    ArrayList<Category> Categories;
    public int count = 0;

    public RecyclerCategoryAdapter(Context mContext, ArrayList<Category> Categories, LayoutInflater mInflater) {
        this.mContext = mContext;
        this.Categories = Categories;
        this.mInflater = mInflater;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        if (count >= Categories.size()) {
            v = mInflater.inflate(R.layout.item_new_category, parent, false);
            v.setTag("ADD");
        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);

            v.setTag(null);
        }
        count += 1;
        //v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position < Categories.size()) {
            final Category category = Categories.get(position);
            holder.tv_name.setText(category.getName());
            holder.checkBox.setChecked(category.getChecked());

            // toggle category checked<->unchecked
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (Categories.get(position).getChecked()) {
                        Categories.get(position).setChecked(false);
                    } else {
                        Categories.get(position).setChecked(true);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Categories.size() + 1;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private CheckBox checkBox;
        private EditText editText_newCategory;


        public MyViewHolder(View itemView) {
            super(itemView);

            if (itemView.getTag() == "ADD") {
                tv_name = null;


            } else {
                tv_name = (TextView) itemView.findViewById(R.id.tv_category);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
                editText_newCategory = (EditText) itemView.findViewById(R.id.editText_category);
            }


        }
    }
}
