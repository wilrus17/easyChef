package tastepad.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditRecipe extends AppCompatActivity implements CategoryDialog.OnFragmentInteractionListener {

    EditText editIngredient1;
    EditText editQuantity1;
    Spinner editUnit1;
    Button buttonAdd;
    LinearLayout container;
    Button buttonClear;
    Button buttonSave;
    EditText editTitle;
    MyDBHandler db;
    EditText editInstructions;
    EditText editServings;
    TextView tv_categories;
    RatingBar editRating;
    ImageView imgv1;
    File imgpath;
    String imgs;
    String s;
    Bitmap bm = null;
    int REQUEST_CHECK = 0;

    private ArrayList<Category> checkedCategories;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_recipe);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // get recipe
        Bundle extras = new Bundle();
        extras = getIntent().getExtras();
        String[][] recipeIngredients = (String[][]) extras.getSerializable("Ingredients");
        String recipeInstructions = (String) extras.getString("Instructions");
        String recipeTitle = (String) extras.getString("Title");
        String recipeServings = (String) extras.getString("Servings");
        float recipeRating = (float) extras.getFloat("Rating");
        final int recipeId = (Integer) extras.getInt("RecipeId");

        // defining recipe title and instruction views
        editTitle = (EditText) findViewById(R.id.edit_title);
        editInstructions = (EditText) findViewById(R.id.edit_instructions);

        // defining each ingredient field and the layout

        editTitle = (EditText) findViewById(R.id.edit_title);
        editServings = (EditText) findViewById(R.id.servings);
        imgv1 = (ImageView) findViewById(R.id.imageView);
        editRating = (RatingBar) findViewById(R.id.ratingBar);
        editIngredient1 = (EditText) findViewById(R.id.edit_ingredient);
        editQuantity1 = (EditText) findViewById(R.id.edit_quantity);
        editUnit1 = (Spinner) findViewById(R.id.edit_unit);
        buttonAdd = (Button) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.new_ingredient_layout);
        buttonClear = (Button) findViewById(R.id.clear);
        buttonSave = (Button) findViewById(R.id.save);
        tv_categories = (TextView) findViewById(R.id.category);
        db = new MyDBHandler(this);


        // set initial recipe values
        Log.d("recipeInfo: ", recipeTitle);
        editTitle.setText(recipeTitle);
        editInstructions.setText(recipeInstructions);
        editServings.setText(recipeServings);
        editRating.setRating(db.getRating(recipeId));
        imgv1.setImageDrawable(Drawable.createFromPath(db.getImagePath(recipeId)));


        String formatted = TextUtils.join(" ", db.getCategories(recipeId));
        Log.i("CATEGORIES", "GOTCATEGORIES: " + formatted);
        if(!formatted.trim().isEmpty()) {
            tv_categories.setText(formatted);
        }
        Button editimage = (Button) findViewById(R.id.buttonEditImage);

        // add new image
        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage();
            }
        });

        // on click category button
        LinearLayout category = (LinearLayout) findViewById(R.id.categoryLayout);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        ViewGroup group = (ViewGroup) container;

        // add pre-edit ingredients in rows
        for (int i = 1; i < recipeIngredients.length; i++) {
            String ingredient = recipeIngredients[i][0];
            String quantity = recipeIngredients[i][1];
            String unit = recipeIngredients[i][2];

            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.cardview_new_ingredient, group, false);

            // remove row button
            Button buttonRemove = (Button) addView.findViewById(R.id.remove);
            buttonRemove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((LinearLayout) addView.getParent()).removeView(addView);
                }
            });

            EditText editIngredient = (EditText) addView.findViewById(R.id.new_ingredient_text);
            editIngredient.setText(ingredient);

            EditText editQuantity = (EditText) addView.findViewById(R.id.new_ingredient_quantity);
            editQuantity.setText(quantity);

            Spinner editUnit = (Spinner) addView.findViewById(R.id.new_ingredient_unit);
            editUnit.setSelection(getIndex(editUnit, unit));

            container.addView(addView);

        }

        // set top row ingredient
        editIngredient1.setText(recipeIngredients[0][0]);
        editQuantity1.setText(recipeIngredients[0][1]);
        editUnit1.setSelection(getIndex(editUnit1, recipeIngredients[0][2]));

        // clear top ingredient row
        buttonClear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                editIngredient1.setText("");
                editQuantity1.setText("");
                editUnit1.setSelection(0);
            }
        });

        // ingredient add, remove row
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.cardview_new_ingredient, null);

                // remove row
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });
                // add row
                container.addView(addView);
            }
        });

        // save recipe to database
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                db.deleteRecipe(recipeId);

                // error if no recipe title input
                if (TextUtils.isEmpty(editTitle.getText().toString())) {
                    editTitle.setError("Your recipe must have a title.");
                    return;

                } else {

                    // save recipe title and instructions to recipes_table
                    Recipe recipe = new Recipe();
                    recipe.setRecipename(editTitle.getText().toString());
                    recipe.setInstructions(editInstructions.getText().toString());
                    recipe.setServings(editServings.getText().toString());
                    recipe.setImagePath(s);
                    db.createRecipe(recipe);

                    db.createRecipe(recipe);
                    int recipeId = db.getLastRecipeId();


                    db.addRating(db.getLastRecipeId(), editRating.getRating());

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
                        Log.d("created id", "value: " + ingredientId);
                        Log.d("created", "value: " + ingredientId);
                        Log.d("ids", "value: " + ingredientId);
                        Log.d("ids", "value: " + ingredientId);
                    }

                    // recipe edit confirmation
                    Toast.makeText(getApplicationContext(), "Recipe Edited!", Toast.LENGTH_SHORT).show();

                    // back to MyRecipes activity
                    Intent i = new Intent(v2.getContext(), MyRecipes.class);
                    startActivity(i);

                }

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

        try {
            s = imgpath.toString();
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();

        }
        imgv1.setImageDrawable(Drawable.createFromPath(s));
    }


}