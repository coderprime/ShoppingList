package ray.cyberpup.com.shoppinglistchallenge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ray.cyberpup.com.shoppinglistchallenge.data.ShopListContract.ShoppingEntry;

/**
 *
 * Database for Shopping List
 * Created on 5/10/15
 *
 * @author Raymond Tong
 */
public class ShopListDbHelper extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "shoplist.db";

    public ShopListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold shopping list items
        final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " +
                                        ShoppingEntry.TABLE_NAME + " (" +
                                        ShoppingEntry._ID + " INTEGER PRIMARY KEY,"+
                                        ShoppingEntry.COLUMN_NAME_CATEGORY + " TEXT UNIQUE NOT NULL, "+
                                        ShoppingEntry.COLUMN_NAME_CREATED_AT + " TEXT NOT NULL, " +
                                        ShoppingEntry.COLUMN_NAME_ID_AT_SERVER + " INTEGER NOT NULL, "+
                                        ShoppingEntry.COLUMN_NAME_ITEM + " TEXT NOT NULL, " +
                                        ShoppingEntry.COLUMN_NAME_UPDATED_AT + " TEXT NOT NULL, " +
                                        ShoppingEntry.COLUMN_NAME_USER_ID + " INTEGER NOT NULL," +
                                        "UNIQUE (" + ShoppingEntry.COLUMN_NAME_CATEGORY +
                                        ") ON CONFLICT REPLACE );";

        db.execSQL(SQL_CREATE_ITEMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drops old table and start over with new table
        db.execSQL("DROP TABLE IF EXISTS " + ShoppingEntry.TABLE_NAME);
        onCreate(db);

    }
}
