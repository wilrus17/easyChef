package tastepad.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Will on 26/03/2018.
 */

public class MyDBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Recipes.db";
    public static final String TABLE_NAME = "Recipes_table";
    public static final String COL_1 = "Recipes_name";
    public static final String COL_2 = "Ingredients";
    public static final String COL_3 = "Instructions";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
     }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
