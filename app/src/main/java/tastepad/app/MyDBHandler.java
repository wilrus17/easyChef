package tastepad.app;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import java.sql.Array;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Will on 26/03/2018.
 */

public class MyDBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RecipesDB.db";
    private static final int DATABASE_VERISON = 7;
    public int id;
    Context mContext;
    MyDBHandler db;

    // recipes table & columns
    public static final String TABLE_RECIPES = "Recipes";
    public static final String RECIPE_ID = "_recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_INSTRUCTIONS = "instructions";
    public static final String RECIPE_RATING = "rating";
    public static final String RECIPE_SERVINGS = "servings";


    // ingredients table & columns
    public static final String TABLE_INGREDIENTS = "Ingredients";
    public static final String INGREDIENT_ID = "_ingredient_id";
    public static final String INGREDIENT_NAME = "ingredient_name";

    // recipe-ingredients table & columns
    public static final String TABLE_RECIPE_INGREDIENTS = "RecipeIngredients";
    public static final String INGREDIENT_QUANTITY = "ingredient_quantity";
    public static final String INGREDIENT_UNIT = "ingredient_unit";


    // category table and columns
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String CATEGORY_ID = "_category_id";
    public static final String CATEGORY_NAME = "category_name";

    // recipe-category table

    public static final String TABLE_RECIPE_CATEGORIES = "RecipeCategories";


    // create table statements
    final String CREATE_TABLE_RECIPES = "CREATE TABLE IF NOT EXISTS " +
            TABLE_RECIPES + "(" +
            RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RECIPE_NAME + " TEXT, " +
            RECIPE_INSTRUCTIONS + " TEXT, " +
            RECIPE_RATING + " FLOAT, " +
            RECIPE_SERVINGS + " TEXT, " +
            ")";

    final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INGREDIENTS + "(" +
            INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            INGREDIENT_NAME + " TEXT NOT NULL " +
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

    final String CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS " +
            TABLE_CATEGORIES + "(" +
            CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CATEGORY_NAME + " TEXT NOT NULL " +
            ")";

    final String CREATE_TABLE_RECIPE_CATEGORIES = "CREATE TABLE IF NOT EXISTS " +
            TABLE_RECIPE_CATEGORIES + "(" +
            RECIPE_ID + " INTEGER NOT NULL, " +
            CATEGORY_ID + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + RECIPE_ID + "," + CATEGORY_ID + "), " +
            "FOREIGN KEY (" + RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + RECIPE_ID + "), " +
            "FOREIGN KEY (" + CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + CATEGORY_ID + ")" +
            ")";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERISON);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_INGREDIENTS);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_RECIPE_CATEGORIES);

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

    public void createCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, category.getName());

        Log.i("Categories", "created category in table: " + category.getName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public void createRecipesCategories(RecipeCategories recipeCategories) {
        ContentValues values = new ContentValues();
        values.put(RECIPE_ID, recipeCategories.getRecipe_id());
        values.put(CATEGORY_ID, recipeCategories.getCategory_id());

        Log.i("Categories", "created recipesCategories in link table: " + recipeCategories.getCategory_id());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_RECIPE_CATEGORIES, null, values);
        db.close();
    }


    // create new recipe
    public void createRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, recipe.getRecipename());
        values.put(RECIPE_INSTRUCTIONS, recipe.getInstructions());
        values.put(RECIPE_SERVINGS, recipe.getServings());
        Log.i("Servings", "dbCreate: " + recipe.getServings());

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

            String fetchedRecipeServings =
                    cursor.getString(cursor.getColumnIndex(RECIPE_SERVINGS));

            recipe.set_id(fetchedRecipeId);
            recipe.setRecipename(fetchedRecipe);
            recipe.setInstructions(fetchedRecipeInstructions);
            recipe.setServings(fetchedRecipeServings);

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
        db.insert(TABLE_INGREDIENTS, null, values);
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

    // get category Id if name exists
    public int checkCategoryExist(String categoryName) {

        String getCategoryId = "SELECT " + CATEGORY_ID +
                " FROM " + TABLE_CATEGORIES +
                " WHERE " + CATEGORY_NAME + " IN " + "('" + categoryName + "')";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(getCategoryId, null);
        if (c != null && c.moveToFirst()) {
            int categoryId = c.getInt(0);
            return categoryId;
        }
        return -1;
    }


    // get ingredient Id if name exists
    public int checkIngredientExist(String ingredientName) {

        String getIngredientId = "SELECT " + INGREDIENT_ID +
                " FROM " + TABLE_INGREDIENTS +
                " WHERE " + INGREDIENT_NAME + " IN " + "('" + ingredientName + "')";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(getIngredientId, null);
        if (c != null && c.moveToFirst()) {
            int ingredientId = c.getInt(0);
            return ingredientId;
        }
        return -1;
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
        c.close();
        Log.i("ingredientsRe", Arrays.deepToString(ingredientsArray));
        return ingredientsArray;

    }

    // ingredientId that was just added
    public int getLastCategoryId() {
        int lastCategoryId = 0;
        String query = "SELECT " + CATEGORY_ID + " FROM " + TABLE_CATEGORIES + " ORDER BY " +
                CATEGORY_ID + " DESC limit 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            lastCategoryId = c.getInt(0);
            return lastCategoryId;
        }
        return lastCategoryId;
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
        c.close();
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

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<String>();

        String selectQuery = "SELECT " + CATEGORY_NAME + " FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return categories;
    }

    public ArrayList<Integer> getCategoryRecipes(String category){

        String GET_CATEGORY_RECIPES =
                "SELECT " + RECIPE_ID +
                        " FROM " + TABLE_RECIPE_CATEGORIES +
                        " INNER JOIN " + TABLE_CATEGORIES +
                        " ON " + TABLE_RECIPE_CATEGORIES + "." + CATEGORY_ID + "=" + TABLE_CATEGORIES + "." + CATEGORY_ID +
                        " WHERE " + TABLE_CATEGORIES + "." + CATEGORY_NAME + "=" + "('" + category + "')" +
                        " GROUP BY " + RECIPE_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(GET_CATEGORY_RECIPES, null);
        int x = c.getCount();
        ArrayList<Integer> list = new ArrayList<>();

        int i = 0;
        while (c.moveToNext()){
            int recipeId = c.getInt(c.getColumnIndex(RECIPE_ID));
            list.add(recipeId);
            i++;

            Log.i("RecipesInCategory", "Recipes in seleted category: " + list.toString());
        }
        c.close();
        return  list;
    }


    // return list of recipes that use inputList ingredients
    public ArrayList<Integer> getFilteredRecipes(List<String> inputList, String selectedCategory) {

        // List of Recipe Id's for selected category
        ArrayList<Integer> categoryRecipes = new ArrayList<>();
        categoryRecipes = getCategoryRecipes(selectedCategory);

        Log.i("selected@db", "selected category: " + selectedCategory);
        String formatted = TextUtils.join("', '", inputList);
        int listSize = inputList.size();

        String GET_FILTERED_RECIPES =
                "SELECT " + RECIPE_ID +
                        " FROM " + TABLE_RECIPE_INGREDIENTS +
                        " INNER JOIN " + TABLE_INGREDIENTS +
                        " ON " + TABLE_RECIPE_INGREDIENTS + "." + INGREDIENT_ID + "=" + TABLE_INGREDIENTS + "." + INGREDIENT_ID +
                        " WHERE " + TABLE_INGREDIENTS + "." + INGREDIENT_NAME + " IN " + "('" + formatted + "')" +
                        " GROUP BY " + RECIPE_ID +
                        " HAVING COUNT(DISTINCT " + TABLE_INGREDIENTS + "." + INGREDIENT_NAME + ")" + ">=" + listSize;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(GET_FILTERED_RECIPES, null);
        int x = c.getCount();
        ArrayList<Integer> list = new ArrayList<Integer>();

        // put each row into list

        while (c.moveToNext()) {
            int recipeId = c.getInt(c.getColumnIndex(RECIPE_ID));
            if(categoryRecipes.contains(recipeId) || selectedCategory == "All"){
                list.add(recipeId);
            }
        }
        c.close();
        return list;
    }

    // update table with rating input
    public void addRating(int id, float rating) {
        System.out.println(TABLE_RECIPES);
        String SET_RATING =
                "UPDATE " + TABLE_RECIPES +
                        " SET " + RECIPE_RATING + " = " + rating +
                        " WHERE " + RECIPE_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SET_RATING);
        db.close();
    }

    public float getRating(int id) {
        String GET_RATING =
                "SELECT " + RECIPE_RATING +
                        " FROM " + TABLE_RECIPES +
                        " WHERE " + RECIPE_ID + "=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(GET_RATING, null);
        Log.i("CHECK", "getRating: " + db.rawQuery(GET_RATING, null));
        c.moveToFirst();
        float rating = c.getFloat(0);
        Log.i("ratingGet", "gotRating: " + rating);
        return rating;
    }
}




