package tastepad.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentIngredients extends Fragment implements NumberPicker.OnValueChangeListener {

    View v;
    private RecyclerView myrecyclerview;
    private List<IngredientCard> lstIngredient;


    public static final FragmentIngredients newInstance(String[][] someIngredients, String servings) {
        FragmentIngredients fragmentIngredients = new FragmentIngredients();
        Bundle args = new Bundle();
        args.putString("servingSize", servings);
        args.putSerializable("someIngredients", someIngredients);
        fragmentIngredients.setArguments(args);
        return fragmentIngredients;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        lstIngredient = new ArrayList<>();
        View view = inflater.inflate(R.layout.ingredients_fragment, container, false);
        final Button setServings = (Button) view.findViewById(R.id.servingSelect);
        ViewGroup layout = (ViewGroup) setServings.getParent();

        String[][] mIngredients = (String[][]) getArguments().getSerializable("someIngredients");
        final String mServings = (String) getArguments().get("servingSize");
        Log.d("Ingredients", Arrays.toString(mIngredients));
        Log.d("Servings", "mServings got:" + mServings);

        if (mServings != null && !mServings.isEmpty()) {
            String servingSize = getString(R.string.servings, mServings);
            setServings.setText(servingSize);
        } else setServings.setVisibility(View.GONE);


        // get each ingredient, quantity, unit
        for (int i = 0; i < mIngredients.length; i++) {
            String ingredient = mIngredients[i][0];
            String quantity = mIngredients[i][1];
            String unit = mIngredients[i][2];

            float quantity1;
            if(quantity.equals("")){
                quantity1 = 0;
            } else quantity1 = Float.parseFloat(quantity);

            // create recycler view item
            lstIngredient.add(new IngredientCard(ingredient, quantity1, unit));
        }

        setServings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(setServings, mServings);
            }
        });

        Bundle bundle = new Bundle();


        myrecyclerview = (RecyclerView) view.findViewById(R.id.ingredientsList);
        RecyclerIngredientAdapter recyclerAdapter = new RecyclerIngredientAdapter(getContext(), lstIngredient);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        // dividers between ingredients
        myrecyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);

        return view;
    }
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);

    }

    public void show(final Button button, final String originalServings) {

        final Dialog d = new Dialog(getActivity(), R.style.LightDialogTheme);

        final float originalServingSize = Float.parseFloat(originalServings);
        Log.i("servingSize", "original serving size: " + originalServingSize);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_servings);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String servingSize = getString(R.string.servings, String.valueOf(np.getValue()));
                float servings = np.getValue();
                button.setText(servingSize); //set the value to textview
                changeQuantity(originalServingSize, servings);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }

    public void changeQuantity(float original, float altered) {
        ArrayList<IngredientCard> alteredQuantitylist = new ArrayList<>();
        for(IngredientCard card : lstIngredient){
        float quantity = card.getQuantity();
        String unit = card.getUnit();
        String name = card.getName();
        float adjustedQuantity = quantity*(altered/original);
        String adjustedString = String.format("%.3f", adjustedQuantity);
        float finalQuantity = Float.parseFloat(adjustedString);
        IngredientCard ingredientCard = new IngredientCard();
        ingredientCard.setQuantity(finalQuantity);
        ingredientCard.setName(name);
        ingredientCard.setUnit(unit);
        alteredQuantitylist.add(ingredientCard);

        }
        myrecyclerview.setAdapter(new RecyclerIngredientAdapter(getContext(), alteredQuantitylist));
        myrecyclerview.invalidate();




    }
}







