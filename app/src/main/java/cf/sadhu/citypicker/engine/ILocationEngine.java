package cf.sadhu.citypicker.engine;

import android.content.Context;

import cf.sadhu.citypicker.callback.OnLocationCallback;

/**
 * Created by sadhu on 2017/7/2.
 * 描述: 定位功能 抽象接口
 */
public interface ILocationEngine {
    void getLocationInfo(Context context, OnLocationCallback callback);

    void release();
}
