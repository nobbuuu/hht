package com.booyue.poetry.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.booyue.poetry.R;
import com.booyue.poetry.api.ResourceApi;
import com.booyue.poetry.listener.PublickTokenCallback;
import com.booyue.poetry.ui.book2.BaseBarActivity;
import com.booyue.poetry.ui.book2.BooK2FragmentPresent;
import com.booyue.poetry.ui.book2.FirstOrderActivity;
import com.booyue.poetry.utils.ConditionVideoView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StartPlayActivity extends BaseBarActivity {

    private ConditionVideoView videoView;
    private ImageView startP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_start_play);

        startP = findViewById(R.id.start_p);

        Fresco.initialize(this);
        ResourceApi.getPublicToken(new PublickTokenCallback() {
            @Override
            public void onSuccess() {
//                getListData();
            }

            @Override
            public void onFailed() {

            }
        });
//        BooK2FragmentPresent.getAllData(this);
        startP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPlayActivity.this, MainNewActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        //获得下载目录，不推荐使用，api29已弃用，返回的是公共下载目录，需要声明存储权限
//        //File directory_doc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        //获得下载目录，返回的是应用下载目录
//        String file_path=getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()+"/video";
//        Deposit(file_path,"123.mp4");
////        videoView = findViewById(R.id.start_player);
//        String path=file_path+"/123.mp4";
//        videoView.setVideoPath(path);
//        //创建MediaController对象
//        MediaController mediaController = new MediaController(this);
//        mediaController.setVisibility(View.GONE);
//        videoView.setMediaController(mediaController); //让videoView 和 MediaController相关联
//        videoView.setFocusable(true); //让VideoView获得焦点
//        videoView.start(); //开始播放视频
//        //给videoView添加完成事件监听器，监听视频是否播放完毕
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
////                Toast.makeText(StartPlayActivity.this, "该视频播放完毕！", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(StartPlayActivity.this, LocalBook2Activity.class);
////                Intent intent = new Intent(StartPlayActivity.this, FirstOrderActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }



    //将assets内文件存储到本地下载目录
    public Boolean Deposit(String path,String fileName){
        InputStream inputStream;
        try {

            //判断文件是否存在
            File file1=new File(path + "/" + fileName);

            if(!file1.exists()){

                inputStream=getAssets().open(fileName);
                File file = new File(path);
                //当目录不存在时创建目录
                if(!file.exists()){
                    file.mkdirs();
                }

                FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + fileName);// 保存到本地的文件夹下的文件
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

            }else {
//                Toast.makeText(StartPlayActivity.this,"已存在",Toast.LENGTH_LONG).show();
                Log.i("T","已存在");

            }

            return true;


        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }


}