package cf.sadhu.citypicker.view.itemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import cf.sadhu.citypicker.R;

/**
 * recyclerView 通用的Decoration
 * 给每个item 设置 margin值
 */
public class LinearDividerDecoration extends RecyclerView.ItemDecoration {

    public static final String TAG = "OffestDecoration";


    private int mHeightOrWidth;
    private int mPaddingLeftOrTop;
    private int mPaddingRightOrBottom;
    private boolean mShowBottom;
    private Paint mRectPaint;
    private Paint mPaddingPaint;
    private onDrawItemListener mOnDrawItemListener;

    private LinearDividerDecoration(Builder builder) {
        if (builder.useDipUnit) {
            this.mHeightOrWidth = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, builder.heightOrWidth, builder.ctx.getResources().getDisplayMetrics()) + 0.5f);
        } else {
            this.mHeightOrWidth = builder.heightOrWidth;
        }
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(builder.ctx.getResources().getColor(builder.color));
        mRectPaint.setStyle(Paint.Style.FILL);
        this.mShowBottom = builder.showBottom;
        if (builder.paddingLeftOrTop != 0 || builder.paddingRightOrBottom != 0) {
            this.mPaddingLeftOrTop = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, builder.paddingLeftOrTop, builder.ctx.getResources().getDisplayMetrics()) + 0.5f);
            this.mPaddingRightOrBottom = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, builder.paddingRightOrBottom, builder.ctx.getResources().getDisplayMetrics()) + 0.5f);
            mPaddingPaint = new Paint();
            mPaddingPaint.setAntiAlias(true);
            mPaddingPaint.setColor(Color.WHITE);
            mPaddingPaint.setStyle(Paint.Style.FILL);
        }
        mOnDrawItemListener = builder.listener;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int childPosition = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (manager != null) {
            if (manager instanceof LinearLayoutManager) {
                if (mOnDrawItemListener == null || mOnDrawItemListener.needDrawDivider(parent, view)) {
                    // manager为LinearLayoutManager时
                    setLinearOffset(((LinearLayoutManager) manager).getOrientation(), outRect, childPosition, itemCount);
                }
            } else {
                throw new RuntimeException("this decoration only use at LinearLayoutManager");
            }
        }
    }

    /**
     * LayoutManager 设置offset
     *
     * @param orientation   方向
     * @param outRect       padding
     * @param childPosition 在 list 中的 postion
     * @param itemCount     list size
     */
    private void setLinearOffset(int orientation, Rect outRect, int childPosition, int itemCount) {
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (childPosition != itemCount - 1 || mShowBottom) {
                outRect.set(0, 0, mHeightOrWidth, 0);
            }
        } else {
            if (childPosition != itemCount - 1 || mShowBottom) {
                outRect.set(0, 0, 0, mHeightOrWidth);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                if (mOnDrawItemListener == null || mOnDrawItemListener.needDrawDivider(parent, child)) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    int left, top, right, bottom;
                    boolean isVertical = ((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL;
                    if (isVertical) {
                        left = mPaddingLeftOrTop;
                        top = child.getBottom() + params.bottomMargin;
                        right = parent.getWidth() - mPaddingRightOrBottom;
                        bottom = top + mHeightOrWidth;
                    } else {
                        left = child.getRight() + params.rightMargin;
                        top = mPaddingLeftOrTop;
                        right = left + mHeightOrWidth;
                        bottom = parent.getHeight() - mPaddingRightOrBottom;
                    }
                    c.drawRect(
                            left,
                            top,
                            right,
                            bottom,
                            mRectPaint);
                    if (mPaddingPaint != null) {
                        if (mPaddingLeftOrTop != 0) {
                            c.drawRect(
                                    isVertical ? 0 : left,
                                    isVertical ? top : 0,
                                    isVertical ? mPaddingLeftOrTop : right,
                                    isVertical ? bottom : mPaddingLeftOrTop,
                                    mPaddingPaint);
                        }
                        if (mPaddingRightOrBottom != 0) {
                            c.drawRect(
                                    isVertical ? right : left,
                                    isVertical ? top : bottom,
                                    isVertical ? parent.getWidth() : right,
                                    isVertical ? bottom : parent.getHeight(),
                                    mPaddingPaint);
                        }
                    }

                }
            }
        }
    }

    public LinearDividerDecoration setHeightOrWidth(int heightOrWidth) {
        mHeightOrWidth = heightOrWidth;
        return this;
    }


    public static class Builder {
        @ColorRes
        private int color = R.color.colorE2;
        private Context ctx;
        private int heightOrWidth = 1;
        private int paddingLeftOrTop;
        private int paddingRightOrBottom;
        private boolean showBottom;
        private boolean useDipUnit;
        private onDrawItemListener listener;

        public Builder(Context ctx) {
            this.ctx = ctx;
        }

        public Builder setColor(@ColorRes int color) {
            this.color = color;
            return this;
        }

        public Builder setHeightOrWidth(int heightOrWidth) {
            this.heightOrWidth = heightOrWidth;
            return this;
        }

        public Builder setPaddingLeftOrTop(int paddingLeftOrTop) {
            this.paddingLeftOrTop = paddingLeftOrTop;
            return this;
        }

        public Builder setPaddingRightOrBottom(int paddingRightOrBottom) {
            this.paddingRightOrBottom = paddingRightOrBottom;
            return this;
        }

        public Builder setShowBottom(boolean showBottom) {
            this.showBottom = showBottom;
            return this;
        }

        public Builder setUseDipUnit(boolean useDipUnit) {
            this.useDipUnit = useDipUnit;
            return this;
        }

        public Builder setListener(onDrawItemListener listener) {
            this.listener = listener;
            return this;
        }

        public int getColor() {
            return color;
        }

        public Context getCtx() {
            return ctx;
        }

        public int getHeightOrWidth() {
            return heightOrWidth;
        }

        public int getPaddingLeftOrTop() {
            return paddingLeftOrTop;
        }

        public int getPaddingRightOrBottom() {
            return paddingRightOrBottom;
        }

        public boolean isShowBottom() {
            return showBottom;
        }

        public boolean isUseDipUnit() {
            return useDipUnit;
        }

        public LinearDividerDecoration builder() {
            return new LinearDividerDecoration(this);
        }

    }

    public static interface onDrawItemListener {
        boolean needDrawDivider(RecyclerView parent, View view);
    }
}

