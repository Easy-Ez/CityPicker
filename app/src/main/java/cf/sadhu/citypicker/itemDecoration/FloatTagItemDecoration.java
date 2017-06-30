package cf.sadhu.citypicker.itemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import cf.sadhu.citypicker.adapter.CityAdapter;

/**
 * Created by sadhu on 2017/6/29.
 * 描述
 */
public class FloatTagItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "FloatTagItemDecoration";
    private float mHeadHeight;
    private float mPaddingLeft;
    private Paint mPaint;
    private Paint mTextPaint;

    public FloatTagItemDecoration(Context context) {
        mHeadHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                48,
                context.getResources().getDisplayMetrics());
        mPaddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                12,
                context.getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics()));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        CityAdapter adapter = (CityAdapter) parent.getAdapter();
        if (adapter.isTag(position)) {
            outRect.set(0, (int) mHeadHeight, 0, 0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            // 如果是头
            if (position != RecyclerView.NO_POSITION
                    && ((CityAdapter) parent.getAdapter()).isTag(position)) {
                drawHeader(c, parent, view, ((CityAdapter) parent.getAdapter()).getTagString(position));
            }
        }
    }

    /**
     * 画头部
     *
     * @param c
     * @param parent
     * @param view
     * @param tag
     */
    private void drawHeader(Canvas c, RecyclerView parent, View view, String tag) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int bottoom = view.getTop() - layoutParams.topMargin;
        int top = (int) (bottoom - mHeadHeight);
        drawTagText(c, left, top, right, bottoom, tag);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        View firstView = parent.getChildAt(0);
        View secondView = parent.getChildAt(1);
        if (firstView != null && secondView != null) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) firstView.getLayoutParams();
            int firstPos = parent.getChildAdapterPosition(firstView);
            int secondPos = parent.getChildAdapterPosition(secondView);
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            float bottoom = mHeadHeight;
            int top = 0;
            // 判断是否达到临界点
            // (第一个可见item是每组的最后一个,第二个可见tiem是下一组的第一个,并且第一个可见item的底部小于header的高度)
            Log.i(TAG, "onDrawOver: " + firstView.getBottom());
            if (((CityAdapter) parent.getAdapter()).isGropEndItem(firstPos)
                    && ((CityAdapter) parent.getAdapter()).isTag(secondPos)
                    && firstView.getBottom() + layoutParams.bottomMargin <= mHeadHeight) {
                bottoom = firstView.getBottom() + layoutParams.bottomMargin;
                top = (int) (bottoom - mHeadHeight);
            }
            drawTagText(c, left, top, right, (int) bottoom, ((CityAdapter) parent.getAdapter()).getTagString(firstPos));
        }
    }

    private void drawTagText(Canvas c, int left, int top, int right, int bottoom, String tagName) {
        // 计算文字居中时候的基线
        Rect targetRect = new Rect(left, top, right, bottoom);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        c.drawRect(left, top, right, bottoom, mPaint);
        c.drawText(tagName, left + mPaddingLeft, baseline, mTextPaint);
    }

}
