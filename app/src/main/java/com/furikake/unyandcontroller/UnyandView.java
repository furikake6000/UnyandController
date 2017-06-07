package com.furikake.unyandcontroller;

/**
 * Created by kouichi on 2017/05/05.
 */

import android.content.Context;
import android.graphics.*;
import android.view.View;
import android.view.MotionEvent;

public class UnyandView extends View {

    final int BUTTON_NUM = 5;

    public static String logText = "";
    boolean[] isButtonDown = new boolean[BUTTON_NUM];

    //Constructor
    public UnyandView(Context context){
        super(context);
        setBackgroundColor(Color.WHITE);

        for(int i=0;i<isButtonDown.length;i++){
            isButtonDown[i] = false;
        }
    }

    //Draw
    @Override
    public void onDraw(Canvas canvas){

        //四角形の描画
        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.argb(255,0,0,255));
        for(int i=0;i<BUTTON_NUM;i++){
            if(isButtonDown[i]){
                canvas.drawRect(getWidth() * i / BUTTON_NUM, 0, getWidth() * (i + 1) / BUTTON_NUM, getHeight(), rectPaint);
            }
        }

        //タッチ情報の描画
        Paint textPaint = new Paint();
        textPaint.setTextSize(48);

        canvas.drawText(logText,0,60,textPaint);
        canvas.drawText("Text draw",0,180,textPaint);

    }

    //Call when Touch Event occurs
    @Override
    public boolean onTouchEvent(MotionEvent event){

        //どのボタンの位置に該当するかを返す
        int buttonIndex = (int)(event.getX() * BUTTON_NUM / getWidth());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                enableButton(buttonIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                disableButton(buttonIndex);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }

        //再描画
        invalidate();

        return true;
    }

    //ボタン押下処理
    boolean enableButton(int index){
        if(isButtonDown[index]){
            return false;
        }else{
            //更新データ送信処理
            byte[] sb = new byte[2];
            sb[0] = (byte)index;
            sb[1] = 0x01;

            UnyandNetwork.sendBytes(sb);

            isButtonDown[index] = true;
            return true;
        }
    }

    //ボタン押上処理
    boolean disableButton(int index){
        if(!isButtonDown[index]){
            return false;
        }else{
            //更新データ送信処理
            byte[] sb = new byte[2];
            sb[0] = (byte)index;
            sb[1] = 0x00;

            UnyandNetwork.sendBytes(sb);

            isButtonDown[index] = false;
            return true;
        }
    }
}
