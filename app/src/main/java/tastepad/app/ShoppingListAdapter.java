package tastepad.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder> {

    private ArrayList<ShoppingItem> mShoppingList;



    public static class ShoppingViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;


        public ShoppingViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView1);

        }
    }

    public ShoppingListAdapter(ArrayList<ShoppingItem> shoppingList) {
        mShoppingList = shoppingList;
    }

    @Override
    public ShoppingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);
        ShoppingViewHolder svh = new ShoppingViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder holder, int position) {
        ShoppingItem currentItem = mShoppingList.get(position);

        holder.mTextView1.setText(currentItem.getItemName());
    }

    @Override
    public int getItemCount() {
        return mShoppingList.size();
    }
}




