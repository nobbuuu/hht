//package com.booyue.media.video.ijk;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import com.xiao.nicevideoplayer.NiceVideoPlayer;
//import com.xiao.nicevideoplayer.NiceVideoPlayerController;
//import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
//
///**
// * @author: wangxinhua
// * @date: 2020/3/12 11:28
// * @description :
// */
//public class HHTVideoPlayer extends NiceVideoPlayer {
//
//    public HHTVideoPlayer(Context context) {
//        super(context);
//    }
//
//    public HHTVideoPlayer(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public void setIJKPlayerType(){
//        setPlayerType(NiceVideoPlayer.TYPE_IJK);
//    }
//
//    public boolean onBackPressd(){
//        return NiceVideoPlayerManager.instance().onBackPressd();
//    }
//    public void setHHTVideoController(NiceVideoPlayerController ijkControllView){
//        setController(ijkControllView);
//    }
//
//    public void setHHTVideoUrl(String url){
//        setUp(url,null);
//    }
//    public void startPlay(){
//        start();
//    }
//
//    public void releaseHHTVideoPlayer(){
//        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
//    }
//     public void releaseHHTPlayer(){
//       releasePlayer();
//    }
//
//
//
//
//}
