package cf.sadhu.citypicker.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.domain.City;

/**
 * Created by sadhu on 2017/6/29.
 * 描述 普通item
 */
public class CityNormalVH extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_city_name)
    TextView mCityName;

    public CityNormalVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(City city) {
        mCityName.setText(city.name);
    }
}
