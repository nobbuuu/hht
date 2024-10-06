package com.booyue.poetry.ui.book2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.booyue.base.toast.Tips;
import com.booyue.base.util.AppUtils;
import com.booyue.poetry.MyApp;
import com.booyue.poetry.R;
import com.booyue.poetry.ui.MainActivity;
import com.booyue.poetry.ui.dialog.DownloadOptionDialog;
import com.booyue.poetry.ui.download.DownloadActivity;
import com.booyue.poetry.utils.DialogUtil;

public class NewSetUpActivity extends BaseBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        TextView textVersionName = (TextView) findViewById(R.id.versionName);
        findViewById(R.id.runSetFinSh).setOnClickListener(v -> finish() );
        findViewById(R.id.startDownButton).setOnClickListener(v -> startActivity(new Intent(NewSetUpActivity.this, DownloadActivity.class)) );

//        findViewById(R.id.appCode).setOnClickListener(v -> {
//            DialogUtil.showNetWorkDialog(this, 0);
//        });



        String versionCode = AppUtils.getVersionName(MyApp.getInstance());
        int versionCode2 = AppUtils.getVersionCode(MyApp.getInstance());
        textVersionName.setText("版本"+versionCode);
        Log.i("TTT!!!1",versionCode);
        Log.i("TTT!!!2",versionCode2+"");
    }
}