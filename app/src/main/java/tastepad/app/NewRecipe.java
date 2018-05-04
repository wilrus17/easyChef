package tastepad.app;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NewRecipe extends AppCompatActivity implements CategoryDialog.OnFragmentInteractionListener {

    EditText textIn;
    EditText quantityIn;
    Spinner unitIn;
    Button buttonAdd;
    LinearLayout container;
    Button buttonClear;
    Button buttonSave;
    EditText recipeTitle;
    MyDBHandler db;
    EditText instructions;
    EditText servings;
    TextView tv_categories;
    ImageView imgv1;
    File imgpath;
    String imgs;
    String s;
    Bitmap bm =null;
    int REQUEST_CHECK = 0;

    private ArrayList<Category> checkedCategories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imgv1 = (ImageView) findViewById(R.id.imageView);

        Button addimage = (Button) findViewById(R.id.buttonAddImage);

        addimage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage();
            }
        });



        // recipe details fields
        recipeTitle = (EditText) findViewById(R.id.title_input);
        instructions = (EditText) findViewById(R.id.instructionsIn);
        tv_categories = (TextView) findViewById(R.id.category);
        servings = (EditText) findViewById(R.id.servings);


        LinearLayout category = (LinearLayout) findViewById(R.id.categoryLayout);
        category.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        // defining each ingredient field and the layout
        textIn = (EditText) findViewById(R.id.textin);
        quantityIn = (EditText) findViewById(R.id.quantityin);
        buttonAdd = (Button) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.new_ingredient_layout);
        unitIn = (Spinner) findViewById(R.id.unitin);
        buttonClear = (Button) findViewById(R.id.clear);
        buttonSave = (Button) findViewById(R.id.save);
        db = new MyDBHandler(this);


        // clear top ingredient row
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textIn.setText("");
                quantityIn.setText("");
                unitIn.setSelection(0);

            }
        });



        // ingredient add, remove row
        buttonAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.cardview_new_ingredient, null);

                // remove row
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });
                // add row
                container.addView(addView);
            }
        });

        final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        ratingbar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //Getting the rating
                Float rating = ratingbar.getRating();
            }
        });



        // save recipe to database
        buttonSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v2) {

                // error if no recipe title input
                if (TextUtils.isEmpty(recipeTitle.getText().toString())) {
                    recipeTitle.setError("Your recipe must have a title.");
                    return;

                } else {

                    // save recipe title and instructions to recipes_table
                    Recipe recipe = new Recipe();
                    recipe.setRecipename(recipeTitle.getText().toString());
                    recipe.setInstructions(instructions.getText().toString());
                    recipe.setServings(servings.getText().toString());
                    recipe.setImagePath(s);
                    Log.i("recipeName", "createRecipe: " + recipeTitle.getText().toString());

                    Log.i("Servings", "createRecipe: " + servings.getText().toString());

                    db.createRecipe(recipe);
                    int recipeId = db.getLastRecipeId();


                    db.addRating(db.getLastRecipeId(), ratingbar.getRating());


                    // save each categories to category table and link table
                    if (checkedCategories != null) {
                        for (Category category : checkedCategories) {

                            // if category name exists in table, get ID for that category
                            // else, create new category get the last added id
                            int categoryId;
                            if (db.checkCategoryExist(category.getName()) != -1) {
                                categoryId = db.checkCategoryExist(category.getName());
                            } else {
                                Category c = new Category();
                                c.setName(category.getName());
                                db.createCategory(category);
                                categoryId = db.getLastCategoryId();
                            }

                            RecipeCategories recipeCategories = new RecipeCategories();
                            recipeCategories.setCategory_id(categoryId);
                            recipeCategories.setRecipe_id(recipeId);
                            db.createRecipesCategories(recipeCategories);
                        }
                    }


                    // identify each ingredient, quantity, unit
                    ViewGroup viewGroup = (ViewGroup) container;
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        View child = viewGroup.getChildAt(j);
                        ViewGroup group = (ViewGroup) child;

                        String ingredientName = ((EditText) group.getChildAt(0)).getText().toString();
                        String quantity = ((EditText) group.getChildAt(1)).getText().toString();
                        String ingredientQuantity;

                        ingredientQuantity = quantity;

                        String ingredientUnit = ((Spinner) group.getChildAt(2)).getSelectedItem().toString();

                        db.getReadableDatabase();

                        // if ingredientName exists in ingredient table, get ID for that ingredient name
                        // else, create new ingredient and get last id that was added
                        int ingredientId;
                        if (db.checkIngredientExist(ingredientName) != -1) {
                            ingredientId = db.checkIngredientExist(ingredientName);
                        } else {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setIngredientname(ingredientName);
                            db.createIngredient(ingredient);
                            ingredientId = db.getLastIngredientId();
                        }

                        // recipe id, ingredient id, quantity, unit to Recipe_Ingredients Link table

                        Log.d("ids", "value: " + recipeId);
                        Log.d("ids", "value: " + ingredientId);

                        RecipeIngredients recipeIngredients = new RecipeIngredients();
                        recipeIngredients.setIngredient_id(ingredientId);
                        recipeIngredients.setRecipe_id(recipeId);
                        recipeIngredients.setQuantity(ingredientQuantity);
                        recipeIngredients.setUnit(ingredientUnit);
                        db.createRecipesIngredients(recipeIngredients);

                    }

                    // recipe creation confirmation
                    Toast.makeText(getApplicationContext(), "Recipe Added!", Toast.LENGTH_SHORT).show();

                    // close the activity and go back to MyRecipes
                    finish();
                }
            }

        });
    }

    public void openDialog() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        Bundle args = new Bundle();
        args.putString("categories", tv_categories.getText().toString());
        CategoryDialog categoryDialog = new CategoryDialog();
        categoryDialog.setArguments(args);
        categoryDialog.show(getSupportFragmentManager(), "category dialog");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFragmentSetCategories(final ArrayList<Category> categories) {
        checkedCategories = categories;
        List<Category> checked = categories;
        List<String> categoryList = new ArrayList<String>();
        for (Category category : checked) {
            categoryList.add(category.getName());
        }
        StringBuilder listString = new StringBuilder();

        for (String s : categoryList)
            listString.append(s + " ");
        tv_categories.setText(listString);
    }

    public void clickImage() {
        //fire intent
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fp = getFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fp));
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        startActivityForResult(intent, REQUEST_CHECK);
    }

    private File getFile() {
        File folder = new File("sdcard/attendence");
        if (!folder.exists()) {
            folder.mkdir();
        }
        imgpath = new File(folder, File.separator +

                Calendar.getInstance().getTime() + ".jpg");


        return imgpath;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try{
            s = imgpath.toString();
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();

        }
        imgv1.setImageDrawable(Drawable.createFromPath(s));
    }



}






