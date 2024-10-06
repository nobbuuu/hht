//package com.booyue.media.demo;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Button;
//
//import com.booyue.media.R;
//import com.booyue.media.bean.VideoBean;
//import com.booyue.media.video.VideoPlayerActivity;
//import com.booyue.media.video.exo.ExoPlayerBaseActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author: wangxinhua
// * @date: 2019/11/12
// * @description :
// */
//public class DemoActivity extends AppCompatActivity {
//    private List<VideoBean> list;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_demo);
//        Button btnVideoPlayer = findViewById(R.id.btnVideoPlayer);
//        btnVideoPlayer.setOnClickListener(v -> {
//            Intent intent = new Intent(DemoActivity.this, VideoPlayerActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putParcelableArrayList(ExoPlayerBaseActivity.Companion.getINTENT_KEY_LIST(), (ArrayList<? extends Parcelable>) list);
//            bundle.putInt(ExoPlayerBaseActivity.Companion.getINTENT_KEY_POSITION(),0);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        });
//        list = new ArrayList<>();
//        String[] videoPath = new String[]{"https://resource.alilo.com.cn/res/7,0175d9f82b02aa.mp4",
//        "https://resource.alilo.com.cn/res/7,0175daf9e8f575.mp4",
//        "https://resource.alilo.com.cn/res/3,0175db05fc389e.mp4",
//        "https://resource.alilo.com.cn/res/3,0175dc1a72d951.mp4",
//        "https://resource.alilo.com.cn/res/3,0175dd6624aadd.mp4",
//        "https://resource.alilo.com.cn/res/5,0175deadb8a85e.mp4"};
//        String[] names = new String[]{"敕勒歌","出塞","别董大","唱咏鹅","春晓","滁州西涧"};
//        for (int i = 0; i < names.length; i++) {
//            VideoBean bean = new VideoBean();
//            bean.name = names[i];
//            bean.path = videoPath[i];
//            list.add(bean);
//        }
//    }
//}
