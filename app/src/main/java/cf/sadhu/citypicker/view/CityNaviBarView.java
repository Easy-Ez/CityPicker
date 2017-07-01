package cf.sadhu.citypicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.domain.NaviInfo;

/**
 * Created by sadhu on 2017/6/30.
 * See {@link cf.sadhu.citypicker.R.styleable#CityNaviBarView CityNaviBarView Attributes},
 *
 * @attr ref android.R.styleable#TextView_text
 * 描述
 */
public class CityNaviBarView extends View {

    private int mTextSize;
    private int mTextColor;
    private int mSelectTextSize;
    private int mSelectTextColor;
    private int mDefualtWidth;
    private int mDefualtHeight;
    private int mSpace;
    private Paint mTextPaint;
    private Map<String, NaviInfo> mNaviMap = new HashMap<>();
    private OnNaviItemSelectListener mOnNaviItemSelectListener;
    private int selectPostion = -1;

    public CityNaviBarView(Context context) {
        this(context, null);
    }

    public CityNaviBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDefaultValue();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CityNaviBarView);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CityNaviBarView_textSize, mTextSize);
        mSelectTextSize = typedArray.getDimensionPixelSize(R.styleable.CityNaviBarView_selectTextSize, mSelectTextSize);
        mTextColor = typedArray.getColor(R.styleable.CityNaviBarView_textColor, mTextColor);
        mSelectTextColor = typedArray.getColor(R.styleable.CityNaviBarView_selectTextColor, mSelectTextColor);
        mSpace = typedArray.getColor(R.styleable.CityNaviBarView_space, mSpace);
        typedArray.recycle();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    private void initDefaultValue() {
        mTextSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getContext().getResources().getDisplayMetrics()) + 0.5f);
        mSelectTextSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getContext().getResources().getDisplayMetrics()) + 0.5f);
        mTextColor = ContextCompat.getColor(getContext(), R.color.color99);
        mSelectTextSize = ContextCompat.getColor(getContext(), R.color.color33);
        mDefualtWidth = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getContext().getResources().getDisplayMetrics()) + 0.5f);
        mDefualtHeight = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getContext().getResources().getDisplayMetrics()) + 0.5f);
        mSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics()) + 0.5f);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultWidthSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultHeightSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mNaviMap.size() > 0) {
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            float height = fm.bottom - fm.top;
            float baseLine = (float) getPaddingTop() - fm.top;
            Set<Map.Entry<String, NaviInfo>> entries = mNaviMap.entrySet();
            for (Map.Entry<String, NaviInfo> entry : entries) {
                if (entry.getValue().position == selectPostion) {
                    mTextPaint.setColor(mSelectTextColor);
                } else {
                    mTextPaint.setColor(mTextColor);
                }
                canvas.drawText(entry.getKey(), getWidth() / 2 - mTextPaint.measureText(entry.getKey()) / 2, baseLine, mTextPaint);
                mNaviMap.get(entry.getKey()).bottom = baseLine + fm.bottom;
                mNaviMap.get(entry.getKey()).top = baseLine + fm.top;
                baseLine = (baseLine + height + mSpace);
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                searchHitItem(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                searchHitItem(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                selectPostion = -1;
                invalidate();
                if (mOnNaviItemSelectListener != null) {
                    mOnNaviItemSelectListener.onNaviTouchUp();
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private void searchHitItem(float x, float y) {
        if (x < 0 || y < 0 || y > getHeight()) {
            return;
        }
        Set<Map.Entry<String, NaviInfo>> entries = mNaviMap.entrySet();
        for (Map.Entry<String, NaviInfo> entry : entries) {
            NaviInfo value = entry.getValue();
            if (value.top <= y && value.bottom >= y) {
                invalidate(new Rect(0, (int) value.top, getWidth(), (int) value.bottom));
                if (mOnNaviItemSelectListener != null && selectPostion != value.position) {
                    mOnNaviItemSelectListener.onNaviItemSelect(entry.getKey(), value.position);
                }
                selectPostion = value.position;
                break;
            }
        }
    }

    /**
     * 获取默认的的宽度
     *
     * @param size
     * @param measureSpec
     * @return
     */
    private int getDefaultWidthSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = getTagMaxWidth() + getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * 获取默认的高度
     *
     * @param size
     * @param measureSpec
     * @return
     */
    private int getDefaultHeightSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = getTagLineHeight() + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * 获取标签中最宽字符的宽度
     *
     * @return
     */
    private int getTagMaxWidth() {
        if (mNaviMap == null || mNaviMap.size() == 0) {
            return mDefualtWidth;
        } else {
            String maxLenghtItem = "";
            Set<String> strings = mNaviMap.keySet();
            for (String chars : strings) {
                if (chars.length() > maxLenghtItem.length()) {
                    maxLenghtItem = chars;
                }
            }
            return (int) mTextPaint.measureText(maxLenghtItem);
        }
    }

    /**
     * 获取所有标签的行高
     *
     * @return
     */
    private int getTagLineHeight() {
        if (mNaviMap == null || mNaviMap.size() == 0) {
            return mDefualtHeight;
        } else {
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            float height = fm.bottom - fm.top;
            return (int) (height * mNaviMap.size()) + (mSpace * mNaviMap.size() - 1);
        }
    }

    public void setTagList(Map<String, NaviInfo> map) {
        this.mNaviMap = map;
        requestLayout();
    }

    public void setOnNaviItemSelectListener(OnNaviItemSelectListener listener) {
        this.mOnNaviItemSelectListener = listener;
    }

    public static interface OnNaviItemSelectListener {
        void onNaviItemSelect(String tag, int position);

        void onNaviTouchUp();
    }
}
