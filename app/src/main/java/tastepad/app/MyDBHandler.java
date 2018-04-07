package tastepad.app;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 26/03/2018.
 */

public class MyDBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RecipesDB.db";
    private static final int DATABASE_VERISON = 1;

    // recipes table & columns
    public static final String TABLE_RECIPES = "Recipes";
    public static final String RECIPE_ID = "_recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_INSTRUCTIONS = "instructions";

    // ingredients table & columns
    public static final String TABLE_INGREDIENTS = "Ingredients";
    public static final String INGREDIENT_ID= "_ingredient_id";
    public static final String INGREDIENT_NAME = "ingredient_name";

    // recipe-ingredients table & columns
    public static final String TABLE_RECIPE_INGREDIENTS = "RecipeIngredients";
    public static final String RI_RECIPE_ID = "_recipe_id01";
    public static final String RI_INGREDIENT_ID = "_ingredient_id02";
    public static final String RI_INGREDIENT_QUANTITY ="ingredient_quantity";
    public static final String RI_INGREDIENT_UNIT = "ingredient_unit";

    // create table statements
    final String CREATE_TABLE_RECIPES = "CREATE TABLE " +
            TABLE_RECIPES + "(" +
            RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RECIPE_NAME + " TEXT, " +
            RECIPE_INSTRUCTIONS + " TEXT " +
            ")";

    final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE " +
            TABLE_INGREDIENTS + "(" +
            INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            INGREDIENT_NAME + " TEXT " +
            ")";

    /*
    final String CREATE_TABLE_RECIPE_INGREDIENTS = "CREATE TABLE " +
           TABLE_RECIPE_INGREDIENTS + "(" +
            RI_RECIPE_ID + " INTEGER NOT NULL FOREIGN KEY REFERENCES TABLE_RECIPES (RECIPE_ID)," +
            RI_INGREDIENT_ID + " INTEGER NOT NULL FOREIGN KEY REFERENCES TABLE_INGREDIENTS (INGREDIENTS_ID)," +
            "PRIMARY KEY (RI_RECIPE_ID, RI_INGREDIENT_ID)" +
            RI_INGREDIENT_QUANTITY + " REAL, " +
            RI_INGREDIENT_UNIT + " TEXT " +
            ")";

*/


    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_INGREDIENTS);
       // db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);

     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);

        // create new tables
        onCreate(db);
    }


    // create new recipe
    public void createRecipe(Recipe Recipe) {
        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, Recipe.getRecipename());
        values.put(RECIPE_INSTRUCTIONS, Recipe.getInstructions());
        SQLiteDatabase db = getWritableDatabase();

        // new row to table
        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    // delete a recipe

    public void deleteRecipe(String recipeId) {
        SQLiteDatabase db = getWritableDatabase();


    }

    public List<Recipe> getRecipes() {

        // sql query to fetch data
        String fetchQuery = "SELECT * from " + TABLE_RECIPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(fetchQuery, null);
        List<Recipe> recipeList = new ArrayList<>();

        // transverse through all rows and add to list
        while (cursor.moveToNext()) {
            Recipe recipe = new Recipe();
            String fetchedRecipeId =
                    cursor.getString(cursor.getColumnIndex(RECIPE_ID));
            String fetchedRecipe =
                    cursor.getString(cursor.getColumnIndex(RECIPE_NAME));
            String fetchedRecipeInstructions =
                    cursor.getString(cursor.getColumnIndex(RECIPE_INSTRUCTIONS));

            recipe.setRecipename(fetchedRecipeId);
            recipe.setRecipename(fetchedRecipe);
            recipe.setInstructions(fetchedRecipeInstructions);

            // add record to list

            recipeList.add(recipe);
        }
        cursor.close();
        db.close();

        // return recipe list
        return recipeList;

    }


}

