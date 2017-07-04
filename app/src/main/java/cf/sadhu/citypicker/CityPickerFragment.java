package cf.sadhu.citypicker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cf.sadhu.citypicker.adapter.CityAdapter;
import cf.sadhu.citypicker.callback.OnLocationCallback;
import cf.sadhu.citypicker.domain.ICity;
import cf.sadhu.citypicker.domain.LocationInfo;
import cf.sadhu.citypicker.domain.NaviInfo;
import cf.sadhu.citypicker.engine.AMapLocationEngineImpl;
import cf.sadhu.citypicker.engine.ILocationEngine;
import cf.sadhu.citypicker.util.PermissionsUtils;
import cf.sadhu.citypicker.view.AppSettingsDialog;
import cf.sadhu.citypicker.view.CityNaviBarView;
import cf.sadhu.citypicker.view.NaviPopupWindow;
import cf.sadhu.citypicker.view.itemDecoration.FloatTagItemDecoration;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by sadhu on 2017/6/29.
 * 城市选择fragment
 */
@RuntimePermissions
public class CityPickerFragment extends Fragment {

    private static final String TAG = "CityPickerFragment";
    private DBManager mDbManager;
    private RecyclerView mRvCity;
    private CityNaviBarView mNaviBarView;
    private CityAdapter mCityAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private NaviPopupWindow mPopupWindow;
    private ILocationEngine mEngine;
    private LocationInfo mLocationInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_city_picker, container, false);
        mDbManager = new DBManager();
        Toast.makeText(getContext(), mDbManager.initCityDB(getContext()) ? "初始化成功" : "初始化失败", Toast.LENGTH_SHORT).show();
        mRvCity = (RecyclerView) view.findViewById(R.id.rv_city_list);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRvCity.setLayoutManager(mLinearLayoutManager);
        mRvCity.addItemDecoration(new FloatTagItemDecoration(getContext()));
        mNaviBarView = (CityNaviBarView) view.findViewById(R.id.navibar);
        mNaviBarView.setOnNaviItemSelectListener(new CityNaviBarView.OnNaviItemSelectListener() {
            @Override
            public void onNaviItemSelect(String tag, int position) {
                Log.i(TAG, "onNaviItemSelect tag: " + tag + ";position:" + position);
                mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
                mPopupWindow.show(mRvCity, tag);
            }

            @Override
            public void onNaviTouchUp() {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new NaviPopupWindow(getContext());
        pullCites();
        return view;
    }


    private void pullCites() {
        // 从数据库获取
        List<ICity> normalCities = mDbManager.getCityList(getContext());
        mLocationInfo = new LocationInfo();
        mCityAdapter = new CityAdapter(normalCities, mLocationInfo);
        // 设置热门城市
        List<ICity> hotCities = normalCities.subList(0, 7);
        mCityAdapter.setHotCity(hotCities);
        mRvCity.setAdapter(mCityAdapter);
        // 设置导航bar的tag list
        mNaviBarView.setTagList(getTagList(normalCities, hotCities));
        CityPickerFragmentPermissionsDispatcher.getLocationInfoWithCheck(this);
    }

    /**
     * 获取tag列表
     *
     * @param cityList
     * @param hotCityList
     * @return
     */
    private Map<String, NaviInfo> getTagList(List<ICity> cityList, List<ICity> hotCityList) {
        Map<String, NaviInfo> naviMap = new LinkedHashMap<>();
        naviMap.put("定位", new NaviInfo(0));
        if (hotCityList.size() > 0) {
            naviMap.put("热门", new NaviInfo(1));
        }
        int size = naviMap.size();
        naviMap.put(String.valueOf(cityList.get(0).getFirstChar()).toUpperCase(),
                new NaviInfo(size));
        for (int i = 1; i < cityList.size(); i++) {
            if (cityList.get(i).getFirstChar() != cityList.get(i - 1).getFirstChar()) {
                naviMap.put(String.valueOf(cityList.get(i).getFirstChar()).toUpperCase(),
                        new NaviInfo(i + size));
            }
        }
        return naviMap;
    }

    //获取多个权限
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE})
    protected void getLocationInfo() {
        // 定位SDK
        mEngine = new AMapLocationEngineImpl();
        mEngine.getLocationInfo(getContext(), new OnLocationCallback() {
            @Override
            public void onLocationInfoChange(ICity city) {
                // 更新locationItem->成功
                mCityAdapter.locationSuccess(city);
            }

            @Override
            public void onLocationError(int code) {
                // 更新locationItem->定位问题
                mCityAdapter.locationError();
            }
        });
    }

    @OnShowRationale({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE})
    protected void showRationale(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setMessage("使用此功能需要部分权限，点击下一步将继续请求权限")
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//继续执行请求
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();//取消执行请求
                        Toast.makeText(getContext(), "已经拒绝了权限申请", Toast.LENGTH_SHORT).show();
                        // 更新locationItem->权限问题
                        mCityAdapter.permissionDenied();
                    }
                }).show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE})
    protected void onPermissionDenied() {
        Toast.makeText(getContext(), "已拒绝一个或以上权限", Toast.LENGTH_SHORT).show();
        // 更新locationItem->权限问题
        mCityAdapter.permissionDenied();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE})
    protected void onNeverAskAgain() {
        new AppSettingsDialog
                .Builder(this)
                .setRationale("此功能需要开启权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("好")
                .setNegativeButton("不行")
                .build()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CityPickerFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (PermissionsUtils.checkPermissions(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE)) {
                getLocationInfo();
            } else {
                // 更新locationItem->权限问题
                mCityAdapter.permissionDenied();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mEngine != null) {
            mEngine.release();
        }
    }
}
