package com.whitecloak.statelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StateLayout extends FrameLayout implements View.OnClickListener {

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

    private ActionListener mErrorListener;
    private ActionListener mEmptyListener;

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
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mContentView = addView(mContentViewIdRes, mContentLayoutRes, 0);
        mLoadingView = addView(mLoadingViewIdRes, mLoadingLayoutRes, R.layout.view_loading);
        mErrorView = addView(mErrorViewIdRes, mErrorLayoutRes, R.layout.view_error);
        mEmptyView = addView(mEmptyViewIdRes, mEmptyLayoutRes, R.layout.view_empty);

        setState(mDefaultState);
    }

    private View addView(@IdRes int idRes, @LayoutRes int layoutRes, @LayoutRes int defLayoutRes) {
        View view;

        if (idRes > 0) {
            view = findViewById(idRes);
        } else if (layoutRes > 0) {
            view = LayoutInflater.from(getContext()).inflate(layoutRes, this, false);
            addView(view);
        } else {
            view = LayoutInflater.from(getContext()).inflate(defLayoutRes, this, false);
            addView(view);
        }

        return view;
    }

    private void showView(@Nullable View view, boolean show) {
        if (view != null) {
            view.setVisibility(show ? VISIBLE : GONE);
        }
    }

    /* View.OnClickListener */

    @Override
    public void onClick(View view) {
        if (mEmptyListener != null && mViewState == VIEW_EMPTY) {
            mEmptyListener.onAction();
        }

        if (mErrorListener != null && mViewState == VIEW_ERROR) {
            mErrorListener.onAction();
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

        if (mViewState == VIEW_ERROR || mViewState == VIEW_EMPTY) {
            setOnClickListener(this);
        } else {
            setOnClickListener(null);
        }
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
        TextView textView = findViewById(R.id.text_empty);
        if (textView != null) {
            textView.setText(message);
        }
    }

    public void showError(@NonNull String message) {
        showError();
        TextView textView = findViewById(R.id.text_error);
        if (textView != null) {
            textView.setText(message);
        }
    }

    public void setErrorListener(@Nullable ActionListener listener) {
        mErrorListener = listener;
    }

    public void setEmptyListener(@Nullable ActionListener listener) {
        mEmptyListener = listener;
    }

    public void setErrorEmptyListener(@Nullable ActionListener listener) {
        mErrorListener = listener;
        mEmptyListener = listener;
    }

    public interface ActionListener {

        void onAction();
    }
}
