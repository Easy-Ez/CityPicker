package cf.sadhu.citypicker.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.util.UIUtils;

/**
 * Created by sadhu on 2017/7/1.
 * 描述: 显示选中 导航 tag 的popupwindow
 */
public class NaviPopupWindow extends PopupWindow {

    private final TextView mTvNaviWord;
    private int mWindowsContentHeight;

    public NaviPopupWindow(Context context) {
        super(context);
        setWidth(UIUtils.dp2px(context, 100));
        setHeight(UIUtils.dp2px(context, 100));
        View view = LayoutInflater.from(context).inflate(R.layout.popup_navi, null);
        mTvNaviWord = (TextView) view.findViewById(R.id.tv_navi_word);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_solid_33_trans_corners_4));
        setContentView(view);
        mWindowsContentHeight = UIUtils.getDisplayContentHeight(context);
    }

    public void show(View parent, String tag) {
        // 居中
        showAtLocation(parent, Gravity.CENTER, 0, (mWindowsContentHeight - parent.getHeight()) / 2);
        setNaviWord(tag);
    }

    private void setNaviWord(String word) {
        mTvNaviWord.setText(word);
    }
}
