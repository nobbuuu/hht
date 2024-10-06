//package com.booyue.database.demo;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.Button;
//
//import com.booyue.base.activity.BaseActivity;
//import com.booyue.database.R;
//
///**
// * @author: wangxinhua
// * @date: 2019/11/15
// * @description :
// */
//public class DemoActivity extends BaseActivity implements View.OnClickListener{
//    private static final String TAG = "DemoActivity";
//
//   private Button btnAdd;
//   private Button btnDelete;
//   private Button btnUpdate;
//   private Button btnQuery;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_demo);
//
//        btnAdd = findViewById(R.id.add);
//        btnDelete = findViewById(R.id.delete);
//        btnUpdate = findViewById(R.id.udpate);
//        btnQuery = findViewById(R.id.query);
//        setViewListener();
//    }
//
//    private void setViewListener() {
//        btnAdd.setOnClickListener(this);
//        btnDelete.setOnClickListener(this);
//        btnUpdate.setOnClickListener(this);
//        btnQuery.setOnClickListener(this);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//    }
//}
