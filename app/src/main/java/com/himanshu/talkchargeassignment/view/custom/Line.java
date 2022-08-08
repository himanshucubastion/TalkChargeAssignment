package com.himanshu.talkchargeassignment.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class Line extends View {
    private ArrayList<Double> list;
    double marginBelow=0.0;
    double marginAbove=0.0;
    Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    ArrayList<Pair<Integer,Integer> > arrayList=new ArrayList<>();

    public Line(Context context) {
        super(context);

    }

    public Line(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
    }

    public void setMarginBelow(double marginBelow) {
        this.marginBelow = marginBelow;
    }

    public void setMarginAbove(double marginAbove) {
        this.marginAbove = marginAbove;
    }

    public void draw(ArrayList<Double> list){

        arrayList.clear();
        this.list=list;





        int widthBetweenTwoPoints=list.size()!=0? getWidth()/list.size() : 0;
        int horizontalCenterPoint=widthBetweenTwoPoints/2;
        double max = list.size()!=0? Collections.max(list):0;
        double min = list.size()!=0? Collections.min(list):0;

        double range=max-min;

        double unit=(float)getHeight()/range;
        unit=(1-marginBelow-marginAbove)*unit;




        for (int i=0;i<list.size();i++){

            double offsetPoint=(list.get(i)-min)*unit;   // point considering min is zero
            offsetPoint= marginBelow*getHeight() + offsetPoint;    // point considering min starts from above margin below (after adding bottom offset)

            int yCoordinate=(int)(getHeight()-offsetPoint);  //

            Pair<Integer,Integer> pair= new Pair<>(horizontalCenterPoint+(widthBetweenTwoPoints * i),yCoordinate );
            arrayList.add(pair);
        }

        postInvalidate();
    }


    Paint textPaint =new Paint();


    public void  init(Canvas canvas){


        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        textPaint.setTextSize(20);
        textPaint.setColor(Color.WHITE);

        for (int i = 0; i < arrayList.size()-1; i++) {

            Pair<Integer,Integer> firstCoordinate=arrayList.get(i);
            Pair<Integer,Integer> secondCoordinate=arrayList.get(i+1);

            canvas.drawLine(firstCoordinate.first,firstCoordinate.second, secondCoordinate.first,secondCoordinate.second,paint);

            canvas.drawText(list.get(i).toString(),firstCoordinate.first-10,firstCoordinate.second+30,textPaint);

            canvas.drawCircle(firstCoordinate.first,firstCoordinate.second,5,paint);

            if (i==arrayList.size()-2){
                canvas.drawCircle(secondCoordinate.first,secondCoordinate.second,5,paint);
                canvas.drawText(list.get(i+1).toString(),secondCoordinate.first-10,secondCoordinate.second+30,textPaint);

            }

        }

    }
}
