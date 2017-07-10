package cf.sadhu.citypicker.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cf.sadhu.citypicker.anim.CircularRevealAnim;
import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.adapter.SearchResultAdapter;
import cf.sadhu.citypicker.db.CityDBManager;
import cf.sadhu.citypicker.db.SearchHistoryDao;
import cf.sadhu.citypicker.domain.ICity;
import cf.sadhu.citypicker.domain.SearchHistory;
import cf.sadhu.citypicker.view.itemDecoration.LinearDividerDecoration;

/**
 * Created by sadhu on 2017/7/9.
 * 描述:
 */
public class SearchFragment extends DialogFragment implements CircularRevealAnim.AnimListener, DialogInterface.OnKeyListener, View.OnClickListener, ViewTreeObserver.OnPreDrawListener, TextView.OnEditorActionListener, TextWatcher, SearchResultAdapter.OnItemClickListener {
    private View view;
    private View mViewOutSide;
    private ImageButton mSearchBtn;
    private ImageButton mBackBtn;
    private EditText mEditSearch;
    private LinearLayout mAnimationView;
    private RecyclerView mSearchList;
    private ImageView mImgClear;
    private SearchHistoryDao mDao;
    private CityDBManager mCityDBManager;
    private List<SearchHistory> mSearchHistories;
    private SearchResultAdapter mAdapter;
    private CircularRevealAnim mCircularRevealAnim;
    private OnSelectCityListener mOnSelectCityListener;

    public static SearchFragment newInstance() {
        Bundle bundle = new Bundle();
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectCityListener) {
            mOnSelectCityListener = (OnSelectCityListener) context;
        }
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

        mViewOutSide = view.findViewById(R.id.view_search_outside);
        mAnimationView = (LinearLayout) view.findViewById(R.id.search_content);
        mEditSearch = (EditText) view.findViewById(R.id.et_search_contents);
        mImgClear = (ImageView) view.findViewById(R.id.img_clear);
        mSearchList = (RecyclerView) view.findViewById(R.id.list_search_result);

        mBackBtn = (ImageButton) view.findViewById(R.id.back);
        mSearchBtn = (ImageButton) view.findViewById(R.id.search);
        mEditSearch.setOnEditorActionListener(this);
        mEditSearch.addTextChangedListener(this);
        mBackBtn.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        mViewOutSide.setOnClickListener(this);
        mImgClear.setOnClickListener(this);

        mCityDBManager = new CityDBManager();
        mDao = new SearchHistoryDao(getContext());
        // 最多十条记录
        mSearchHistories = mDao.queryWithLimit(10);
        mSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        // 分割线
        mSearchList.addItemDecoration(new LinearDividerDecoration.Builder(getContext())
                .setPaddingLeftOrTop(40)
                .setShowBottom(false)
                .builder());
        mAdapter = new SearchResultAdapter(mSearchHistories);
        mAdapter.setOnItemClickedListener(this);
        mSearchList.setAdapter(mAdapter);

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
        } else if (view.getId() == R.id.img_clear) {
            mEditSearch.getText().clear();
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
     * list item 点击
     *
     * @param postion      位置
     * @param isSearchMode 是否搜索模式
     */
    @Override
    public void onItemClick(int postion, boolean isSearchMode) {
        // 搜索模式先存入数据库
        SearchHistory searchHistory;
        ICity iCity = mAdapter.getData().get(postion);
        if (isSearchMode) {
            searchHistory = new SearchHistory(iCity.getCityName(), iCity.getCityPinYin());
        } else {
            searchHistory = mSearchHistories.get(postion);
        }
        searchHistory.setOperateTime(System.currentTimeMillis());
        mDao.createOrUpdate(searchHistory);
        hideAnim();
        //回调
        if (mOnSelectCityListener != null) {
            mOnSelectCityListener.onSelectCity(iCity);
        }
    }

    /**
     * 清除搜索历史
     */
    @Override
    public void onClearClick() {
        mDao.deleteAll();
        mAdapter.deleteAllItem();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    private static final String TAG = "SearchFragment";

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
            mImgClear.setVisibility(View.VISIBLE);
            List<ICity> iCities = mCityDBManager.queryLike(getContext(), s.toString());
            mAdapter.setData(iCities, true);
        } else {
            mImgClear.setVisibility(View.INVISIBLE);
            mAdapter.setData(mSearchHistories, false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

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


    public static interface OnSelectCityListener {
        void onSelectCity(ICity city);
    }

}
