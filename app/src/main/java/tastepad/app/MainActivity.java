package tastepad.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView fridgeCard, recipesCard, shoppingCard, exploreCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //defining Cards
        fridgeCard = (CardView) findViewById(R.id.fridge_card);
        recipesCard = (CardView) findViewById(R.id.recipes_card);
        shoppingCard = (CardView) findViewById(R.id.shopping_card);
        exploreCard = (CardView) findViewById(R.id.explore_card);
        //Add Click Listener to cards
        fridgeCard.setOnClickListener(this);
        recipesCard.setOnClickListener(this);
        shoppingCard.setOnClickListener(this);
        exploreCard.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
        Intent i ;
        switch (v.getId()) {
            case R.id.fridge_card : i = new Intent(this,MyFridge.class);startActivity(i); break;
            case R.id.recipes_card : i = new Intent(this,MyRecipes.class);startActivity(i); break;
            case R.id.shopping_card : i = new Intent(this,ShoppingList.class);startActivity(i); break;
            case R.id.explore_card : i = new Intent(this,activity_recipe_screen.class);startActivity(i); break;
            default:break ;



        }

    }
}
