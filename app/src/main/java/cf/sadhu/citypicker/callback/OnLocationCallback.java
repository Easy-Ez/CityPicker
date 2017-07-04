package cf.sadhu.citypicker.callback;

import cf.sadhu.citypicker.domain.ICity;
import cf.sadhu.citypicker.domain.LocationInfo;

/**
 * Created by sadhu on 2017/7/4.
 * 描述 定位回调
 */
public interface OnLocationCallback {
    void onLocationInfoChange(ICity info);

    void onLocationError(int code);
}
