package com.furikake.unyandcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import android.os.Handler;

public class Unyand extends Activity{

    //Const
    private static final String ip = "10.0.2.2";    //IPアドレス
    public static final int port = 42014;	//ポート番号

    //Call when app starts
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new UnyandView(this));
    }

    //Called when activity starts
    @Override
    public void onStart(){
        super.onStart();

        //Thread to wait for server
        Thread thread = new Thread(){
            public void run(){
                UnyandNetwork.connect(ip, port);
            }
        };
        thread.start();
    }

    //Call when activity ends
    @Override
    public void onStop(){
        super.onStop();
        UnyandNetwork.disconnect();
    }

    //ログへの書き込み
    public static void printLog(final String text){
        //Viewに情報送信
        UnyandView.logText = text;
    }

    /*
    //ボタンクリック
    public void onClick(View v){
        //現在ボタンが一つしか無いため分岐なし
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //データ送信
                    if(socket != null && socket.isConnected()){
                        byte[] writeData = sendTextbox.getText().toString().getBytes("UTF8");
                        ops.write(writeData);
                        ops.flush();
                    }
                }catch(IOException e){
                    printLog("データ送信失敗");
                }

                //テキストボックスを空に
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendTextbox.setText("");
                    }
                });
            }
        });
        thread.start();
    }
    */
}
