package tastepad.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.InterfaceAddress;
import java.util.ArrayList;

public class CategoryDialog extends AppCompatDialogFragment {

    View v;
    private RecyclerView myrecyclerview;
    private ArrayList<Category> lstCategory;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_category, null);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.category_recyclerview);
        RecyclerCategoryAdapter recyclerAdapter = new RecyclerCategoryAdapter(getContext(),lstCategory, inflater);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);

        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(getActivity());

                builder.setView(v)
                .setTitle("Categories")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstCategory = new ArrayList<>();
        lstCategory.add(new Category("Other"));
        lstCategory.add(new Category("Starter"));
        lstCategory.add(new Category("Main Curse"));
        lstCategory.add(new Category("Desert"));


    }



    // interface to pass the checkbox data strings to setText category and
}
