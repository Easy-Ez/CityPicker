package cf.sadhu.citypicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.domain.ICity;
import cf.sadhu.citypicker.domain.SearchHistory;
import cf.sadhu.citypicker.vh.SearchEndVH;
import cf.sadhu.citypicker.vh.SearchResultVH;

/**
 * Created by sadhu on 2017/7/10.
 * 描述
 */
public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_END = 2;
    private List<ICity> mCities = new ArrayList<>();
    private boolean isSearchMode;
    private OnItemClickListener mListener;

    public SearchResultAdapter(List<SearchHistory> cities) {
        this.mCities.addAll(cities);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_NORMAL) {
            holder = new SearchResultVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false)
                    , mListener);
        } else {
            holder = new SearchEndVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_end, parent, false)
                    , mListener);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchResultVH) {
            ((SearchResultVH) holder).bindData(mCities.get(position), isSearchMode);
        }
    }

    @Override
    public int getItemCount() {
        if (mCities == null || mCities.size() == 0) {
            return 0;
        } else {
            if (isSearchMode) {
                return mCities.size();
            } else {
                return mCities.size() + 1;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isSearchMode) {
            return TYPE_NORMAL;
        } else {
            if (position < mCities.size()) {
                return TYPE_NORMAL;
            } else {
                return TYPE_END;
            }
        }
    }

    public void setData(List<? extends ICity> iCities, boolean isSearchMode) {
        this.isSearchMode = isSearchMode;
        mCities.clear();
        mCities.addAll(iCities);
        notifyDataSetChanged();
    }

    public List<ICity> getData() {
        return mCities;
    }

    public void setOnItemClickedListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void deleteAllItem() {
        mCities.clear();
        notifyDataSetChanged();
    }

    public static interface OnItemClickListener {
        void onItemClick(int postion, boolean isSearchMode);

        void onClearClick();
    }

}
