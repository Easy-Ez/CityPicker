package cf.sadhu.citypicker.util;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.DimenRes;
import android.util.TypedValue;

/**
 * Created by sadhu on 2017/6/30.
 * 描述 ui 相关工具类
 */
public class UIUtils {
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dp2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static int getDimmens(Context context, @DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }
    public static int getTextWidth(Paint mPaint, String str) {
        float iSum = 0;
        if (str != null && !str.equals("")) {
            int len = str.length();
            float widths[] = new float[len];
            mPaint.getTextWidths(str, widths);
            for (int i = 0; i < len; i++) {
                iSum += Math.ceil(widths[i]);
            }
        }
        return (int) iSum;
    }

}
