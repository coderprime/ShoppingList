package ray.cyberpup.com.shoppinglistchallenge.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 5/9/15
 *
 * @author Raymond Tong
 */
public final class ShopListContract {



    // Separate authority and path, in case more tables are added in the future
    public static final String CONTENT_AUTHORITY = "ray.cyberpup.com.shoppinglistchallenge";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SHOP_ITEMS = "shopping";

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    // This class should not be instantiated
    private ShopListContract(){}

    // Convert Date class to string
    public static String getStringFromDate(Date date){

        // Convert Unix timestamp from seconds to milliseconds
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(date);

    }

    // Convert string date to Unix time
    public static Date getDateFromString(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try{
            return simpleDateFormat.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Table that contains all our data
    // Inherits primary key field _ID
    public static final class ShoppingEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                                                .buildUpon()
                                                .appendPath(PATH_SHOP_ITEMS).build();

        // Standard format for "vendor-specific" MIME type
        // Multiple rows
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" +
                                                    CONTENT_AUTHORITY + "/" +
                                                    PATH_SHOP_ITEMS;

        // Single row
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"+
                                                        CONTENT_AUTHORITY + "/" +
                                                        PATH_SHOP_ITEMS;



        public static final String TABLE_NAME = "list";
        public static final String COLUMN_NAME_ITEM = "item";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_ID_AT_SERVER = "id_server";
        public static final String COLUMN_NAME_CREATED_AT = "created_at";
        public static final String COLUMN_NAME_UPDATED_AT = "updated_at";
        public static final String COLUMN_NAME_USER_ID = "user_id";



        // Return specific URIs according to different type of queries

        // Return a single URI based on id
        public static Uri buildShopListUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // TODO: add more queries later

    }


}
