package tastepad.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MySection extends StatelessSection {

    String title;
    List<ShoppingItem> shoppingItems;

    public MySection(String title, List<ShoppingItem> shoppingItems){

        super(SectionParameters.builder()
                .itemResourceId(R.layout.shopping_item)
                .headerResourceId(R.layout.item_inventory)
                .build());


        this.title = title;
        this.shoppingItems = shoppingItems;
    }

    @Override
    public int getContentItemsTotal() {
        return shoppingItems.size();

    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


}
