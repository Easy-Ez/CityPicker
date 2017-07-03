package cf.sadhu.citypicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.domain.City;
import cf.sadhu.citypicker.domain.ICity;
import cf.sadhu.citypicker.domain.LocationInfo;
import cf.sadhu.citypicker.vh.CityHotVH;
import cf.sadhu.citypicker.vh.CityLocationVH;
import cf.sadhu.citypicker.vh.CityNormalVH;

/**
 * Created by sadhu on 2017/6/29.
 * 描述
 */
public class CityAdapter extends RecyclerView.Adapter {
    private static final int TYPE_LOCATION = 1;
    private static final int TYPE_HOT_CITY = 2;
    private static final int TYPE_NORMAL = 3;

    private List<ICity> mHotCities;
    private List<ICity> mNormalCities;
    private LocationInfo mLocationInfo;

    public CityAdapter(List<ICity> cityList) {
        this.mNormalCities = cityList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        switch (viewType) {
            case TYPE_LOCATION:
                vh = new CityLocationVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_location, parent, false));
                break;
            case TYPE_HOT_CITY:
                vh = new CityHotVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_hot, parent, false));
                break;
            case TYPE_NORMAL:
                vh = new CityNormalVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_normal, parent, false));
                break;
            default:
                vh = new CityNormalVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_normal, parent, false));
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CityLocationVH) {
            ((CityLocationVH) holder).bindData(mLocationInfo);
        } else if (holder instanceof CityHotVH) {
            ((CityHotVH) holder).bindData(mHotCities);
        } else if (holder instanceof CityNormalVH) {
            ((CityNormalVH) holder).bindData(getItemByPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mNormalCities != null) {
            return mNormalCities.size() + getHeadSize();
        } else {
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mHotCities == null || mHotCities.size() == 0) {
            if (position == 0) {
                return TYPE_LOCATION;
            } else {
                return TYPE_NORMAL;
            }
        } else {
            if (position == 0)
                return TYPE_LOCATION;
            else if (position == 1)
                return TYPE_HOT_CITY;
            else
                return TYPE_NORMAL;
        }
    }

    /**
     * 头部个数
     *
     * @return
     */
    private int getHeadSize() {
        if (mHotCities == null || mHotCities.size() == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    private ICity getItemByPosition(int position) {
        return mNormalCities.get(position - getHeadSize());
    }

    public String getTagString(int position) {
        if (position == 0) {
            return "定位城市";
        }
        if (position == 1 && getHeadSize() == 2) {
            return "热门城市";
        }
        return String.valueOf(getItemByPosition(position).getFirstChar()).toUpperCase();
    }

    /**
     * 是否带tag
     *
     * @param position
     * @return
     */
    public boolean isTag(int position) {
        int headSize = getHeadSize();
        int readPostion = position - headSize;
        return position == 0 // 第一个肯定有tag
                || (position == 1 && headSize == 2)//如果有热门城市,则第二个也是有tag
                || readPostion == 0// 普通城市的第一个也肯定有tag
                || mNormalCities.get(readPostion).getFirstChar() != mNormalCities.get(readPostion - 1).getFirstChar();// 如果当前item的首字母跟上一个item不同,也有tag
    }

    /**
     * 是否是每组的最后一个item
     *
     * @param position
     * @return
     */
    public boolean isGropEndItem(int position) {
        int headSize = getHeadSize();
        // 定位item or 热门城市只有一个
        if (position == 0)
            return true;
        if (headSize == 2 && position == 1)
            return true;
        // 最后一个item
        if (position == getItemCount() + 1)
            return true;
        // 与下一个item的tag不同
        return getItemByPosition(position).getFirstChar() != getItemByPosition(position + 1).getFirstChar();

    }

    public void setHotCity(List<ICity> hotCity) {
        if (mHotCities == null) {
            this.mHotCities = hotCity;
        } else {
            this.mHotCities.clear();
            this.mHotCities.addAll(hotCity);
        }
    }

}
