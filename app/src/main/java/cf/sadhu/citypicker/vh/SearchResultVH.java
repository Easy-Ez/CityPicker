package cf.sadhu.citypicker.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.adapter.SearchResultAdapter;
import cf.sadhu.citypicker.domain.ICity;

/**
 * Created by sadhu on 2017/7/10.
 * 描述
 */
public class SearchResultVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView mImgIcon;
    private TextView mTvKeyword;
    private SearchResultAdapter.OnItemClickListener mListener;
    private boolean isSearchMode;

    public SearchResultVH(View itemView, SearchResultAdapter.OnItemClickListener listener) {
        super(itemView);
        this.mListener = listener;
        mImgIcon = (ImageView) itemView.findViewById(R.id.img_icon);
        mTvKeyword = (TextView) itemView.findViewById(R.id.tv_key_word);
        itemView.setOnClickListener(this);
    }

    public void bindData(ICity city, boolean isSearchMode) {
        this.isSearchMode = isSearchMode;
        mImgIcon.setImageResource(isSearchMode ? R.drawable.ic_search : R.drawable.ic_history);
        mTvKeyword.setText(String.format("%s (%s)", city.getCityName(), city.getCityPinYin()));
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(getAdapterPosition(), isSearchMode);
        }
    }
}
