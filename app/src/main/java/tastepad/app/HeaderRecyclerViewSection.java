package tastepad.app;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HeaderRecyclerViewSection extends StatelessSection{
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private String title;
    public List<ShoppingItem> list;
    Context context;

    public HeaderRecyclerViewSection(String title, List<ShoppingItem> list) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.shopping_item)
                .headerResourceId(R.layout.section_header)
                .build());
        this.title = title;
        this.list = list;
    }
    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    public boolean addItem(ShoppingItem shoppingItem) {
        return list.add(shoppingItem);
    }

    public boolean removeItem(ShoppingItem shoppingItem) {
        return list.remove(shoppingItem);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder iHolder = (ItemViewHolder)holder;
        final ShoppingItem currentItem = list.get(position);
        iHolder.itemContent.setText(currentItem.getItemName());

        iHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                removeItem(currentItem);
            }
        });

    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder)holder;
        hHolder.headerTitle.setText(title);
    }

}