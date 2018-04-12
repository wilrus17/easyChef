package tastepad.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentIngredients extends Fragment {

    View v;
    private RecyclerView myrecyclerview;
    private List<IngredientCard> lstIngredient;

    public static final FragmentIngredients newInstance(String[][] someIngredients) {
        FragmentIngredients fragmentIngredients = new FragmentIngredients();
        Bundle args = new Bundle();
        args.putSerializable("someIngredients", someIngredients);
        fragmentIngredients.setArguments(args);
        return fragmentIngredients;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        lstIngredient = new ArrayList<>();
        View view = inflater.inflate(R.layout.ingredients_fragment, container, false);

        String[][] mIngredients = (String[][]) getArguments().getSerializable("someIngredients");
        Log.d("Ingredients", Arrays.toString(mIngredients));

        // get each ingredient, quantity, unit
        for (int i=0; i< mIngredients.length; i++){
            String ingredient = mIngredients[i][0];
            String quantity = mIngredients[i][1];
            String unit = mIngredients[i][2];

            // create recycler view item
            lstIngredient.add(new IngredientCard(ingredient ,quantity + " " + unit));
        }

        myrecyclerview = (RecyclerView) view.findViewById(R.id.ingredientsList);
        RecyclerIngredientAdapter recyclerAdapter = new RecyclerIngredientAdapter(getContext(),lstIngredient);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        // dividers between ingredients
        myrecyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);

        return view;
    }

}




