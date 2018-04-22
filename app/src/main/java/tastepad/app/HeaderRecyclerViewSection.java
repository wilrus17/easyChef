package tastepad.app;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HeaderRecyclerViewSection extends StatelessSection{
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private String title;
    public List<ShoppingItem> list;
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
        ItemViewHolder iHolder = (ItemViewHolder)holder;
        ShoppingItem currentItem = list.get(position);
        iHolder.itemContent.setText(currentItem.getItemName());

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