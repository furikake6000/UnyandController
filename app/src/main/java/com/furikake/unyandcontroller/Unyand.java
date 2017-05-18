package com.furikake.unyandcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Unyand extends Activity implements View.OnClickListener{

    //Const
    private static final String ip = "10.0.2.2";    //IPアドレス
    public static final int port = 42014;	//ポート番号

    private final Handler handler = new Handler();

    //Values
    private Socket socket;
    private InputStream ips;
    private OutputStream ops;

    //Layouts
    private EditText sendTextbox;
    private Button sendButton;
    private TextView logView;

    //Call when app starts
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Set layout
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        //Sendtextbox
        sendTextbox = new EditText(this);
        sendTextbox.setText("");
        sendTextbox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(sendTextbox);

        //SendButton
        sendButton = new Button(this);
        sendButton.setText("送信");
        sendButton.setOnClickListener(this);
        sendButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(sendButton);

        //LogView
        logView = new TextView(this);
        logView.setText("");
        logView.setTextSize(16);
        logView.setTextColor(Color.BLACK);
        logView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(logView);
    }

    //Called when activity starts
    @Override
    public void onStart(){
        super.onStart();

        //Thread to wait for server
        Thread thread = new Thread(){
            public void run(){
                connect(ip, port);
            }
        };
        thread.start();
    }

    //Call when activity ends
    @Override
    public void onStop(){
        super.onStop();
        disconnect();
    }

    //ログへの書き込み
    private void printLog(final String text){
        //ハンドラの作成(メインスレッド以外はUIに直接アクセスできないため)
        handler.post(new Runnable() {
            @Override
            public void run() {
                logView.setText(text + "\n" + logView.getText());
            }
        });
    }

    //通信
    private void connect(String address, int port){
        //接続
        printLog(address + "に接続開始 ポート:" + port);
        do{
            try{
                socket = new Socket(address, port);
                ips = socket.getInputStream();
                ops = socket.getOutputStream();
                printLog("接続完了しました。");
            }
            catch (IOException e){
                //接続失敗したら再挑戦
                printLog("接続失敗しました。リトライします...");

                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e2){

                }
            }
        }while(socket == null || !(socket.isConnected()));
    }

    //切断
    private void disconnect(){
        try {
            socket.close();
        }catch (Exception e){
        }
    }

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
}
