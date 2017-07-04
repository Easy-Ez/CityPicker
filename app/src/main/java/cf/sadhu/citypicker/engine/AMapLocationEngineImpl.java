package cf.sadhu.citypicker.engine;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import cf.sadhu.citypicker.callback.OnLocationCallback;
import cf.sadhu.citypicker.domain.City;
import cf.sadhu.citypicker.util.PinyinUtils;

/**
 * Created by sadhu on 2017/7/4.
 * 描述 高德导航定位引擎
 */
public class AMapLocationEngineImpl implements ILocationEngine, AMapLocationListener {

    private AMapLocationClient mlocationClient;
    private OnLocationCallback mCallback;
    private Context mContext;

    @Override
    public void getLocationInfo(Context context, OnLocationCallback callback) {
        mContext = context;
        mCallback = callback;
        mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果
        locationOption.setOnceLocation(true);
        //设置定位参数
        mlocationClient.setLocationOption(locationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                StringBuilder sb = new StringBuilder();
                sb.append("country:")
                        .append(amapLocation.getCountry())//国家信息
                        .append(";province:")
                        .append(amapLocation.getProvince())//省信息
                        .append(";city:")
                        .append(amapLocation.getCity())//城市信息
                        .append(";district:")
                        .append(amapLocation.getDistrict())//城区信息
                        .append(";street:")
                        .append(amapLocation.getStreet());//街道信息
                Log.e("AmapError", "onLocationChanged: " + sb.toString());
                if (mCallback != null) {
                    mCallback.onLocationInfoChange(new City(amapLocation.getCity(),
                            PinyinUtils.getInstance(mContext).toPinyin(amapLocation.getCity())));
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                if (mCallback != null) {
                    mCallback.onLocationError(amapLocation.getErrorCode());
                }
            }
        }
    }

    @Override
    public void release() {
        mlocationClient.onDestroy();
        mCallback = null;
    }
}
