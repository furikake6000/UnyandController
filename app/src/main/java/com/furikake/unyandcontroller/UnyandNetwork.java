package com.furikake.unyandcontroller;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by kouichi on 2017/06/07.
 */

public class UnyandNetwork {

    //Values
    private static Socket socket;
    private static InputStream ips;
    private static OutputStream ops;

    //通信
    public static void connect(String address, int port){
        //接続
        Unyand.printLog(address + "に接続開始 ポート:" + port);
        do{
            try{
                socket = new Socket(address, port);
                ips = socket.getInputStream();
                ops = socket.getOutputStream();
                Unyand.printLog("接続完了しました。");
            }
            catch (IOException e){
                //接続失敗したら再挑戦
                Unyand.printLog("接続失敗しました。リトライします...");

                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e2){

                }
            }
        }while(socket == null || !(socket.isConnected()));
    }

    //切断
    public static void disconnect(){
        try {
            socket.close();
        }catch (Exception e){
        }
    }

    //バイト列送信
    public static void sendBytes(final byte[] bytes){

        //非同期実行でないとandroid.os.NetworkOnMainThreadExceptionを出されてしまう
        AsyncTask<Void,Void,Void> sendBytesTask = new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params){
                //何も入力されていなければ終了
                if(bytes.length == 0)return null;
                //通信が確立されていなければ終了
                if(socket == null || !socket.isConnected())return null;

                try{
                    //データをストリームに書き込み
                    ops.write(bytes);
                    //終端記号としてFFを追加
                    ops.write(0xFF);
                    //送信
                    ops.flush();

                }catch(IOException e){
                    //送信失敗
                    Unyand.printLog("つうしんがおかしいよ");
                }

                return null;
            }
        };

        sendBytesTask.execute();
    }
}
