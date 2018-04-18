package tastepad.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerFridgeAdapter extends RecyclerView.Adapter<RecyclerFridgeAdapter.MyViewHolder> {
    private ArrayList<fridgeItem> mFridgeList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.inventoryItemName);
            tv_date = (TextView) itemView.findViewById(R.id.inventoryItemDate);
        }
    }




    public RecyclerFridgeAdapter(ArrayList<fridgeItem> fridgeList) {
        mFridgeList = fridgeList;

        }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(mFridgeList.get(position).getName());
        holder.tv_date.setText(mFridgeList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return mFridgeList.size();
    }




}
