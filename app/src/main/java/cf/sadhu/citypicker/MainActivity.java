package cf.sadhu.citypicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cf.sadhu.citypicker.adapter.CityAdapter;
import cf.sadhu.citypicker.domain.City;
import cf.sadhu.citypicker.itemDecoration.FloatTagItemDecoration;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DBManager mDbManager;
    private RecyclerView mRvCity;
    private CityAdapter mCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbManager = new DBManager();
        Toast.makeText(this, mDbManager.initCityDB(this) ? "初始化成功" : "初始化失败", Toast.LENGTH_SHORT).show();
        mRvCity = (RecyclerView) findViewById(R.id.rv_city_list);
        mRvCity.setLayoutManager(new LinearLayoutManager(this));
        mRvCity.addItemDecoration(new FloatTagItemDecoration(this));

    }

    public void pullCites(View view) {
        List<City> cityList = mDbManager.getCityList(this);
        mCityAdapter = new CityAdapter(cityList);
        mCityAdapter.setHotCity(cityList.subList(0, 7));
        mRvCity.setAdapter(mCityAdapter);
    }

}
