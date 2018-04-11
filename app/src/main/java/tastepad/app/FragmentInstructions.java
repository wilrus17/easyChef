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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import org.w3c.dom.Text;

public class FragmentInstructions extends Fragment  {



    public static final FragmentInstructions newInstance(String someInstructions) {
        FragmentInstructions fragmentInstructions = new FragmentInstructions();
        Bundle args = new Bundle();
        args.putString("someInstructions", someInstructions);
        fragmentInstructions.setArguments(args);
        return fragmentInstructions;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.instructions_fragment, container, false);
        String mInstructions = getArguments().getString("someInstructions");
        Log.d("String5", mInstructions);
        TextView instructions = (TextView) view.findViewById(R.id.instructionsContainer);
        instructions.setText(mInstructions);
        return view;


    }









}




















