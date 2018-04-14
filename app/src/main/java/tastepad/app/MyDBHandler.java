package tastepad.app;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Will on 26/03/2018.
 */

public class MyDBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RecipesDB.db";
    private static final int DATABASE_VERISON = 2;
    public int id;
    Context mContext;
    MyDBHandler db;

    // recipes table & columns
    public static final String TABLE_RECIPES = "Recipes";
    public static final String RECIPE_ID = "_recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_INSTRUCTIONS = "instructions";

    // ingredients table & columns
    public static final String TABLE_INGREDIENTS = "Ingredients";
    public static final String INGREDIENT_ID = "_ingredient_id";
    public static final String INGREDIENT_NAME = "ingredient_name";

    // recipe-ingredients table & columns
    public static final String TABLE_RECIPE_INGREDIENTS = "RecipeIngredients";

    public static final String INGREDIENT_QUANTITY = "ingredient_quantity";
    public static final String INGREDIENT_UNIT = "ingredient_unit";

    // create table statements
    final String CREATE_TABLE_RECIPES = "CREATE TABLE IF NOT EXISTS " +
            TABLE_RECIPES + "(" +
            RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RECIPE_NAME + " TEXT, " +
            RECIPE_INSTRUCTIONS + " TEXT " +
            ")";

    final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INGREDIENTS + "(" +
            INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            INGREDIENT_NAME + " TEXT NOT NULL UNIQUE " +
            ")";

    final String CREATE_TABLE_RECIPE_INGREDIENTS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_RECIPE_INGREDIENTS + "(" +
            RECIPE_ID + " INTEGER NOT NULL, " +
            INGREDIENT_ID + " INTEGER NOT NULL, " +
            INGREDIENT_QUANTITY + " REAL, " +
            INGREDIENT_UNIT + " TEXT, " +
            "PRIMARY KEY (" + RECIPE_ID + "," + INGREDIENT_ID + "), " +
            "FOREIGN KEY (" + RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + RECIPE_ID + "), " +
            "FOREIGN KEY (" + INGREDIENT_ID + ") REFERENCES " + TABLE_INGREDIENTS + "(" + INGREDIENT_ID + ")" +
            ")";


    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_INGREDIENTS);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);
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
    public void createRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, recipe.getRecipename());
        values.put(RECIPE_INSTRUCTIONS, recipe.getInstructions());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }


    // delete a recipe
    public void deleteRecipe(int recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_RECIPES, RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
        db.delete(TABLE_RECIPE_INGREDIENTS, RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
    }


    // gets list of Recipes
    public List<Recipe> getRecipes() {

        // sql query to fetch data
        String fetchQuery = "SELECT * from " + TABLE_RECIPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(fetchQuery, null);
        List<Recipe> recipeList = new ArrayList<>();

        // transverse through all rows and add to list
        while (cursor.moveToNext()) {
            Recipe recipe = new Recipe();

            int fetchedRecipeId =
                    cursor.getInt(cursor.getColumnIndex(RECIPE_ID));

            String fetchedRecipe =
                    cursor.getString(cursor.getColumnIndex(RECIPE_NAME));

            String fetchedRecipeInstructions =
                    cursor.getString(cursor.getColumnIndex(RECIPE_INSTRUCTIONS));

            recipe.set_id(fetchedRecipeId);
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

    // add ingredient
    public void createIngredient(Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put(INGREDIENT_NAME, ingredient.getIngredientname());
        SQLiteDatabase db = getWritableDatabase();
        db.insertWithOnConflict(TABLE_INGREDIENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // create recipe-ingredients relation with quantity and unit
    public void createRecipesIngredients(RecipeIngredients recipeIngredient) {
        ContentValues values = new ContentValues();
        values.put(RECIPE_ID, recipeIngredient.getRecipe_id());
        values.put(INGREDIENT_ID, recipeIngredient.getIngredient_id());
        values.put(INGREDIENT_QUANTITY, recipeIngredient.getQuantity());
        values.put(INGREDIENT_UNIT, recipeIngredient.getUnit());
        SQLiteDatabase db = getWritableDatabase();
        // new row to table
        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
        db.close();
    }

    // get ingredients for given recipe id
    public String[][] getRecipeIngredients(int id) {
        Log.d("int2", "value: " + id);

        String GET_ALL_INGREDIENTS = "SELECT " + INGREDIENT_NAME + "," + INGREDIENT_QUANTITY + "," +
                INGREDIENT_UNIT +
                " FROM " + TABLE_INGREDIENTS +
                " INNER JOIN " + TABLE_RECIPE_INGREDIENTS +
                " ON " + TABLE_RECIPE_INGREDIENTS + "." + INGREDIENT_ID + "=" + TABLE_INGREDIENTS + "." + INGREDIENT_ID +
                " WHERE " + TABLE_RECIPE_INGREDIENTS + "." + RECIPE_ID + "=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(GET_ALL_INGREDIENTS, null);
        int x = c.getCount();
        String[][] ingredientsArray = new String[x][3];

        // put each row into 2d array
        int i = 0;
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(INGREDIENT_NAME));
            String quantity = c.getString(c.getColumnIndex(INGREDIENT_QUANTITY));
            String unit = c.getString(c.getColumnIndex(INGREDIENT_UNIT));
            ingredientsArray[i][0] = name;
            ingredientsArray[i][1] = quantity;
            ingredientsArray[i][2] = unit;
            Log.i("ingredients", Arrays.deepToString(ingredientsArray));
            i++;
        }
        Log.i("ingredientsRe", Arrays.deepToString(ingredientsArray));
        return ingredientsArray;
    }

    // recipeId that was just added
    public int getLastRecipeId() {
        int lastRecipeId = 0;
        String query = "SELECT " + RECIPE_ID + " FROM " + TABLE_RECIPES + " ORDER BY " +
                RECIPE_ID + " DESC limit 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastRecipeId = c.getInt(0);
            return lastRecipeId;
        }
        return lastRecipeId;
    }

    // ingredientId that was just added
    public int getLastIngredientId() {
        int lastIngredientId = 0;
        String query = "SELECT " + INGREDIENT_ID + " FROM " + TABLE_INGREDIENTS + " ORDER BY " +
                INGREDIENT_ID + " DESC limit 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastIngredientId = c.getInt(0);
            return lastIngredientId;
        }
        return lastIngredientId;
    }


}




