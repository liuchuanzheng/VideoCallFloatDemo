package com.example.videocallfloatdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.videocallfloatdemo.floatview.FloatService;
import com.example.videocallfloatdemo.floatview.FloatingButtonService;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    Button btn_float;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }
    private void initView() {
        btn_float = findViewById(R.id.btn_float);
        btn_float.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_float:
                moveTaskToBack(true);//最小化Activity
//                Intent intent = new Intent(this, FloatService.class);//开启服务显示悬浮框
//                bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);

                if (FloatingButtonService.isStarted) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
                    } else {
                        startService(new Intent(Main2Activity.this, FloatingButtonService.class));
                    }
                } else {
                    startService(new Intent(Main2Activity.this, FloatingButtonService.class));
                }

                break;
            default:
                break;
        }
    }
    ServiceConnection mVideoServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatService.MyBinder binder = (FloatService.MyBinder) service;
            binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                    startService(new Intent(Main2Activity.this, FloatingButtonService.class));
                }
            }
        }
    }


}
