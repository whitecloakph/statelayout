package com.whitecloak.statelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StateLayout extends FrameLayout {

    @IntDef({VIEW_CONTENT, VIEW_LOADING, VIEW_ERROR, VIEW_EMPTY, VIEW_NOTHING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewState {
    }

    public static final int VIEW_CONTENT = 0;
    public static final int VIEW_LOADING = 1;
    public static final int VIEW_ERROR = 2;
    public static final int VIEW_EMPTY = 3;
    public static final int VIEW_NOTHING = 4;

    @ViewState
    private int mViewState = -1;
    private int mDefaultState;

    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    private int mContentViewIdRes;
    private int mLoadingViewIdRes;
    private int mErrorViewIdRes;
    private int mEmptyViewIdRes;

    private int mContentLayoutRes;
    private int mLoadingLayoutRes;
    private int mErrorLayoutRes;
    private int mEmptyLayoutRes;

    private RefreshListener mRefreshListener;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateLayout, defStyleAttr, 0);

        try {
            mDefaultState = a.getInt(R.styleable.StateLayout_initialState, VIEW_CONTENT);

            mContentViewIdRes = a.getResourceId(R.styleable.StateLayout_contentView, 0);
            mLoadingViewIdRes = a.getResourceId(R.styleable.StateLayout_loadingView, 0);
            mErrorViewIdRes = a.getResourceId(R.styleable.StateLayout_errorView, 0);
            mEmptyViewIdRes = a.getResourceId(R.styleable.StateLayout_emptyView, 0);

            mContentLayoutRes = a.getResourceId(R.styleable.StateLayout_contentLayout, 0);
            mLoadingLayoutRes = a.getResourceId(R.styleable.StateLayout_loadingLayout, 0);
            mErrorLayoutRes = a.getResourceId(R.styleable.StateLayout_errorLayout, 0);
            mEmptyLayoutRes = a.getResourceId(R.styleable.StateLayout_emptyLayout, 0);
        } finally {
            a.recycle();
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewState == VIEW_EMPTY || mViewState == VIEW_ERROR || mViewState == VIEW_NOTHING) {
                    if (mRefreshListener != null) {
                        mRefreshListener.onRefresh();
                    }
                }
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mContentView = addView(mContentViewIdRes, mContentLayoutRes, 0);
        mLoadingView = addView(mLoadingViewIdRes, mLoadingLayoutRes, R.layout.view_loading);
        mErrorView = addView(mErrorViewIdRes, mErrorLayoutRes, R.layout.view_error);
        mEmptyView = addView(mEmptyViewIdRes, mEmptyLayoutRes, R.layout.view_empty);

        setState(mDefaultState);

        showError2(false);
        showEmpty2(false);
    }

    private View addView(@IdRes int idRes, @LayoutRes int layoutRes, @LayoutRes int defLayoutRes) {
        View view;

        if (idRes > 0) {
            view = findViewById(idRes);
        } else if (layoutRes > 0) {
            view = inflate(getContext(), layoutRes, null);
            addView(view);
        } else {
            view = inflate(getContext(), defLayoutRes, null);
            addView(view, new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        return view;
    }

    private void showView(@Nullable View view, boolean show) {
        if (view != null) {
            view.setVisibility(show ? VISIBLE : GONE);
        }
    }

    @SuppressWarnings("unused")
    public int getState() {
        return mViewState;
    }

    public void setState(@ViewState int state) {
        if (mViewState == state) {
            return;
        }

        mViewState = state;

        showView(mContentView, state == VIEW_CONTENT);
        showView(mLoadingView, state == VIEW_LOADING);
        showView(mErrorView, state == VIEW_ERROR);
        showView(mEmptyView, state == VIEW_EMPTY);
    }

    public void showContent() {
        setState(VIEW_CONTENT);
    }

    public void showLoading() {
        setState(VIEW_LOADING);
    }

    public void showError() {
        setState(VIEW_ERROR);
    }

    public void showEmpty() {
        setState(VIEW_EMPTY);
    }

    public void showNothing() {
        setState(VIEW_NOTHING);
    }

    public void showEmpty(@NonNull String message) {
        showEmpty();
        TextView textView = (TextView) findViewById(R.id.text_empty);
        if (textView != null) {
            textView.setText(message);
        }
    }

    public void showEmpty2(boolean show) {
        TextView textView = (TextView) findViewById(R.id.text_empty2);
        if (textView != null) {
            textView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void showError(@NonNull String message) {
        showError();
        TextView textView = (TextView) findViewById(R.id.text_error);
        if (textView != null) {
            textView.setText(message);
        }
    }

    public void showError2(boolean show) {
        TextView textView = (TextView) findViewById(R.id.text_error2);
        if (textView != null) {
            textView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void setRefreshListener(@Nullable RefreshListener refreshListener) {
        mRefreshListener = refreshListener;
        showEmpty2(true);
        showError2(true);
    }

    public interface RefreshListener {

        void onRefresh();
    }
}
