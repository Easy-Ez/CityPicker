package cf.sadhu.citypicker.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by sadhu on 2017/7/4.
 * 描述
 */
public class PermissionsUtils {
    // 查看权限是否已申请
    public static boolean checkPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!isAppliedPermission(context, permission))
                return false;
        }
        return true;

    }

    private static boolean isAppliedPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }
}
