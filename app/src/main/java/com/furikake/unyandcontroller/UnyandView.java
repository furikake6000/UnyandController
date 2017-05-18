package com.furikake.unyandcontroller;

/**
 * Created by kouichi on 2017/05/05.
 */

import android.content.Context;
import android.graphics.*;
import android.view.View;

public class UnyandView extends View {
    //Constructor
    public UnyandView(Context context){
        super(context);
        setBackgroundColor(Color.WHITE);
    }

    //Draw
    @Override
    public void onDraw(Canvas canvas){
        Paint paint = new Paint();
        paint.setTextSize(48);
        canvas.drawText("Hello,World",0,48,paint);
    }
}
