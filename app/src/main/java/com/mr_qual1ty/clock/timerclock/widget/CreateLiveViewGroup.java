package com.mr_qual1ty.clock.timerclock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.mr_qual1ty.clock.timerclock.DensityUtils;

/**
 * Created by mr_qual1ty on 2017/3/23.
 */


/**
 * 弹出窗口，用来操作位置
 * Created by zy on 2016/8/29.
 */
public class CreateLiveViewGroup extends View {

    /**
     * 绘制按钮的画笔
     */
    private Paint buttonPaint;

    /**
     * 绘制背景的画笔,从透明到完全不透明
     */
    private Paint bgPaint;

    /**
     * 绘制文字的画笔
     */
    private Paint textPaint;
    private Context mContext;

    /**
     * 按钮初始位置，到上屏的距离
     */
    private float maginTop = 1200;

    /**
     * 按钮底边到顶部的高度
     */
    private float maginBottom;

    /**
     * 按钮的高(初始化)
     */
    private float buttonHeight;

    /**
     * 按钮的宽(初始化)
     */
    private float buttonWidth;


    /**
     * 按钮左边的距离
     */
    private float maginLeft;

    /**
     * 按钮右边的距离
     */
    private float maginRight;

    /**
     * 按钮半径
     */
    private float radius;

    /**
     * 画圆角矩形需要的rect
     */
    RectF r1;
    /**
     * 按钮高度变化的变量（椭圆变成圆）
     */
    int button_y = 0;

    /**
     * 按钮高度变化的变量(圆形按钮位移)
     */
    int circle_height_y = 0;

    /**
     * 字体大小
     */
    private float textSize;

    /**
     * 到达位置的top
     */
    private float tagretAddressTop;

    /**
     * 到达位置的bottom
     */
    private float tagretAddressBottom;

    /**
     * 这个位置，高度不再拉伸
     */
    private float noScaleIndex = 824;

    /**
     * 到达位置的left
     */
    private float tagretAddressLeft;
    /**
     * 到达位置的right
     */
    private float tagretAddressRight;

    /**
     * 变成球的rect 的4个点
     */
    private final float circleLeft = 292;
    /**
     * 变成球的4个点
     */
    private final float circleRight = 428;
    /**
     * 变成球的4个点
     */
    private final float circleTop = 688;

    /**
     * 变成球的4个点
     */
    private final float circleBottom = 824;

    private float circlrRadiues;

    private float tagretRadius = 80;


    private int windowWidth;
    private int windowHeight;

    boolean buttonMove = false;
    /**
     * 背景透明度
     */
    int bgAlpha = 0;

    /**
     * 文字透明度
     *
     * @param canvas
     */
    int textAlpha = 255;
    /**
     * 按钮变成球过后移动的纵坐标
     */
    int circle_y = 0;
    /**
     * button消失时候的透明度
     */
    int buttonAlpah = 255;


    /**
     * 文字最终停留的位置
     */
    float textFinalleft = 0;

    /**
     * 文字高度的变动
     */
    float textHeightChanged = 0;

    /**
     * 文本内容
     */
    private String numberStr = "3";

    /**
     * 文字加动画后移动的高度
     */
    private float textAnimationHeight = 0;

    /**
     * 画圆线程开启的flag
     */
    private volatile boolean drawCircleFlag;

    /**
     * 控制圆移动的flag
     */
    private volatile boolean ciclrDistanceFlag = false;

    public void setAnimationOverListener(AnimationOverListener listener) {
        this.listener = listener;
    }

    private AnimationOverListener listener;


    /**
     * 按钮放大缩放的flag
     */
    private volatile boolean blowUpThreadStartFlag = false;

    public CreateLiveViewGroup(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CreateLiveViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CreateLiveViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowWidth = wm.getDefaultDisplay().getWidth();
        windowHeight = wm.getDefaultDisplay().getHeight();
        setMeasuredDimension(windowWidth, windowHeight);

        buttonHeight = DensityUtils.dip2px(40);
        buttonWidth = DensityUtils.dip2px(295);
        radius = DensityUtils.dip2px(90);
        circlrRadiues = DensityUtils.dip2px(15);

        maginTop = windowHeight - DensityUtils.dip2px(270);
        maginLeft = windowWidth / 2 - buttonWidth / 2;
        maginRight = maginLeft + buttonWidth;
        maginBottom = maginTop + buttonHeight;


        tagretAddressTop = DensityUtils.dip2px(220);
        tagretAddressBottom = tagretAddressTop + 136;
        tagretAddressBottom = tagretAddressTop + tagretRadius * 2;
        tagretAddressLeft = windowWidth / 2;
        tagretAddressRight = windowWidth / 2 + tagretRadius * 2;

        /**
         * 文字高度，从最大的圆到小点的圆
         */
        textHeightChanged = maxRadius - normalRadius;
        Log.e("xxxx", "maginLeft= " + maginLeft);
        Log.e("xxxx", "maginTop= " + maginTop);
        Log.e("xxxx", "maginRight= " + maginRight);
        Log.e("xxxx", "maginBottom= " + maginBottom);
    }

//    private int textAnimationChangeHeight = 0;

    private Thread drawCircleThread;
    private Thread blowThread;
    private Thread circlrDistanceThread;
//    private TextDrawThread textThread;

    private final float normalRadius = 68;

    private final float maxRadius = 130;

    private void startDraws() {
        drawCircleThread = new Thread(new CircleChangeRunnable());
        blowThread = new Thread(new CircleBlowupRunnable(normalRadius, maxRadius));
        circlrDistanceThread = new Thread(new CirclrDistanceRunnable());
    }

    public void startAnimation() {
        drawCircleFlag = true;
        drawCircleThread.start();
    }

    private String tag = "xxxxx";

    /**
     * 初始化
     */
    private void init() {
        Log.e(tag, "init run~~~~!!!!");
        initPaint();
        startDraws();
    }


    private void initPaint() {
        initBgPaint();
        initButtonPaint();
        initTextPaint();
    }

    private void initBgPaint() {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAlpha(0);
    }

    private void initButtonPaint() {
        buttonPaint = new Paint();
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setColor(Color.parseColor("#F2BA00"));
        buttonPaint.setAntiAlias(true);
    }

    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#000000"));
        textSize = DensityUtils.sp2px(14);
        textPaint.setTextSize(textSize);
    }

    // 控制第二个线程的start
    private boolean circleDistanceOpen = false;

    // 控制第三个放大线程的start
    private boolean cicleBlowupOpen = false;

    /**
     * 停掉所有线程
     */
    public void releaseResource() {
        drawCircleFlag = false;
        blowUpThreadStartFlag = false;
        ciclrDistanceFlag = false;
        drawCircleThread = null;
        blowThread = null;
        circlrDistanceThread = null;
    }

    String defaultStr = "发起直播";

    int fatanY = 0;

    @Override
    protected void onDraw(final Canvas canvas) {
//        Rect bgRect = new Rect();
//        bgRect.left = getLeft();
//        bgRect.top = getTop();
//        bgRect.right = getRight();
//        bgRect.bottom = getBottom();
//
//        if (bgAlpha < 255) {
//            bgAlpha = bgAlpha + 5;
//        }
//        bgPaint.setAlpha(bgAlpha);
//        canvas.drawRect(bgRect, bgPaint);

        // 移动到这个点就得变成圆去往上面移动了
        r1 = new RectF();
        r1.left = maginLeft + button_y;
        r1.right = maginRight - button_y;
        r1.top = maginTop - button_y;
        r1.bottom = maginBottom - button_y / 2;


        float circle_point_x = r1.right - r1.left;
        float circle_point_y = r1.bottom - r1.top;

        if (circle_point_x == circle_point_y || circle_point_x < circle_point_y) {
            // TODO 这是button 椭圆形→圆形
            if (drawCircleThread != null) {
                drawCircleThread.interrupt();
                buttonMove = false;
                drawCircleFlag = false;
            }
            //开始画圆啦
            loge("maginBottom - circle_point_y - circle_height_y =" + (maginBottom - circle_point_y - circle_height_y));
            if (maginBottom - circle_point_y - circle_height_y > tagretAddressTop) {
                ciclrDistanceFlag = true;
                //开启第二个线程
                if (!circleDistanceOpen) {
                    circlrDistanceThread.start();
                    circleDistanceOpen = true;
                }
                if (maginBottom - circle_point_y - circle_height_y > tagretAddressBottom) {
                    loge("tagretAddressBottom=" + tagretAddressBottom);
                    canvas.drawCircle(tagretAddressLeft, maginBottom - circle_point_y - circle_height_y, circle_point_y / 2, buttonPaint);
                } else {
                    fatanY = fatanY + 30;
                    canvas.drawCircle(tagretAddressLeft, maginBottom - circle_point_y - circle_height_y + fatanY, circle_point_y / 2, buttonPaint);
                    loge("圆反弹");
                }
            } else {
//                canvas.drawCircle(tagretAddressLeft, tagretAddressTop + circle_point_y / 2, circle_point_y / 2, buttonPaint);
                ciclrDistanceFlag = false;
                if (!cicleBlowupOpen) {
                    circlrDistanceThread.interrupt();
                    loge("开始第三个线程");
                    blowThread.start();
                    cicleBlowupOpen = true;
                    blowUpThreadStartFlag = true;
                }
                if (buttonAlphaFlag) {
                    loge("buttonAlpha = " + buttonAlpah);
                    if (buttonAlpah >= 35) {
                        buttonAlpah = buttonAlpah - 20;
                    } else if (buttonAlpah > 0 && buttonAlpah < 35) {
                        buttonAlpah--;
                    }
                    buttonPaint.setAlpha(buttonAlpah);
                }

                canvas.drawCircle(tagretAddressLeft, tagretAddressTop + circle_point_y / 2, tagretRadius, buttonPaint);
                textPaint.setTextSize(DensityUtils.sp2px(40));
                Rect rect = new Rect();
                textPaint.getTextBounds("3", 0, numberStr.length(), rect);
                int textAlphas = 12;
                if (textAlpha >= textAlphas) {
                    textAlpha = textAlpha - textAlphas;
                } else if (textAlpha > 0 && textAlpha < textAlphas) {
                    textAlpha--;
                }
                textPaint.setAlpha(textAlpha);
                int w = rect.width() + 5; //获取宽度
                int h = rect.height();//获取高度
                float textLeft = tagretAddressLeft - w / 2;
//                float textTop = tagretAddressTop + circle_point_y / 2 + h / 2 + textAnimationChangeHeight + textAnimationHeight;
                float textTop = textAnimationHeight - 50 + windowHeight / 2;
                canvas.drawText(numberStr, textLeft, textTop, textPaint);
            }

        } else {
            buttonMove = true;
            drawCircleFlag = true;
            canvas.drawRoundRect(r1, radius, radius, buttonPaint);

            //这个文字的要生生画在按钮的中央，可是真蛋疼。
            Rect rect = new Rect();

            textPaint.getTextBounds(defaultStr, 0, defaultStr.length(), rect);

            int w = rect.width(); //获取宽度
            int h = rect.height();//获取高度

            float textLeft = maginLeft + buttonWidth / 2 - w / 2;
            float textTop = maginTop + buttonHeight / 2 + h / 2 - button_y;
            if (textAlpha >= 10) {
                textAlpha = textAlpha - 10;
                if (textAlpha >= 0 && textAlpha < 10) {
                    textAlpha = 0;
                }
            }
            textPaint.setAlpha(textAlpha);
            canvas.drawText(defaultStr, textLeft, textTop, textPaint);
        }
        if (!animationFlag) {
            loge("动画完毕");
            releaseResource();
            if (listener != null) {
                listener.animationOver();
            }
        }
    }


    private boolean animationFlag = true;


    private boolean buttonAlphaFlag = false;

    void loge(String text) {
//        Log.e("xxxxxxxxxxxxx", text);
    }


    /**
     * 画圆的runnable
     */
    private class CircleChangeRunnable implements Runnable {

        @Override
        public void run() {
            if (drawCircleFlag) {
                for (int i = 0; i < 1000; i++) {
                    try {
                        Thread.sleep(3);
                    } catch (Exception e) {

                    }
                    if (buttonMove) {
                        button_y++;
                        postInvalidate();
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * 圆位移的runnable
     */
    private class CirclrDistanceRunnable implements Runnable {

        @Override
        public void run() {
            loge("ciclrDistanceFlag = " + ciclrDistanceFlag);
            if (ciclrDistanceFlag) {
                for (int i = 0; i < 1000; i++) {
                    try {
                        Thread.sleep(2);
                    } catch (Exception e) {

                    }
                    circle_height_y = circle_height_y + i;
                    postInvalidate();
                    if (!ciclrDistanceFlag) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * 圆放大缩小的runnable
     */
    private class CircleBlowupRunnable implements Runnable {

        private float maxRudiuse;

        private float minRudius;

        private int sleepTimes;

        public CircleBlowupRunnable(float minRudius, float maxRudiuse) {
            this.minRudius = minRudius;
            this.maxRudiuse = maxRudiuse;
            Float temp = 500 / (maxRudiuse - minRudius);
            sleepTimes = temp.intValue();
        }

        @Override
        public void run() {
            int changTimes = 3;
            while (changTimes > 0) {
                if (blowUpThreadStartFlag) {
                    if (changTimes > 0) {
//                        textAnimationChangeHeight = 0;
                        textAlpha = 255;
                        numberStr = String.valueOf(changTimes);
                        textAnimationHeight = tagretAddressTop - tagretAddressBottom;
                        // 放大
                        for (float i = minRudius; i < maxRudiuse; i++) {
                            try {
                                Thread.sleep(sleepTimes);
                            } catch (Exception e) {
                            }
                            tagretRadius++;
                            textAnimationHeight = textAnimationHeight + 2;
                            postInvalidate();
                        }
                    }

                    if (changTimes == 1) {
                        // 放大至全屏
                        int fullRudiuse = 100;
                        buttonAlphaFlag = true;
                        for (float i = minRudius; i < fullRudiuse; i++) {
                            try {
                                Thread.sleep(10);
                            } catch (Exception e) {
                            }
                            tagretRadius = tagretRadius + 50;
                            textAnimationHeight = textAnimationHeight + 2;
                            postInvalidate();
                        }
                    }
                    if (changTimes > 1) {
                        // 缩小
                        for (float i = maxRudiuse; i > minRudius; i--) {
                            try {
                                Thread.sleep(sleepTimes);
                            } catch (Exception e) {
                            }
                            tagretRadius--;
                            textAnimationHeight = textAnimationHeight + 2;
                            postInvalidate();
                        }
                    }
                    changTimes--;
                }
            }
            animationFlag = false;
        }
    }
}

interface AnimationOverListener {
    void animationOver();
}
