package cf.sadhu.citypicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cf.sadhu.citypicker.R;
import cf.sadhu.citypicker.util.UIUtils;

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
    private List<String> mTagList = new ArrayList<>();


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
        mSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics()) + 0.5f);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultWidthSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultHeightSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
        if (mTagList == null || mTagList.size() == 0) {
            return mDefualtWidth;
        } else {
            String maxLenghtItem = "";
            for (int i = 0; i < mTagList.size(); i++) {
                if (mTagList.get(i).length() > maxLenghtItem.length()) {
                    maxLenghtItem = mTagList.get(i);
                }
            }
            return UIUtils.getTextWidth(mTextPaint, maxLenghtItem);
        }
    }

    /**
     * 获取所有标签的行高
     *
     * @return
     */
    private int getTagLineHeight() {
        if (mTagList == null || mTagList.size() == 0) {
            return mDefualtHeight;
        } else {
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            float height = fm.bottom - fm.top + fm.leading;
            return (int) (height * mTagList.size()) + (mSpace * mTagList.size() - 1);
        }
    }

    public void setTagList(List<String> tags) {
        this.mTagList.clear();
        this.mTagList.addAll(tags);
        requestLayout();
    }


}
