package com.mr_qual1ty.clock.timerclock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Calendar;

/**
 * 自定义时钟View
 */
public class TimerClock extends SurfaceView implements SurfaceHolder.Callback, Runnable {


    private boolean isBlackStatus = false; // false 为白天，生之钟


    // 默认半径
    private static final int DEFAULT_RADIUS = 200;

    // 圆环宽度,默认
    private static final int DEFAULT_RING = 20;

    private static final String CIRCLE_LIVE_COLOR = "#d8d8d8"; // live 圆环 color
    private static final String CIRCLE_DIED_COLOR = "#1b1b1b"; // died 圆环 color


    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean isRun;

    // 圆和刻度的画笔
    private Paint mPaint;


    // 指针画笔
    private Paint mPointerPaint;

    private int mPaintColor;// 指针，圆心点的颜色，保持一致

    private String mScaleColor;

    private Paint mTextPaint; // 文字画笔


    private String mRingColor = CIRCLE_LIVE_COLOR;

    // 画布的宽高
    private int mCanvasWidth, mCanvasHeight;
    // 时钟半径
    private int mRadius = DEFAULT_RADIUS;
    // 秒针长度
    private int mSecondPointerLength;
    // 分针长度
    private int mMinutePointerLength;
    // 时针长度
    private int mHourPointerLength;
    // 时刻度长度
    private int mHourDegreeLength;
    // 时刻度长度(12,3,6,9比较宽的刻度)
    private int mHourDegreeLengthBig;
    // 圆环宽度
    private int mRingWidth = DEFAULT_RING;

    private Thread mThread;

    // 时钟显示的时、分、秒
    private int mHour, mMinute, mSecond;

    public TimerClock(Context context) {
        this(context, null);
    }

    public TimerClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mThread = new Thread(this);
//        switchWhiteStatus();
        switchBlackStatus();
        initCalendar();
        initPaint();
//        setFocusable(true);
//        setFocusableInTouchMode(true);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPointerPaint = new Paint();

        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mPointerPaint.setAntiAlias(true);
        mPointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPointerPaint.setTextSize(22);
        mPointerPaint.setTextAlign(Paint.Align.CENTER);

        mTextPaint = new Paint();
        mPointerPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    private void initCalendar() {
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);
        mSecond = Calendar.getInstance().get(Calendar.SECOND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth, desiredHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = widthSize;
        } else {
            desiredWidth = mRadius * 2 + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = Math.min(widthSize, desiredWidth);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize;
        } else {
            desiredHeight = mRadius * 2 + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = Math.min(heightSize, desiredHeight);
            }
        }

        // +20是为了设置默认的20px的内边距，因为绘制时钟的圆的画笔设置的宽度是20px
        setMeasuredDimension(mCanvasWidth = desiredWidth, mCanvasHeight = desiredHeight);

        mRadius = (int) (Math.min(desiredWidth - getPaddingLeft() - getPaddingRight(),
                desiredHeight - getPaddingTop() - getPaddingBottom()) * 1.0f / 2);
        calculateLengths();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRun = true;
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRun = false;
    }

    @Override
    public void run() {
        long start, end;
        for (; ; ) {
            if (!isRun)
                break;
            start = System.currentTimeMillis();
            draw();
            logic();
            end = System.currentTimeMillis();

            try {
                if (end - start < 1000) {
                    Thread.sleep(1000 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 逻辑
     */
    private void logic() {
        mSecond++;
        if (mSecond == 60) {
            mSecond = 0;
            mMinute++;
            if (mMinute == 60) {
                mMinute = 0;
                mHour++;
                if (mHour == 24) {
                    mHour = 0;
                }
            }
        }

    }

    /**
     * 绘制
     */
    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas == null)
                return;
            mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            mCanvas.drawColor(isBlackStatus ? Color.parseColor("#333333") : Color.WHITE);
            drawCircle();
            drawScale();
            drawPoint();
        } catch (Exception e) {
        } finally {
            if (mCanvas == null)
                return;
            mHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    /**
     * 绘制指针
     */
    private void drawPoint() {
        mPointerPaint.setColor(Color.parseColor(mScaleColor));
        Path path = new Path();
        path.moveTo(0, 0);
        int[] hourPointerCoordinates = getPointerCoordinates(mHourPointerLength);
        path.lineTo(hourPointerCoordinates[0], hourPointerCoordinates[1]);
        path.lineTo(hourPointerCoordinates[2], hourPointerCoordinates[3]);
        path.lineTo(hourPointerCoordinates[4], hourPointerCoordinates[5]);
        path.close();
        mCanvas.save();
        mCanvas.rotate(180 + mHour % 12 * 30 + mMinute * 1.0f / 60 * 30);
        mCanvas.drawPath(path, mPointerPaint);
        mCanvas.restore();
        //绘制分针
        path.reset();
        path.moveTo(0, 0);
        int[] minutePointerCoordinates = getPointerCoordinates(mMinutePointerLength);
        path.lineTo(minutePointerCoordinates[0], minutePointerCoordinates[1]);
        path.lineTo(minutePointerCoordinates[2], minutePointerCoordinates[3]);
        path.lineTo(minutePointerCoordinates[4], minutePointerCoordinates[5]);
        path.close();
        mCanvas.save();
        mCanvas.rotate(180 + mMinute * 6);
        mCanvas.drawPath(path, mPointerPaint);
        mCanvas.restore();
        //绘制秒针
        path.reset();
        path.moveTo(0, 0);
        int[] secondPointerCoordinates = getPointerCoordinates(mSecondPointerLength);
        path.lineTo(secondPointerCoordinates[0], secondPointerCoordinates[1]);
        path.lineTo(secondPointerCoordinates[2], secondPointerCoordinates[3]);
        path.lineTo(secondPointerCoordinates[4], secondPointerCoordinates[5]);
        path.close();
        mCanvas.save();
        mCanvas.rotate(180 + mSecond * 6);
        mCanvas.drawPath(path, mPointerPaint);
        mCanvas.restore();
    }

    /**
     * 画出圆环
     */
    private void drawCircle() {
        mCanvas.translate(getPaddingStart() - getPaddingEnd() + mCanvasWidth * 1f / 2, getPaddingTop() - getPaddingBottom() + mCanvasHeight * 1f / 2);
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setColor(Color.parseColor(mRingColor));
        mCanvas.drawCircle(0, 0, mRadius, mPaint);
    }

    /**
     * 画出小时的刻度，12个小刻度，其中12，3，6，9，为加粗的刻度
     */
    private void drawScale() {
        int rotate = 360 / 12;
        int bigRotate = 360 / 4;
        float distance = 10f + mRingWidth; // 刻度距离边界，加上的圆环的内宽
        int nums = bigRotate / rotate;
        mPaint.setStrokeWidth(4f);
        mPaint.setColor(Color.parseColor(mScaleColor));
        for (int i = 0; i < 4; i++) {
            //画宽的刻度
            mCanvas.drawLine(0, mRadius - distance, 0, mRadius - mHourDegreeLengthBig - distance, mPaint);
            mCanvas.rotate(bigRotate);
        }

        mPaint.setStrokeWidth(2f);
        for (int i = 0; i < 12; i++) {
            // 画窄的刻度
            if (i % nums != 0)
                mCanvas.drawLine(0, mRadius - distance, 0, mRadius - mHourDegreeLength - distance, mPaint);
            mCanvas.rotate(rotate);
        }


        mTextPaint.setTextSize(mRadius * 1f / 6);
        mTextPaint.setColor(mPaintColor);

        Rect textBound = new Rect();//创建一个矩形
        distance = mRadius * 9f / 20; // 文字距离表盘的边距
        mPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                drawNum(mCanvas, i * bigRotate, 12 + "", mTextPaint, distance);
            } else {
                drawNum(mCanvas, i * bigRotate, i * 3 + "", mTextPaint, distance);
            }
        }

        mCanvas.drawCircle(0, 0, mRadius * 1f / 20, mTextPaint);
    }

    /**
     * 画成正向的数字
     *
     * @param canvas
     * @param degree
     * @param text
     * @param paint
     */
    private void drawNum(Canvas canvas, int degree, String text, Paint paint, float distance) {
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        canvas.rotate(degree);
        canvas.translate(0, distance - mRadius);//这里的50是坐标中心距离时钟最外边框的距离，当然你可以根据需要适当调节
        canvas.rotate(-degree);
        canvas.drawText(text, -textBound.width() / 2,
                textBound.height() / 2, paint);
        canvas.rotate(degree);
        canvas.translate(0, mRadius - distance);
        canvas.rotate(-degree);
    }

    /**
     * 计算指针和刻度长度
     */
    private void calculateLengths() {
        mHourDegreeLength = (int) (mRadius * 1.0f / 10);
        mHourDegreeLengthBig = (int) (mRadius * 1.0f / 5);

        // hour : minute : second = 1 : 1.25 : 1.5
        mHourPointerLength = (int) (mRadius * 1.0 / 2);
        mMinutePointerLength = (int) (mHourPointerLength * 1.25f);
        mSecondPointerLength = (int) (mHourPointerLength * 1.5f);
    }

    /**
     * 切换为死之钟
     */
    public void switchBlackStatus() {
        isBlackStatus = true; //切换为死之钟
        mRingColor = CIRCLE_DIED_COLOR;
        mPaintColor = Color.WHITE;
        mScaleColor = "#ffffff";
    }

    public void switchWhiteStatus() {
        isBlackStatus = false; //切换为生之钟
        mRingColor = CIRCLE_LIVE_COLOR;
        mPaintColor = Color.BLACK;
        mScaleColor = CIRCLE_LIVE_COLOR;
    }


    /**
     * 获取指针坐标
     *
     * @param pointerLength 指针长度
     * @return int[]{x1,y1,x2,y2,x3,y3}
     */
    private int[] getPointerCoordinates(int pointerLength) {
        int y = (int) (pointerLength * 3.0f / 4);
        int x = (int) (y * Math.tan(Math.PI / 180 * 5));
        return new int[]{-x, y, 0, pointerLength, x, y};
    }

}
