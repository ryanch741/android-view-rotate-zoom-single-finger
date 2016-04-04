package com.example.testimg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SingleFingerView extends LinearLayout {
    private ImageView mView;
    private ImageView mPushView;
    private float _1dp;
    private boolean mCenterInParent = true;
    private Drawable mImageDrawable, mPushImageDrawable;
    private float mImageHeight, mImageWidth, mPushImageHeight, mPushImageWidth;
    private int mLeft = 0, mTop = 0;


    public SingleFingerView(Context context) {
        this(context, null, 0);
    }

    public SingleFingerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleFingerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._1dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        this.parseAttr(context, attrs);
        View mRoot = View.inflate(context, R.layout.test_image_view, null);
        addView(mRoot, -1, -1);
        mPushView = (ImageView) mRoot.findViewById(R.id.push_view);
        mView = (ImageView) mRoot.findViewById(R.id.view);
        mPushView.setOnTouchListener(new PushBtnTouchListener(mView));
        mView.setOnTouchListener(new ViewOnTouchListener(mPushView));
        initForSingleFingerView();
    }


    private void parseAttr(Context context, AttributeSet attrs) {
        if (null == attrs) return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SingleFingerView);
        if (a != null) {
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.SingleFingerView_centerInParent) {
                    this.mCenterInParent = a.getBoolean(attr, true);
                } else if (attr == R.styleable.SingleFingerView_image) {
                    this.mImageDrawable = a.getDrawable(attr);
                } else if (attr == R.styleable.SingleFingerView_image_height) {
                    this.mImageHeight = a.getDimension(attr, 200 * _1dp);
                } else if (attr == R.styleable.SingleFingerView_image_width) {
                    this.mImageWidth = a.getDimension(attr, 200 * _1dp);
                } else if (attr == R.styleable.SingleFingerView_push_image) {
                    this.mPushImageDrawable = a.getDrawable(attr);
                } else if (attr == R.styleable.SingleFingerView_push_image_width) {
                    this.mPushImageWidth = a.getDimension(attr, 50 * _1dp);
                } else if (attr == R.styleable.SingleFingerView_push_image_height) {
                    this.mPushImageHeight = a.getDimension(attr, 50 * _1dp);
                } else if (attr == R.styleable.SingleFingerView_left) {
                    this.mLeft = (int) a.getDimension(attr, 0 * _1dp);
                } else if (attr == R.styleable.SingleFingerView_top) {
                    this.mTop = (int) a.getDimension(attr, 0 * _1dp);
                }
            }
        }
    }

    private void initForSingleFingerView() {
       /* ViewTreeObserver vto2 = mView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                FrameLayout.LayoutParams viewLP = (FrameLayout.LayoutParams) mView.getLayoutParams();
                FrameLayout.LayoutParams pushViewLP = (FrameLayout.LayoutParams) mPushView.getLayoutParams();
                pushViewLP.width = (int) mPushImageWidth;
                pushViewLP.height = (int) mPushImageHeight;
                pushViewLP.leftMargin = (viewLP.leftMargin + mView.getWidth()) - mPushView.getWidth() / 2;
                pushViewLP.topMargin = (viewLP.topMargin + mView.getHeight()) - mPushView.getWidth() / 2;
                mPushView.setLayoutParams(pushViewLP);
            }
        });*/
    }

    private void setViewToAttr(int pWidth, int pHeight) {
        if (null != mImageDrawable) {
            this.mView.setBackgroundDrawable(mImageDrawable);
        }
        if (null != mPushImageDrawable) {
            this.mPushView.setBackgroundDrawable(mPushImageDrawable);
        }
        FrameLayout.LayoutParams viewLP = (FrameLayout.LayoutParams) this.mView.getLayoutParams();
        viewLP.width = (int) mImageWidth;
        viewLP.height = (int) mImageHeight;
        int left = 0, top = 0;
        if (mCenterInParent) {
            left = pWidth / 2 - viewLP.width / 2;
            top = pHeight / 2 - viewLP.height / 2;
        } else {
            if (mLeft > 0) left = mLeft;
            if (mTop > 0) top = mTop;
        }
        viewLP.leftMargin = left;
        viewLP.topMargin = top;
        this.mView.setLayoutParams(viewLP);

        FrameLayout.LayoutParams pushViewLP = (FrameLayout.LayoutParams) mPushView.getLayoutParams();
        pushViewLP.width = (int) mPushImageWidth;
        pushViewLP.height = (int) mPushImageHeight;
        pushViewLP.leftMargin = (int) (viewLP.leftMargin + mImageWidth - mPushImageWidth / 2);
        pushViewLP.topMargin = (int) (viewLP.topMargin + mImageHeight - mPushImageHeight / 2);
        mPushView.setLayoutParams(pushViewLP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setParamsForView(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean hasSetParamsForView = false;

    private void setParamsForView(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (null != layoutParams && !hasSetParamsForView) {
            System.out.println("AAAAAAAAAAAAAAAAAAA setParamsForView");
            hasSetParamsForView = true;
            int width;
            if ((getLayoutParams().width == LayoutParams.MATCH_PARENT)) {
                width = MeasureSpec.getSize(widthMeasureSpec);
            } else {
                width = getLayoutParams().width;
            }
            int height;
            if ((getLayoutParams().height == LayoutParams.MATCH_PARENT)) {
                height = MeasureSpec.getSize(heightMeasureSpec);
            } else {
                height = getLayoutParams().height;
            }
            setViewToAttr(width, height);
        }
    }
}
