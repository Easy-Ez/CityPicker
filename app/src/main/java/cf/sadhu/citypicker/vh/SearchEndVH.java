package cf.sadhu.citypicker.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cf.sadhu.citypicker.adapter.SearchResultAdapter;

/**
 * Created by sadhu on 2017/7/10.
 * 描述 搜索历史清除历史vh
 */
public class SearchEndVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SearchResultAdapter.OnItemClickListener mListener;

    public SearchEndVH(View itemView, SearchResultAdapter.OnItemClickListener listener) {
        super(itemView);
        this.mListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClearClick();
        }
    }
}
