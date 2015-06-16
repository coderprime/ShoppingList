package ray.cyberpup.com.shoppinglistchallenge.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created on 5/10/15
 *
 * @author Raymond Tong
 */
public class ShopListProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildMatcher();

    // Tree of UriMatcher objects
    // Return these codes if match is found
    private static final int ITEM = 1;

    private ShopListDbHelper mShopListDbHelper;




    private static UriMatcher buildMatcher(){

        // Code to return for the root URI is -1 = NO_MATCH
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String AUTHORITY = ShopListContract.CONTENT_AUTHORITY;
        final String PATH = ShopListContract.PATH_SHOP_ITEMS;
        matcher.addURI(AUTHORITY, PATH, ITEM);
        return matcher;
    }




    @Override
    public boolean onCreate() {

        mShopListDbHelper = new ShopListDbHelper(getContext());
        if(mShopListDbHelper != null)
            return true;
        else
            return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        // Just in case, we need to add additional queries
        switch (sUriMatcher.match(uri)) {

            case ITEM:
                cursor = mShopListDbHelper.getReadableDatabase().query(
                        ShopListContract.ShoppingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Invalid uri: "+uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    // TODO:
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    // TODO:
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    // TODO:
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
