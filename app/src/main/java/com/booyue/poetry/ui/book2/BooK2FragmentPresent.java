package com.booyue.poetry.ui.book2;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.booyue.base.util.JSONUtils;
import com.booyue.net.callback.ModelCallback;
import com.booyue.poetry.api.ResourceApi;
import com.booyue.poetry.bean.MainDataBean;
import com.booyue.poetry.listener.PublickTokenCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.20:11
 */

public class BooK2FragmentPresent{
    /**
     * 获取全局数据
     */
    public static void getAllData(Context context) {
        ResourceApi.getPublicToken(new PublickTokenCallback() {
            @Override
            public void onSuccess() {
                getListData(context);
            }

            @Override
            public void onFailed() {

            }
        });

    }


    //请求全局数据  只有这一个接口
    public static void getListData(Context context) {
//        ResourceApi.postMainData(new ModelCallback<MainDataBean>() {
//            @Override
//            public void onSuccess(MainDataBean response) {
//                if (response.isSuccess()){
////                    Log.i("GGG",new Gson().toJson(response));
//                    handleData(response);
//
//                }else {
//                    Toast.makeText(context, "请求数据错误", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onError(int ret, String msg) {
//                super.onError(ret, msg);
//            }
//        });
    }



    /**
     * 处理数据
     * @param holder
     */
    private static void handleData(MainDataBean holder) {
        if (holder == null) return;
//        if (TextUtils.isEmpty(holder.getInfo())) return;
        MainDataBean.MainDataDTO dynastyListDTO = new MainDataBean.MainDataDTO();
        dynastyListDTO.setId(3);
        dynastyListDTO.setDynastyList(holder.getData().getDynastyList());
//        AppListBean appListBean = JSONUtils.fromJson(holder.mResult, AppListBean.class);
//        if (!JSONUtils.isBeanEmpty(appListBean) && JSONUtils.isSuccess(appListBean.ret)) {
//
//        } else {
////            CustomToast.showToast(R.string.request_fail);
//            MessageEvent event = new MessageEvent();
//            event.id = MessageEvent.EVENT_REQUEST_FAIL;
//            EventBus.getDefault().post(event);
//        }
    }
}
