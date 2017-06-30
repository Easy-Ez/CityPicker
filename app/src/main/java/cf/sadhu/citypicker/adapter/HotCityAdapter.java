package cf.sadhu.citypicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.domain.City;

/**
 * Created by sadhu on 2017/6/30.
 * 描述 热门城市
 */
public class HotCityAdapter extends RecyclerView.Adapter<HotCityAdapter.HotCityVH> {
    private List<City> mHotCities;

    public HotCityAdapter(List<City> cities) {
        mHotCities = cities;
    }

    @Override
    public HotCityVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotCityVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_hot_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HotCityVH holder, int position) {
        holder.mTvCity.setText(mHotCities.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mHotCities != null ? mHotCities.size() : 0;
    }

    static class HotCityVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_city_name)
        TextView mTvCity;

        public HotCityVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
