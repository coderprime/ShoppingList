package ray.cyberpup.com.shoppinglistchallenge.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

import ray.cyberpup.com.shoppinglistchallenge.R;
import ray.cyberpup.com.shoppinglistchallenge.ShoppingListActivity;

/**
 * Created on 5/17/15
 *
 * @author Raymond Tong
 */
public class ShoppingListActivityUnitTest extends ActivityUnitTestCase<ShoppingListActivity> {


    ShoppingListActivity mShoppingListActivity;
    TextView mTextView;
    Intent mLaunch;

    public ShoppingListActivityUnitTest() {
        super(ShoppingListActivity.class);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mLaunch = new Intent(getInstrumentation().getTargetContext(), ShoppingListActivity.class);
        startActivity(mLaunch, null, null);



        mShoppingListActivity = getActivity();
        mTextView = (TextView)mShoppingListActivity.findViewById(R.id.text);



    }

}
