package ray.cyberpup.com.shoppinglistchallenge.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created on 5/17/15
 *
 * @author Raymond Tong
 */
public class ShopSyncService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
