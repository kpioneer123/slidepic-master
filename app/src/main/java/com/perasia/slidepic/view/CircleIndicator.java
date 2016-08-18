package com.perasia.slidepic.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;

import android.support.annotation.AnimatorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;



public class CircleIndicator extends LinearLayout {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;
    private ViewPager mViewpager;
    private int mIndicatorMargin = -1;
    private int mIndicatorWidth = -1;
    private int mIndicatorHeight = -1;
    private int mAnimatorResId = com.perasia.slidepic.R.animator.scale_with_alpha;
    private int mAnimatorReverseResId = 0;
    private int mIndicatorBackgroundResId = com.perasia.slidepic.R.drawable.white_radius;
    private int mIndicatorUnselectedBackgroundResId = com.perasia.slidepic.R.drawable.white_radius;
    private Animator mAnimatorOut;
    private Animator mAnimatorIn;
    private Animator mImmediateAnimatorOut;
    private Animator mImmediateAnimatorIn;
    private int datassize=0;
    private  int  oldPosition;
    private int mLastPosition = -1;

    public CircleIndicator(Context context) {
        super(context);
        init(context, null);

    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleTypedArray(context, attrs);
        checkIndicatorConfig(context);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.perasia.slidepic.R.styleable.CircleIndicator);
        mIndicatorWidth =
                typedArray.getDimensionPixelSize(com.perasia.slidepic.R.styleable.CircleIndicator_ci_width, -1);
        mIndicatorHeight =
                typedArray.getDimensionPixelSize(com.perasia.slidepic.R.styleable.CircleIndicator_ci_height, -1);
        mIndicatorMargin =
                typedArray.getDimensionPixelSize(com.perasia.slidepic.R.styleable.CircleIndicator_ci_margin, -1);

        mAnimatorResId = typedArray.getResourceId(com.perasia.slidepic.R.styleable.CircleIndicator_ci_animator,
                com.perasia.slidepic.R.animator.scale_with_alpha);
        mAnimatorReverseResId =
                typedArray.getResourceId(com.perasia.slidepic.R.styleable.CircleIndicator_ci_animator_reverse, 0);
        mIndicatorBackgroundResId =
                typedArray.getResourceId(com.perasia.slidepic.R.styleable.CircleIndicator_ci_drawable,
                        com.perasia.slidepic.R.drawable.white_radius);
        mIndicatorUnselectedBackgroundResId =
                typedArray.getResourceId(com.perasia.slidepic.R.styleable.CircleIndicator_ci_drawable_unselected,
                        mIndicatorBackgroundResId);

        int orientation = typedArray.getInt(com.perasia.slidepic.R.styleable.CircleIndicator_ci_orientation, -1);
        setOrientation(orientation == VERTICAL ? VERTICAL : HORIZONTAL);

        int gravity = typedArray.getInt(com.perasia.slidepic.R.styleable.CircleIndicator_ci_gravity, -1);
        setGravity(gravity >= 0 ? gravity : Gravity.CENTER);

        typedArray.recycle();
    }

    /**
     * Create and configure Indicator in Java code.
     */
    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin) {
        configureIndicator(indicatorWidth, indicatorHeight, indicatorMargin,
                com.perasia.slidepic.R.animator.scale_with_alpha, 0, com.perasia.slidepic.R.drawable.white_radius, com.perasia.slidepic.R.drawable.white_radius);
    }

    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin,
                                   @AnimatorRes int animatorId, @AnimatorRes int animatorReverseId,
                                   @DrawableRes int indicatorBackgroundId,
                                   @DrawableRes int indicatorUnselectedBackgroundId) {

        mIndicatorWidth = indicatorWidth;
        mIndicatorHeight = indicatorHeight;
        mIndicatorMargin = indicatorMargin;

        mAnimatorResId = animatorId;
        mAnimatorReverseResId = animatorReverseId;
        mIndicatorBackgroundResId = indicatorBackgroundId;
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedBackgroundId;

        checkIndicatorConfig(getContext());
    }

    private void checkIndicatorConfig(Context context) {
        mIndicatorWidth = (mIndicatorWidth < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
                (mIndicatorHeight < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
                (mIndicatorMargin < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;

        mAnimatorResId = (mAnimatorResId == 0) ? com.perasia.slidepic.R.animator.scale_with_alpha : mAnimatorResId;

        mAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut.setDuration(0);

        mAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn.setDuration(0);

        mIndicatorBackgroundResId = (mIndicatorBackgroundResId == 0) ? com.perasia.slidepic.R.drawable.white_radius
                : mIndicatorBackgroundResId;
        mIndicatorUnselectedBackgroundResId =
                (mIndicatorUnselectedBackgroundResId == 0) ? mIndicatorBackgroundResId
                        : mIndicatorUnselectedBackgroundResId;
    }

    private Animator createAnimatorOut(Context context) {
        return AnimatorInflater.loadAnimator(context, mAnimatorResId);
    }

    private Animator createAnimatorIn(Context context) {
        Animator animatorIn;
       if (mAnimatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorResId);
           animatorIn.setInterpolator(new ReverseInterpolator());
       } else {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId);
        }
        return animatorIn;
    }

    public void setViewPager(ViewPager viewPager, int num) {
        Log.d("5551",num+"");
        mViewpager = viewPager;






        if (mViewpager != null && mViewpager.getAdapter() != null) {
            oldPosition = num-1;
            mIndicatorBackgroundResId= com.perasia.slidepic.R.drawable.white_radius;
            createIndicators(num);
            this.datassize =num;

            mViewpager.removeOnPageChangeListener(mInternalPageChangeListener);
            mViewpager.addOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewpager.getCurrentItem());
        }
    }

    private final ViewPager.OnPageChangeListener mInternalPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override public void onPageSelected(int position) {

            if (mViewpager.getAdapter() == null || mViewpager.getAdapter().getCount() <= 0) {
                return;
            }

            if (mAnimatorIn.isRunning()) {
                mAnimatorIn.end();
                mAnimatorIn.cancel();
            }

            if (mAnimatorOut.isRunning()) {
                mAnimatorOut.end();
                mAnimatorOut.cancel();
            }

            View currentIndicator;
           int  index = (position) % datassize;

            Log.d("555",position+"");
            Log.d("555",index+"");
//            if (mLastPosition >= 0 && (currentIndicator = getChildAt(mLastPosition)) != null) {
//                currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
//                mAnimatorIn.setTarget(currentIndicator);
//                mAnimatorIn.start();
//            }
//
//            View selectedIndicator = getChildAt(position);
//            if (selectedIndicator != null) {
//                selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId);
//                mAnimatorOut.setTarget(selectedIndicator);
//                mAnimatorOut.start();
//            }
//            mLastPosition = position;



            if (oldPosition >= 0 && (currentIndicator = getChildAt(oldPosition)) != null) {
                currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
                mAnimatorIn.setTarget(currentIndicator);
                mAnimatorIn.start();
            }


            for (int i = 0; i <datassize; i++) {


                    if (index != i) {
                        currentIndicator = getChildAt(oldPosition);
                        currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
                        mAnimatorIn.setTarget(currentIndicator);
                        mAnimatorIn.start();
                    }else{

                        View selectedIndicator = getChildAt(index);
                        if (selectedIndicator != null) {

                            Log.d("88888",index+"");
                            selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId);
                            mAnimatorOut.setTarget(selectedIndicator);
                            mAnimatorOut.start();
                        }
                    }

                }




              oldPosition = (position) % datassize;

        }

        @Override public void onPageScrollStateChanged(int state) {
        }
    };


    /**
     * @deprecated User ViewPager addOnPageChangeListener
     */
    @Deprecated public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (mViewpager == null) {
            throw new NullPointerException("can not find Viewpager , setViewPager first");
        }
        mViewpager.removeOnPageChangeListener(onPageChangeListener);
        mViewpager.addOnPageChangeListener(onPageChangeListener);
    }

    private void createIndicators(int count ) {
        removeAllViews();

        if (count <= 0) {
            return;
        }
      //  int currentItem = mViewpager.getCurrentItem();

        for (int i = 0; i < count; i++) {
            Log.d("555",i+"");
            if (i == 0) {

                addIndicator(mIndicatorBackgroundResId, mImmediateAnimatorOut);
            } else {
                addIndicator(mIndicatorUnselectedBackgroundResId, mImmediateAnimatorIn);
            }
        }

    }

    private void addIndicator(@DrawableRes int backgroundDrawableId, Animator animator) {

        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }


    View Indicator = new View(getContext());
    Indicator.setBackgroundResource(backgroundDrawableId);
    addView(Indicator, mIndicatorWidth, mIndicatorHeight);
    LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();
    lp.leftMargin = mIndicatorMargin;
    lp.rightMargin = mIndicatorMargin;
    Indicator.setLayoutParams(lp);

    animator.setTarget(Indicator);

    animator.start();



    }

    private class ReverseInterpolator implements Interpolator {
        @Override public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
