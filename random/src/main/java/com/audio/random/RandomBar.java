package com.audio.random;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @author Lix
 * @date 2019-12-29 2:37
 */
public class RandomBar extends View implements Runnable {
    public static final int DEFAULT_SIZE = 50;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 每个随机条画笔
     */
    private Paint mBarPaint;
    /**
     * 绘制矩形
     */
    private Rect mRandomBarRect;
    /**
     * randomBar 偏移量
     */
    private int mRandomBarOffset;
    /**
     * 播放时间间隔
     */
    private float mDelayTime;
    /**
     * 随机条的个数
     */
    private int mRandomBarNum;
    /**
     * 随机条的宽度
     */
    private int mRandomBarWidth;
    /**
     * 随机条的高度
     */
    private int mRandomBarHeight;
    /**
     * 每个随机条的颜色
     */
    private int mRandomBarColor;
    /**
     * 当前View宽度
     */
    private int mViewWidth;
    /**
     * 随机条数据
     */
    private float[] mRandomBarCurrentHeight;
    /**
     * 是否正在进行动画播放
     */
    private boolean isStart;

    public RandomBar(Context context) {
        this(context, null);
    }

    public RandomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
        initPaint();
    }

    private void initPaint() {
        mBarPaint = new Paint();
        mRandomBarRect = new Rect();
        mBarPaint.setColor(mRandomBarColor);
        mBarPaint.setAntiAlias(true);
        mBarPaint.setStyle(Paint.Style.FILL);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, com.audio.random.R.styleable.RandomBar);
        mDelayTime = typedArray.getFloat(com.audio.random.R.styleable.RandomBar_RandomBarDelayTime, 200f);
        mRandomBarNum = typedArray.getInt(com.audio.random.R.styleable.RandomBar_RandomBarNum, 8);
        mRandomBarOffset = typedArray.getInt(R.styleable.RandomBar_RandomBarOffset, 3);
        mRandomBarWidth = typedArray.getInt(com.audio.random.R.styleable.RandomBar_RandomBarWidth, 8);
        mRandomBarHeight = typedArray.getInt(com.audio.random.R.styleable.RandomBar_RandomBarHeight, 8);
        mRandomBarColor = typedArray.getColor(com.audio.random.R.styleable.RandomBar_RandomBarColor, ContextCompat.getColor(mContext, com.audio.random.R.color.white));
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setRandomNum(canvas);
        new Thread(this).run();
    }

    /**
     * 设置随机数
     *
     * @param canvas
     */
    private void setRandomNum(Canvas canvas) {
        if (mRandomBarCurrentHeight != null) {
            for (int i = 0; i < mRandomBarNum; i++) {
                int random = (int) (Math.random() * 50);
                canvas.drawRect((float) (mViewWidth * 0.4 / 2 + mRandomBarWidth * i + mRandomBarOffset), mRandomBarCurrentHeight[i] + random,
                        (float) (mViewWidth * 0.4 / 2 + mRandomBarWidth * (i + 1)), mRandomBarHeight, mBarPaint);
            }
        } else {
            for (int i = 0; i < mRandomBarNum; i++) {
                float f = (float) (Math.random() * mRandomBarHeight);
                canvas.drawRect((float) (mViewWidth * 0.4 / 2 + mRandomBarWidth * i + mRandomBarOffset), f,
                        (float) (mViewWidth * 0.4 / 2 + mRandomBarWidth * (i + 1)), mRandomBarHeight, mBarPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = getMeasuredWidth();
        mRandomBarHeight = getMeasuredHeight();
        mRandomBarWidth = (int) (mViewWidth * 0.6 / mRandomBarNum);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = resolveSize(DEFAULT_SIZE, widthMeasureSpec);
        int height = resolveSize(DEFAULT_SIZE, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 获取测量大小
     *
     * @param size
     * @param measureSpec
     * @return
     */
    public static int resolveSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            default:

                break;
        }
        return result;
    }


    public float[] getRandomBarCurrentHeight() {
        return mRandomBarCurrentHeight;
    }

    public void setRandomBarCurrentHeight(float[] randomBarCurrentHeight) {
        mRandomBarCurrentHeight = randomBarCurrentHeight;
        //让View延时mDelayTime毫秒再重绘
        postInvalidate();
    }

    public int getRandomBarNum() {
        return mRandomBarNum;
    }

    /**
     * 数量展示
     *
     * @param randomBarNum
     */
    public void setRandomBarNum(int randomBarNum) {
        mRandomBarNum = randomBarNum;
    }

    public int getRandomBarOffset() {
        return mRandomBarOffset;
    }

    /**
     * randomBar 偏移量
     *
     * @param randomBarOffset
     */
    public void setRandomBarOffset(int randomBarOffset) {
        mRandomBarOffset = randomBarOffset;
    }

    /**
     * 动画
     */
    @Override
    public void run() {
        if (isStart) {
            try {
                Thread.sleep((long) mDelayTime);
                postInvalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isStart = false;
    }

    public void Start() {
        isStart = true;
        postInvalidate();
    }
}
