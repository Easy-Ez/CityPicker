package cf.sadhu.citypicker.view.itemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * recyclerView 通用的Decoration
 * 给每个item 设置 margin值
 */
public class MyGridOffestDecoration extends RecyclerView.ItemDecoration {

    public static final String TAG = "OffestDecoration";
    private int mSpace;
    private int mOffset;
    private int mEdgeSpace;

    /**
     * @param mSpace item间的间距 默认没有边距
     */
    public MyGridOffestDecoration(int mSpace, Context ctx) {
        this(mSpace, 0, ctx);
    }

    /**
     * @param mSpace     item间的间距
     * @param mEdgeSpace 边距(padding)
     */
    public MyGridOffestDecoration(int mSpace, int mEdgeSpace, Context ctx) {
        this(mSpace, mEdgeSpace, mEdgeSpace, ctx);
    }

    /**
     * @param mSpace item间的间距
     * @param mEdgeSpace 边距(padding)
     * @param mOffset
     * @param ctx
     */
    public MyGridOffestDecoration(int mSpace, int mEdgeSpace, int mOffset, Context ctx) {
        this.mSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSpace, ctx.getResources().getDisplayMetrics()) + 0.5f);
        this.mEdgeSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mEdgeSpace, ctx.getResources().getDisplayMetrics()) + 0.5f);
        this.mOffset = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mOffset, ctx.getResources().getDisplayMetrics()) + 0.5f);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int childPosition = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (manager != null) {
            if (manager instanceof GridLayoutManager) {
                // manager为GridLayoutManager时
                setGridOffset(((GridLayoutManager) manager).getOrientation(), ((GridLayoutManager) manager).getSpanCount(), outRect, childPosition, itemCount);
            }
        }
    }


    /**
     * 设置GridLayoutManager 类型的 offest
     *
     * @param orientation   方向
     * @param spanCount     个数
     * @param outRect       padding
     * @param childPosition 在 list 中的 postion
     * @param itemCount     list size
     */
    private void setGridOffset(int orientation, int spanCount, Rect outRect, int childPosition, int itemCount) {
        float totalSpace = mSpace * (spanCount - 1) + mEdgeSpace * 2; // 总共的padding值
        float eachSpace = totalSpace / spanCount; // 分配给每个item的padding值
        int column = childPosition % spanCount; // 列数
        int row = childPosition / spanCount;// 行数
        float left;
        float right;
        float top;
        float bottom;
        if (orientation == GridLayoutManager.VERTICAL) {
            top = 0; // 默认 top为0
            bottom = mOffset; // 默认bottom为间距值
            if (mEdgeSpace == 0) {
                left = column * eachSpace / (spanCount - 1);
                right = eachSpace - left;
                // 无边距的话  只有最后一行bottom为0
                // 只有一行
                if (itemCount <= spanCount) {
                    if (0 == row) {
                        bottom = 0;
                    }
                } else {
                    // 多行
                    if (itemCount / spanCount == row) {
                        bottom = 0;
                    }
                }

            } else {
                // 只有一行
                if (itemCount <= spanCount) {
                    top = mEdgeSpace;
                    bottom = mEdgeSpace;
                } else {
                    if (childPosition < spanCount) {
                        // 有边距的话 第一行top为边距值
                        top = mEdgeSpace;
                    } else if (itemCount / spanCount == row) {
                        // 有边距的话 最后一行bottom为边距值
                        bottom = mEdgeSpace;
                    }
                }


                left = column * (eachSpace - mEdgeSpace - mEdgeSpace) / (spanCount - 1) + mEdgeSpace;
                right = eachSpace - left;
            }
        } else {
            // orientation == GridLayoutManager.HORIZONTAL 跟上面的大同小异, 将top,bottom替换为left,right即可
            left = 0;
            right = mOffset;
            if (mEdgeSpace == 0) {
                top = column * eachSpace / (spanCount - 1);
                bottom = eachSpace - top;
                if (itemCount / spanCount == row) {
                    right = 0;
                }
            } else {
                if (childPosition < spanCount) {
                    left = mEdgeSpace;
                } else if (itemCount / spanCount == row) {
                    right = mEdgeSpace;
                }
                top = column * (eachSpace - mEdgeSpace - mEdgeSpace) / (spanCount - 1) + mEdgeSpace;
                bottom = eachSpace - top;
            }
        }
        outRect.set((int) left, (int) top, (int) right, (int) bottom);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}

