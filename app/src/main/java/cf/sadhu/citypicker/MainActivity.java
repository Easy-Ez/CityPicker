package cf.sadhu.citypicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cf.sadhu.citypicker.adapter.CityAdapter;
import cf.sadhu.citypicker.domain.City;
import cf.sadhu.citypicker.domain.NaviInfo;
import cf.sadhu.citypicker.itemDecoration.FloatTagItemDecoration;
import cf.sadhu.citypicker.view.CityNaviBarView;
import cf.sadhu.citypicker.view.NaviPopupWindow;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DBManager mDbManager;
    private RecyclerView mRvCity;
    private CityNaviBarView mNaviBarView;
    private CityAdapter mCityAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private NaviPopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbManager = new DBManager();
        Toast.makeText(this, mDbManager.initCityDB(this) ? "初始化成功" : "初始化失败", Toast.LENGTH_SHORT).show();
        mRvCity = (RecyclerView) findViewById(R.id.rv_city_list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvCity.setLayoutManager(mLinearLayoutManager);
        mRvCity.addItemDecoration(new FloatTagItemDecoration(this));
        mNaviBarView = (CityNaviBarView) findViewById(R.id.navibar);
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
        mPopupWindow = new NaviPopupWindow(this);

    }

    public void pullCites(View view) {
        List<City> cityList = mDbManager.getCityList(this);
        mCityAdapter = new CityAdapter(cityList);
        mCityAdapter.setHotCity(cityList.subList(0, 7));
        mRvCity.setAdapter(mCityAdapter);
        mNaviBarView.setTagList(getTagList(cityList));
    }


    private Map<String, NaviInfo> getTagList(List<City> cityList) {

        Map<String, NaviInfo> naviMap = new LinkedHashMap<>();

        naviMap.put("定位", new NaviInfo(0));
        naviMap.put("热门", new NaviInfo(1));
        naviMap.put(String.valueOf(cityList.get(0).getFirstChar()).toUpperCase(),
                new NaviInfo(2));
        for (int i = 1; i < cityList.size(); i++) {
            if (cityList.get(i).getFirstChar() != cityList.get(i - 1).getFirstChar()) {
                naviMap.put(String.valueOf(cityList.get(i).getFirstChar()).toUpperCase(),
                        new NaviInfo(i + 2));
            }
        }
        return naviMap;
    }

}
