package com.booyue.base.toast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.booyue.base.R;
import com.booyue.base.app.ProjectInit;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Administrator
 * @date 2018/7/11
 * 提示信息
 */

public class Tips {

    public static void  show(String tips){
        Toast.makeText(ProjectInit.getApplicationContext(),tips, Toast.LENGTH_SHORT).show();
    }
    public static void  show(int tips){
        Toast.makeText(ProjectInit.getApplicationContext(),tips, Toast.LENGTH_SHORT).show();
    }

    public static  void showCustom(Context context, int tips , int type){
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(R.layout.network_toast_view,null);
//
////		int width = WindowsUtils.getDisplayWidth((Activity)context);
////		int height = WindowsUtils.getDisplayHeight((Activity)context);
////		window.setLayout(3*width/4, height/6);
//		//3.用我们的详情页，放到window中，替换掉默认的AlertDialog样式
//
//		TextView tvInstraction = (TextView) layout.findViewById(R.id.toastTitle);
//
//		tvInstraction.setText(tips);
//		Toast toast = new Toast(context.getApplicationContext());
//		toast.setGravity(Gravity.BOTTOM,0,40);
//		toast.setDuration(Toast.LENGTH_LONG);
//		toast.setView(layout);
//		toast.show();
    }

	public void showMyToast(final Toast toast, final int cnt) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				toast.show();
			}
		}, 0, 3500);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				toast.cancel();
				timer.cancel();
			}
		}, cnt );
	}

}
