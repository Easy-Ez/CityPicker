package cf.sadhu.citypicker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sadhu on 2017/7/9.
 * 描述:
 */
public class SearchFragment extends DialogFragment implements CircularRevealAnim.AnimListener, DialogInterface.OnKeyListener, View.OnClickListener, ViewTreeObserver.OnPreDrawListener, TextView.OnEditorActionListener {
    private View view;
    private CircularRevealAnim mCircularRevealAnim;
    private View mViewOutSide;
    private ImageButton mSearchBtn;
    private ImageButton mBackBtn;
    private EditText mEditSearch;
    private LinearLayout mAnimationView;

    public static SearchFragment newInstance() {
        Bundle bundle = new Bundle();
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_dlg_search, container, false);
        init();//实例化
        return view;
    }

    private void init() {

        mEditSearch = (EditText) view.findViewById(R.id.et_search_contents);
        mAnimationView = (LinearLayout) view.findViewById(R.id.search_suggestion_content);
        mBackBtn = (ImageButton) view.findViewById(R.id.back);
        mSearchBtn = (ImageButton) view.findViewById(R.id.search);
        mViewOutSide = view.findViewById(R.id.view_search_outside);
        mEditSearch.setOnEditorActionListener(this);
        mBackBtn.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        mViewOutSide.setOnClickListener(this);

        //实例化动画效果
        mCircularRevealAnim = new CircularRevealAnim();
        //监听动画
        mCircularRevealAnim.setAnimListener(this);

        getDialog().setOnKeyListener(this);//键盘按键监听
        mSearchBtn.getViewTreeObserver().addOnPreDrawListener(this);//绘制监听


    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }


    /**
     * 初始化SearchFragment
     */
    private void initDialog() {
        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setWindowAnimations(R.style.DialogEmptyAnimation);//取消过渡动画 , 使DialogSearch的出现更加平滑
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back || view.getId() == R.id.view_search_outside) {
            hideAnim();
        } else if (view.getId() == R.id.search) {
            search();
        }
    }

    /**
     * 监听搜索键绘制时
     */
    @Override
    public boolean onPreDraw() {
        mSearchBtn.getViewTreeObserver().removeOnPreDrawListener(this);
        mCircularRevealAnim.show(mSearchBtn, mAnimationView);
        return true;
    }

    /**
     * 监听键盘按键
     */
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            hideAnim();
            return true;
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }


    /**
     * 搜索框动画隐藏完毕时调用
     */
    @Override
    public void onHideAnimationEnd() {
        mEditSearch.setText("");
        dismiss();
    }

    /**
     * 搜索框动画显示完毕时调用
     */
    @Override
    public void onShowAnimationEnd() {
        if (isVisible()) {
            InputMethodManager etSearchKeyword = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            etSearchKeyword.showSoftInput(mEditSearch, InputMethodManager.SHOW_FORCED);
        }
    }

    private void hideAnim() {
        InputMethodManager etSearchKeyword = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        etSearchKeyword.hideSoftInputFromInputMethod(mEditSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mCircularRevealAnim.hide(mSearchBtn, mAnimationView);
    }

    private void search() {

    }


}
