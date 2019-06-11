package com.appimake.drawerapp.expand;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.appimake.drawerapp.R;

public class DAExpandableLayout extends FrameLayout {

    private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final int DEFAULT_DURATION = 200;
    private int collapseHeight;
    private int collapseTargetId;
    private int collapsePadding;
    private int duration;
    private int portraitMeasuredHeight = -1;
    private int landscapeMeasuredHeight = -1;
    private Scroller scroller;
    private View subView;
    private Status status = Status.COLLAPSED;
    private OnExpandListener expandListener;
    private Interpolator interpolator;
    private Runnable movingRunnable = new Runnable() {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                getLayoutParams().height = scroller.getCurrY();
                requestLayout();
                post(this);
                return;
            }
            if (scroller.getCurrY() == getTotalCollapseHeight()) {
                status = Status.COLLAPSED;
                notifyCollapseEvent();
            } else {
                status = Status.EXPANDED;
                notifyExpandEvent();
            }
        }
    };

    public DAExpandableLayout(final Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public DAExpandableLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public DAExpandableLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DAExpandableLayout(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        refreshScroller();

        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DAExpandableLayout, defStyleAttr, defStyleRes);
        collapseHeight = typedArray.getDimensionPixelOffset(R.styleable.DAExpandableLayout_exl_collapseHeight, 0);
        collapseTargetId = typedArray.getResourceId(R.styleable.DAExpandableLayout_exl_collapseTargetId, 0);
        collapsePadding = typedArray.getDimensionPixelOffset(R.styleable.DAExpandableLayout_exl_collapsePadding, 0);
        duration = typedArray.getInteger(R.styleable.DAExpandableLayout_exl_duration, 0);
        boolean initialExpanded = typedArray.getBoolean(R.styleable.DAExpandableLayout_exl_expanded, false);
        status = initialExpanded ? Status.EXPANDED : Status.COLLAPSED;
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (!isMoving()) {
            setExpandedMeasuredHeight(getMaxChildHeight(widthMeasureSpec));
        }

        if (isExpanded()) {
            setMeasuredDimension(widthMeasureSpec, getExpandedMeasuredHeight());
        } else if (isCollapsed()) {
            setMeasuredDimension(widthMeasureSpec, getTotalCollapseHeight());
        } else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private int getMaxChildHeight(int widthMeasureSpec) {
        int max = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, MeasureSpec.UNSPECIFIED);
            max = Math.max(max, child.getMeasuredHeight());
        }
        return max;
    }

    private int getTotalCollapseHeight() {
        if (collapseHeight > 0) {
            return collapseHeight + collapsePadding;
        }
        View view = findViewById(collapseTargetId);
        if (view == null) {
            return 0;
        }
        return (getRelativeTop(view) - getTop()) + collapsePadding;
    }

    private int getRelativeTop(View target) {
        if (target == null) {
            return 0;
        }
        if (target.getParent().equals(this)) {
            return target.getTop();
        }
        return target.getTop() + getRelativeTop((View) target.getParent());
    }

    private int getExpandedMeasuredHeight() {
        return isPortrait() ? portraitMeasuredHeight : landscapeMeasuredHeight;
    }

    private void setExpandedMeasuredHeight(int measuredHeight) {
        if (isPortrait()) {
            portraitMeasuredHeight = measuredHeight;
        } else {
            landscapeMeasuredHeight = measuredHeight;
        }
    }

    private boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private int getAnimateDuration() {
        return duration > 0 ? duration : DEFAULT_DURATION;
    }

    private void notifyExpandEvent() {
        if (expandListener != null) {
            expandListener.onExpanded(this);
        }
    }

    private void notifyCollapseEvent() {
        if (expandListener != null) {
            expandListener.onCollapsed(this);
        }
    }

    private void refreshScroller() {
        Interpolator interpolator = this.interpolator != null ? this.interpolator : DEFAULT_INTERPOLATOR;
        scroller = new Scroller(getContext(), interpolator);
    }

    public void expand() {
        expand(true);
    }

    public void expand(boolean smoothScroll) {
        if (isExpanded() || isMoving()) {
            return;
        }
        status = Status.MOVING;
        int duration = smoothScroll ? getAnimateDuration() : 0;
        int collapseHeight = getTotalCollapseHeight();
        scroller.startScroll(0, collapseHeight, 0, getExpandedMeasuredHeight() - collapseHeight, duration);
        if (smoothScroll) {
            post(movingRunnable);
        } else {
            movingRunnable.run();
        }
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean smoothScroll) {
        if (isExpanded()) {
            collapse(smoothScroll);
        } else {
            expand(smoothScroll);
        }
    }

    public void collapse() {
        collapse(true);
    }

    public void collapse(boolean smoothScroll) {
        if (isCollapsed() || isMoving()) {
            return;
        }
        status = Status.MOVING;
        int duration = smoothScroll ? getAnimateDuration() : 0;
        int expandedMeasuredHeight = getExpandedMeasuredHeight();
        scroller.startScroll(0, expandedMeasuredHeight, 0, -(expandedMeasuredHeight - getTotalCollapseHeight()), duration);
        if (smoothScroll) {
            post(movingRunnable);
        } else {
            movingRunnable.run();
        }
    }

    public Status getStatus() {
        return status;
    }

    public boolean isExpanded() {
        return status != null && status.equals(Status.EXPANDED);
    }

    public boolean isCollapsed() {
        return status != null && status.equals(Status.COLLAPSED);
    }

    public boolean isMoving() {
        return status != null && status.equals(Status.MOVING);
    }

    public int getCollapseHight() {
        return collapseHeight;
    }

    public void setCollapseHeight(int collapseHeight) {
        this.collapseHeight = collapseHeight;
        requestLayout();
    }

    public int getCollapseTargetId() {
        return collapseTargetId;
    }

    public void setCollapseTargetId(int collapseTargetId) {
        this.collapseTargetId = collapseTargetId;
        requestLayout();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setOnExpandListener(OnExpandListener expandListener) {
        this.expandListener = expandListener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        refreshScroller();
    }

    public enum Status {
        EXPANDED, COLLAPSED, MOVING
    }
}