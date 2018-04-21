package tastepad.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

class MySection extends StatelessSection {

    ArrayList<String> myList = new ArrayList<>(10);

    public MySection() {
        // call constructor with layout resources for this Section header and items
        super(SectionParameters.builder()
                .itemResourceId(R.layout.shopping_item)
                .headerResourceId(R.layout.cardview_new_ingredient)
                .build());
    }

    public void addItem(int position, String item) {
        myList.add(position, item);
    }

    public void removeItem(int position) {
        myList.remove(position);
    }

    @Override
    public int getContentItemsTotal() {
        return myList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new ShoppingViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShoppingViewHolder itemHolder = (ShoppingViewHolder) holder;
        itemHolder.tvItem.setText(myList.get(position));
    }

    public static class ShoppingViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem;


        public ShoppingViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.textView1);
        }
    }

}