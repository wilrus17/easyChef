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
    public static final String DATABASE_NAME = "Recipes.db";
    public static final String TABLE_RECIPE = "recipe_table";
    public static final String COLUMN_ID = "recipe_id";
    public static final String COLUMN_RECIPENAME = "recipe_name";


    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_RECIPE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPENAME + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        onCreate(db);
    }

    //Add new row to database
    public void addRecipe(String recipe) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPENAME, recipe);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_RECIPE, null, values);
        db.close();
    }

    public void deleteRecipe(String recipeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM" + TABLE_RECIPE + " WHERE " + COLUMN_ID + "=\"" + recipeId + "\";");

    }

    public List<Recipe> getRecipes() {

        String fetchQuery = "SELECT * from " + TABLE_RECIPE; // sql query to fetch data
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(fetchQuery, null);
        List<Recipe> recipes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Recipe recipe = new Recipe(cursor.toString());
            String fetchedRecipe =
                    cursor.getString(cursor.getColumnIndex(COLUMN_RECIPENAME));
            recipe.setRecipename(fetchedRecipe); // This should be a method in your Recipe.class to set the value for the object

            recipes.add(recipe);
        }
        cursor.close();
        db.close();

        return recipes;

    }
}
