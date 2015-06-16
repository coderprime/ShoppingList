package ray.cyberpup.com.shoppinglistchallenge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created on 6/14/15
 *
 * @author Raymond Tong
 */
public class ShopListAuthenticatorService extends Service {

    private static final Object sSyncLock = new Object();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
