package ray.cyberpup.com.shoppinglistchallenge.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.TextView;

import junit.framework.Assert;

import ray.cyberpup.com.shoppinglistchallenge.R;
import ray.cyberpup.com.shoppinglistchallenge.ShoppingListActivity;

/**
 * Created on 5/17/15
 *
 * @author Raymond Tong
 */
public class ShoppingListActivityFunctionalTest extends ActivityInstrumentationTestCase2<ShoppingListActivity> {

    private ShoppingListActivity mShoppingListActivity;

    private TextView mFirstTestText;

    public ShoppingListActivityFunctionalTest() {
        super(ShoppingListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mShoppingListActivity = getActivity();
        mFirstTestText = (TextView)mShoppingListActivity.findViewById(R.id.text);

    }

    public void testTextView(){

        String actual = mFirstTestText.getText().toString();
        String expected = "Hello Ray!";
        Assert.assertEquals("Strings do not match.", expected, actual);


        ViewAsserts.assertOnScreen(mShoppingListActivity.getWindow().getDecorView(), mFirstTestText);
        assertEquals("Wrong Text", expected, actual);
    }
}
