package com.swjtu.deanstar.activity.animalchoose.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.swjtu.deanstar.activity.animalchoose.R;
import com.swjtu.deanstar.activity.animalchoose.util.ViewUitl;

/**
 * Created by yhp5210 on 2017/1/11.
 */

public class AnimalChooseView extends View{

    private static final String TAG = "AnimalChooseView";
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final int ANIMAL_WIDTH = 25;
    private static final int ANIMAL_HEIGHT = 25;
    private int mWidth,mHeight;
    private Paint mPaint;
    private int[] animals = {R.drawable.mouse,R.drawable.cattle,R.drawable.tiger,R.drawable.rabbit,
            R.drawable.dragon,R.drawable.snake,R.drawable.horse,R.drawable.goat,R.drawable.monkey,
            R.drawable.chiken,R.drawable.dog,R.drawable.pig};
    private float startX,startY,endX,endY;
    private float mSweepAngle = 0f;


    {
        initResources();
    }

    public AnimalChooseView(Context context) {
        super(context);
    }

    public AnimalChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimalChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mWidth = ViewUitl.getSize(widthMeasureSpec,WIDTH);
        mHeight = ViewUitl.getSize(widthMeasureSpec,HEIGHT);
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bg = decodeSampledBitmapFromResource(getResources(),R.drawable.rotate_bg,mWidth,mHeight);
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f,0.5f);
        matrix.preTranslate(40f,40f);
        canvas.drawBitmap(bg,matrix,mPaint);
        canvas.save();
        canvas.translate(mWidth/2,mHeight/2);
       // drawXy(canvas);
        for(int i = 0;i < 12;i ++){

            float angleStep = 360/12;
            Bitmap animal = decodeSampledBitmapFromResource(getResources()
                    ,animals[i],ANIMAL_WIDTH,ANIMAL_HEIGHT);
            canvas.drawBitmap(animal,-ANIMAL_WIDTH,-mHeight/2*(3/4.0f),mPaint);
            canvas.rotate(angleStep);
        }
        canvas.restore();

    }


    /**
     * 画辅助坐标系
     * @param canvas
     */
    public void drawXy(Canvas canvas){


        int dimision = 20;
        int arrowDimision = 20;
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setStrokeWidth(2.0f);
        canvas.drawLine(-mWidth/2+dimision,0,mWidth/2-dimision,0,textPaint);//画X轴
        canvas.drawLine(0,-mHeight/2+dimision,0,mHeight/2-dimision,textPaint);//画Y轴
        textPaint.setStrokeWidth(3.0f);
        float[] yArrow = {-arrowDimision,-mHeight/2+arrowDimision+dimision,0,-mHeight/2+dimision,0,-mHeight/2+dimision,arrowDimision,-mHeight/2+arrowDimision+dimision};
        canvas.drawLines(yArrow,textPaint);
        float[] xArrow = {mWidth/2-dimision-arrowDimision,-arrowDimision,mWidth/2-dimision,0,mWidth/2-dimision,0,mWidth/2-dimision-arrowDimision,arrowDimision};
        canvas.drawLines(xArrow,textPaint);
        canvas.drawText("0",-50,50,textPaint);
    }

    public void initResources(){

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight){

        //首先设置inJustDecodeBounds = true来解码用来检查尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options,int reqWidth,int reqHeight){
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth){

            final int halfHeight = height/2;
            final int halfWidth = width/2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps     both
            // height and width larger than the requested height and width.
            while((halfHeight/inSampleSize) >= reqHeight
                    &&(halfWidth/inSampleSize) >= reqWidth){
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action){

            case MotionEvent.ACTION_DOWN:{

                Log.d(TAG,"down");
                startX = event.getX();
                startY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                Log.d(TAG,"move");
                endX = event.getX();
                endY = event.getY();
                float a = (float) Math.tanh(startY/startX);
                float b = (float) Math.tanh(endY/endX);
                mSweepAngle = (float) (Math.atan(startY/startX) - Math.atan(endY/endX));
                Log.d(TAG,"----------a="+a+"--b="+b+"-----sweep angle:"+mSweepAngle);
            }break;
            case MotionEvent.ACTION_UP:{
                Log.d(TAG,"up");
            }break;


        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }


}
