package ray.cyberpup.com.shoppinglistchallenge.tests;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import ray.cyberpup.com.shoppinglistchallenge.data.ShopListDbHelper;

/**
 * Test Helper class to test CRUD operations
 * Created on 5/18/15
 *
 * @author Raymond Tong
 */
public class TestDbHelper extends AndroidTestCase {


    public void testCreateDb() {

        // Make sure old test db is tossed
        mContext.deleteDatabase(ShopListDbHelper.DATABASE_NAME);

        // Create a database
        SQLiteDatabase db = new ShopListDbHelper(mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());
        db.close();

    }




}
