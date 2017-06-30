package cf.sadhu.citypicker.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.domain.LocationInfo;
import cf.sadhu.citypicker.util.UIUtils;

/**
 * Created by sadhu on 2017/6/29.
 * 描述 定位item
 */
public class CityLocationVH extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_location_name)
    TextView mLocationName;


    public CityLocationVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mLocationName.setMinWidth(
                (UIUtils.getScreenWidth(itemView.getContext())
                        - UIUtils.getDimmens(itemView.getContext(), R.dimen.naviBarWidth)
                        - UIUtils.getDimmens(itemView.getContext(), R.dimen.itemPadding )* 2
                        - UIUtils.getDimmens(itemView.getContext(), R.dimen.itemSpace )* 2) / 3);

    }

    public void bindData(LocationInfo info) {


        if (info == null || info.mStatus == 0) {
            mLocationName.setText("正在定位...");
        } else if (info.mStatus == 1 && info.mCity != null) {
            mLocationName.setText(info.mCity.name);
        } else {
            mLocationName.setText("定位失败");
        }
    }
}
