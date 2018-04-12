package tastepad.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

public class FragmentIngredients extends Fragment {

    public static final FragmentIngredients newInstance(String[][] someIngredients) {
        FragmentIngredients fragmentIngredients = new FragmentIngredients();
        Bundle args = new Bundle();
        args.putSerializable("someIngredients", someIngredients);
        fragmentIngredients.setArguments(args);
        return fragmentIngredients;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ingredients_fragment, container, false);
        String[][] mIngredients = (String[][]) getArguments().getSerializable("someInstructions");
        Log.d("Ingredients", Arrays.toString(mIngredients));

        for (int i=0; i< mIngredients.length; i++){
            Log.d("ingredient name",mIngredients[i][1]);
        }


        //TextView instructions = (TextView) view.findViewById(R.id.instructionsContainer);
        //instructions.setText(mInstructions);
        return view;
    }
}




