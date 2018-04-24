package tastepad.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.InterfaceAddress;
import java.util.ArrayList;

public class CategoryDialog extends AppCompatDialogFragment {

    View v;
    private RecyclerView myrecyclerview;
    private ArrayList<Category> lstCategory;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ITEM = "item";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        loadData();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_category, null);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.category_recyclerview);
        final RecyclerCategoryAdapter recyclerAdapter = new RecyclerCategoryAdapter(getContext(), lstCategory, inflater);
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

                        EditText tv_category = (EditText) v.findViewById(R.id.editText_category);
                        String new_category = tv_category.getText().toString();
                        CheckBox checkBoxNewCategory = (CheckBox) v.findViewById(R.id.checkBoxNewCategory);

                        Log.i("newcategory", "typed category =:" + new_category);

                        // list of checked categories
                        ArrayList<Category> lstCategoryChecked = new ArrayList<>();
                        CheckBox checkBoxCategory = (CheckBox) v.findViewById(R.id.checkBox);
                        if (checkBoxNewCategory.isChecked()) {
                            Category category = new Category(new_category);
                            category.setChecked(true);
                            lstCategory.add(category);
                            recyclerAdapter.notifyDataSetChanged();
                        }

                        for (Category category : lstCategory) {
                            if (category.getChecked()) {
                                lstCategoryChecked.add(category);
                            }

                        }

                        Log.i("categories", "checked: " + lstCategoryChecked);
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetCategories(lstCategoryChecked);

                        saveData();
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
        lstCategory.add(new Category("Main Course"));
        lstCategory.add(new Category("Desert"));
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preference", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lstCategory);
        editor.putString("category list", json);
        editor.apply();


    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preference", getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("category list", null);
        Type type = new TypeToken<ArrayList<Category>>() {
        }.getType();
        lstCategory = gson.fromJson(json, type);
        if (lstCategory == null) {
            lstCategory = new ArrayList<>();
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentSetCategories(ArrayList<Category> categories);
    }


    // interface to pass the checkbox data strings to setText category and
}
