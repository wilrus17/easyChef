package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class FragmentInstructions extends Fragment  {

    View view;
    TextView instructions;


    public static FragmentInstructions newInstance(String someInstructions) {
        FragmentInstructions fragmentInstructions = new FragmentInstructions();
        Bundle args = new Bundle();
        args.putString("someInstructions", someInstructions);
        fragmentInstructions.setArguments(args);
        return fragmentInstructions;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            String mInstructions = getArguments().getString("someInstructions");
            Log.d("String5", mInstructions);
            view = inflater.inflate(R.layout.instructions_fragment,container,false);
            TextView instructions = (TextView) view.findViewById(R.id.instructionsContainer);
            instructions.setText(mInstructions);

        } return view;




       // String mInstructions = this.getArguments().getString("Instructions");
      //  textView.setText(mInstructions);
    }






}







