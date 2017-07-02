package cf.sadhu.citypicker.vh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.adapter.HotCityAdapter;
import cf.sadhu.citypicker.domain.City;
import cf.sadhu.citypicker.view.itemDecoration.MyGridOffestDecoration;
import cf.sadhu.citypicker.util.UIUtils;

/**
 * Created by sadhu on 2017/6/29.
 * 描述 热门item
 */
public class CityHotVH extends RecyclerView.ViewHolder {
    @BindView(R.id.rv_hot_city_list)
    RecyclerView mRvHot;
    private List<City> mHotCities;

    public CityHotVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        ViewGroup.LayoutParams layoutParams = mRvHot.getLayoutParams();
        layoutParams.width = UIUtils.getScreenWidth(itemView.getContext()) -
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.naviBarWidth);
        mRvHot.setLayoutParams(layoutParams);
        mRvHot.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
        mRvHot.addItemDecoration(new MyGridOffestDecoration(12, 12, itemView.getContext()));
        mRvHot.setHasFixedSize(true);
        mHotCities = new ArrayList<>();
        mRvHot.setAdapter(new HotCityAdapter(mHotCities));
    }

    public void bindData(List<City> cities) {
        mHotCities.clear();
        mHotCities.addAll(cities);
        mRvHot.getAdapter().notifyDataSetChanged();
    }
}
