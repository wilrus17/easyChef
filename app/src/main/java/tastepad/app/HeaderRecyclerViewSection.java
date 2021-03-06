package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HeaderRecyclerViewSection extends StatelessSection {
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private String title;
    public List<ShoppingItem> list;
    public Context context;
    public static ArrayList<String> toPantryList;


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
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder iHolder = (ItemViewHolder) holder;
        final ShoppingItem currentItem = list.get(position);

        iHolder.itemContent.setText(currentItem.getItemName());
        iHolder.checkBox.setChecked(currentItem.getChecked());
        iHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(currentItem);
                ShoppingList.notifyAdapter();
            }
        });

        // onclick checkbox, toggle checked<->unchecked
        iHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("checkbox", "checked was: " + list.get(position).getChecked());
                if (list.get(position).getChecked()) {
                    list.get(position).setChecked(false);

                } else list.get(position).setChecked(true);

                // add item to other section, remove from current
                ShoppingList.moveItem(currentItem);
                list.remove(currentItem);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)    {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder) holder;
        hHolder.headerTitle.setText(title);

        ViewGroup layout = (ViewGroup) hHolder.clear.getParent();

        if (title.equals("Unchecked")) {
            hHolder.clear.setVisibility(View.GONE);
            hHolder.toPantry.setVisibility(View.GONE);
        }

        // clear checked items
        hHolder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ShoppingItem item : list) {
                    if (item.getChecked()) {
                        list.remove(item);
                        ShoppingList.notifyAdapter();
                    }
                }

            }
        });

        // send checked item names to pantry
        hHolder.toPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> pantryItemNames = new ArrayList<>();
                for (ShoppingItem item : list) {
                    if (item.getChecked()) {
                        String pantryItemName = item.getItemName();
                        pantryItemNames.add(pantryItemName);
                    }
                }
                toPantryList = pantryItemNames;
                Log.i("PANTRY", "Items ready to go to Pantry: " + toPantryList);
                if (toPantryList != null) {
                    Toast.makeText(v.getContext(), "Sent " + toPantryList.size() + " items(s) to the Pantry",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}