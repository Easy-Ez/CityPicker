package cf.sadhu.citypicker.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.domain.ICity;

import static cf.sadhu.citypicker.ui.activity.CityPickerActivity.EXTRA_NAME;

/**
 * Created by sadhu on 2017/7/10.
 * 描述
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private TextView mCityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        mCityName = (TextView) findViewById(R.id.tv_city);
    }

    public void startSelectCity(View view) {
        startActivityForResult(new Intent(this, CityPickerActivity.class), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ICity city = (ICity) data.getSerializableExtra(EXTRA_NAME);
            mCityName.setText(city.getCityName());
        }

    }
}
