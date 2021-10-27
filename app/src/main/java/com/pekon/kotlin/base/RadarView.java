package com.pekon.kotlin.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import dagger.hilt.android.HiltAndroidApp;

/**
 * @author zhaosengshan
 */
@HiltAndroidApp
public class RadarView extends View {

    /**
     * 数据个数
     */
    private int count = 3;

    /**
     * 网格最大半径
     */
    private float radius;

    /**
     * 中心X
     */
    private float centerX;
    /**
     * 中心Y
     */
    private float centerY;

    /**
     * 雷达区画笔
     */
    private Paint mainPaint;

    /**
     * 文本画笔
     */
    private Paint textPaint;

    /**
     * 数据区画笔
     */
    private Paint valuePaint;
    /**
     * 外圈圆点画笔
     */
    private Paint pointPaint;
    /**
     * 绘制背景
     */
    private Paint bgPaint;

    /**
     * 肌肤类型描述画笔
     */
    private Paint desPaint;

    /**
     * 圆圈类型描述画笔
     */
    private Paint circlePaint;

    /**
     * 圆圈类型描述画笔
     */
    private Paint secondPaint;

    /**
     * 肌肤类型
     */
    private List<String> skinType;

    /**
     * 肌肤类型描述
     */
    private List<String> skinDes;
    /**
     * 肌肤类型得分
     */
    private List<Double> skinScore;

    /**
     * 数据最大值
     */
    private double maxValue = 3;
    /**
     * 弧度
     */
    private float angle;
    // 当前缩放比
    private float currentScale = 1;

    private Bitmap bitmap;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //雷达区画笔初始化
        mainPaint = new Paint();
        mainPaint.setColor(Color.parseColor("#00000000"));
        mainPaint.setAntiAlias(true);
        mainPaint.setStrokeWidth(1);
        mainPaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(Color.parseColor("#0055A3"));
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(1);
        pointPaint.setStyle(Paint.Style.FILL);


        //分数
        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#0055A3"));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);

        //文案
        desPaint = new Paint();
        desPaint.setColor(Color.parseColor("#0055A3"));
        desPaint.setTextAlign(Paint.Align.CENTER);
        desPaint.setTextSize(40);
        desPaint.setAntiAlias(true);

        //数据区（分数）画笔初始化
        valuePaint = new Paint();
        valuePaint.setColor(Color.GRAY);
        valuePaint.setAntiAlias(true);
        valuePaint.setStyle(Paint.Style.FILL);

        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#268D93E9"));
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);

        //圆圈
        circlePaint = new Paint();

        secondPaint = new Paint();
        secondPaint.setColor(Color.parseColor("#0055A3"));
        secondPaint.setAntiAlias(true);
        secondPaint.setStrokeWidth(1);
        secondPaint.setStyle(Paint.Style.STROKE);
        secondPaint.setPathEffect(new DashPathEffect(new float[]{2, 4}, 0));

        skinType = new ArrayList<>();
        skinType.add("");//视机能
        skinType.add("");
        skinType.add("");
        count = skinType.size();

        skinDes = new ArrayList<>();
        skinDes.add("");
        skinDes.add("");//清晰
        skinDes.add("");//光管理

        //默认分数
        skinScore = new ArrayList<>(count);
        skinScore.add(0.0);
        skinScore.add(0.0);
        skinScore.add(0.0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(w, h) / 2 - 45;
        centerX = w / 2;
        centerY = h / 2;
        //一旦size发生改变，重新绘制
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 后绘制的会覆盖先绘制的
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
//        drawBG(canvas);
        //绘制蜘蛛网
        drawPolygon(canvas);
        //绘制标题
        drawTitle(canvas);

        //绘制直线
//        drawLines(canvas);
        //绘制覆盖区域
        drawRegion(canvas);
    }

    /**
     * 绘制标题文字
     */
    private void drawTitle(Canvas canvas) {
        if (count != skinType.size()) {
            return;
        }
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        //标题高度
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        //绘制文字1
        float x1 = centerX;
        float y1 = centerY - radius;
        canvas.drawText(skinDes.get(0), x1, y1 - fontHeight / 5 - 40, desPaint);
        canvas.drawText(skinType.get(0), x1, y1 - fontHeight / 5 - 18, textPaint);
        //绘制文字2
        float x2 = (float) (centerX + radius * Math.sin(angle) - 20);
        float y2 = (float) (centerY + radius * Math.cos(angle / 2)) + 10;
        //标题一半的宽度
        float dis = textPaint.measureText(skinType.get(1));
        canvas.drawText(skinDes.get(1), x2 + dis / 2 + 25, y2 + fontHeight - 2, desPaint);
        canvas.drawText(skinType.get(1), x2 + dis / 2 + 12, y2 + fontHeight / 5 + 20, textPaint);
//        //绘制文字3
//        float x3 = (float) (centerX + radius * Math.sin(angle / 2));
//        float y3 = (float) (centerY + radius * Math.cos(angle / 2));
//        canvas.drawText(skinType.get(2), x3 + 8, y3 + fontHeight + 8, textPaint);
//        canvas.drawText(skinDes.get(2), x3 + 8, y3 + fontHeight + 36, desPaint);
        //绘制文字4
        float x4 = (float) (centerX - radius * Math.sin(angle / 2));
        float y4 = (float) (centerY + radius * Math.cos(angle / 2)) + 10;
        canvas.drawText(skinDes.get(2), x4 - 6, y4 + fontHeight, desPaint);
        canvas.drawText(skinType.get(2), x4 - 6, y4 + fontHeight + 28, textPaint);
//        //绘制文字5
//        float x5 = (float) (centerX - radius * Math.sin(angle));
//        float y5 = (float) (centerY - radius * Math.cos(angle));
//        //标题的宽度
//        float dis5 = textPaint.measureText(skinType.get(1));
//        canvas.drawText(skinType.get(4), x5 - dis5 / 2 - 15, y5 - fontHeight / 5 - 6, textPaint);
//        canvas.drawText(skinDes.get(4), x5 - dis5 / 2 - 18, y5 - fontHeight / 5 + 24, desPaint);
    }

    /**
     * 绘制覆盖区域
     */
    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        double dataValue;
        double percent;
        //绘制圆点1
        dataValue = skinScore.get(0);
        if (dataValue > maxValue) {
            dataValue = maxValue;
        }
        if (dataValue != maxValue) {
            percent = dataValue / maxValue;
        } else {
            percent = 1;
        }

        float x1 = centerX;
        float y1 = (float) (centerY - radius * percent * currentScale);
        path.moveTo(x1, y1);
        //绘制圆点2
        dataValue = skinScore.get(1);
        if (dataValue > maxValue) {
            dataValue = maxValue;
        }
        if (dataValue != maxValue) {
            percent = dataValue / maxValue;
        } else {
            percent = 1;
        }
        float x2 = (float) (centerX + currentScale * radius * percent * Math.sin(angle));
        float y2 = (float) (centerY - currentScale * radius * percent * Math.cos(angle));
        path.lineTo(x2, y2);

        //绘制圆点4
        dataValue = skinScore.get(2);
        if (dataValue > maxValue) {
            dataValue = maxValue;
        }
        if (dataValue != maxValue) {
            percent = dataValue / maxValue;
        } else {
            percent = 0.85;
        }
        float x4 = (float) (centerX - currentScale * radius * percent * Math.sin(angle / 2) - 1);
        float y4 = (float) (centerY + currentScale * radius * percent * Math.cos(angle / 2));
        path.lineTo(x4, y4);

        path.close();
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), getImageId());
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        valuePaint.setShader(shader);
        canvas.drawPath(path, valuePaint);
    }

    private int getImageId() {

        return 0;
    }


    public void loadAnimation(boolean enabled) {
        if (!enabled) {
            currentScale = 1;
            invalidate();
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(1500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentScale = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    clearBitmap();
                    if (animFinishListener != null) {
                        animFinishListener.onFinish();
                    }
                }
            });
        }
    }

    /**
     * 绘制多边形
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        //1度=1*PI/180   360度=2*PI   那么我们每旋转一次的角度为2*PI/内角个数
        //中心与相邻两个内角相连的夹角角度
        angle = (float) (2 * Math.PI / count);
        //每个蛛丝之间的间距
        float r = radius / count;

        for (int i = 0; i <= count; i++) {
            //当前半径
            float curR = r * i;

            circlePaint.setStyle(Paint.Style.FILL);
            if (i == 1) {
                circlePaint.setColor(Color.parseColor("#00000000"));
            } else if (i == 2) {
                circlePaint.setColor(Color.parseColor("#00000000"));
            } else if (i == 3) {
                circlePaint.setColor(Color.parseColor("#00000000"));
            }
            canvas.drawCircle(centerX, centerY, curR + 1, circlePaint);

            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(1);
            circlePaint.setColor(Color.parseColor("#00000000"));
            canvas.drawCircle(centerX, centerY, curR + 1, circlePaint);

            path.reset();
            for (int j = 0; j <= count; j++) {
                if (j == 0) {
                    float x = (float) (centerX + curR * Math.sin(angle));
                    float y = (float) (centerY - curR * Math.cos(angle));
                    path.moveTo(x, y);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x1 = (float) (centerX + curR * Math.sin(angle / 2));
                    float y1 = (float) (centerY + curR * Math.cos(angle / 2));
                    path.lineTo(x1, y1);
                    float x2 = (float) (centerX - curR * Math.sin(angle / 2));
                    float y2 = (float) (centerY + curR * Math.cos(angle / 2));
                    path.lineTo(x2, y2);
                    float x3 = (float) (centerX - curR * Math.sin(angle));
                    float y3 = (float) (centerY - curR * Math.cos(angle));
                    path.lineTo(x3, y3);
                    float x4 = centerX;
                    float y4 = centerY - curR;
                    path.lineTo(x4, y4);
                    float x = (float) (centerX + curR * Math.sin(angle));
                    float y = (float) (centerY - curR * Math.cos(angle));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mainPaint);
        }
    }


    private void clearBitmap() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public void setSkinScore(List<Double> skinScore) {
        this.skinScore = skinScore;
        invalidate();
    }

    public void setSkinType(List<String> skinType) {
        this.skinType = skinType;
    }

    public void setSkinDes(List<String> skinDes) {
        this.skinDes = skinDes;
    }

    /**
     * 设置满分分数，默认是100分满分
     */
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    private OnAnimFinishListener animFinishListener;

    /**
     * 动画结束 绘制完成
     */
    public interface OnAnimFinishListener {
        void onFinish();
    }

    public void setAnimFinishListener(OnAnimFinishListener animFinishListener) {
        this.animFinishListener = animFinishListener;
    }
}