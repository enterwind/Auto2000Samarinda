package vay.enterwind.auto2000samarinda.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by novay on 09/03/18.
 */

public class LikeService extends Service {

    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Saving action implementation
        Toast.makeText(this, ""+intent, Toast.LENGTH_SHORT).show();
        return null;
    }

}
