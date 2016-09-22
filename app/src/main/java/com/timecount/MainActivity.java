package com.timecount;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtn;
    private TextView mOffTextView;
    private Handler mOffHandler;
    private Timer mOffTime;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                timeCountDialog();
                break;
            default:
                break;
        }
    }


    public void timeCountDialog() {
        mOffTextView = new TextView(this);

        mDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setCancelable(false)
                .setView(mOffTextView)
                .setPositiveButton("5秒", null)
                .setNegativeButton("取消", null)
                .create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        final Button Positivebutton = ((AlertDialog) mDialog).getButton(AlertDialog.BUTTON_POSITIVE);
        final Button Negativebutton = ((AlertDialog) mDialog).getButton(AlertDialog.BUTTON_NEGATIVE);
        Positivebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Positivebutton.getText().toString().contains("申请")){
                    mDialog.dismiss();
                    mOffTime.cancel();
                }
            }
        });
        Negativebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                mOffTime.cancel();
            }
        });

        mOffHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what > 0) {
                    //动态显示倒计时
                    mOffTextView.setText("    即将关闭：" + msg.what);
                    Positivebutton.setText(msg.what + "秒");
                } else {
                    //倒计时结束自动关闭
                    if (mDialog != null) {
                        mOffTextView.setVisibility(View.GONE);
                        Positivebutton.setText("申请特聘");
                    }
                    mOffTime.cancel();
                }
                super.handleMessage(msg);
            }

        };

        //////倒计时

        mOffTime = new Timer(true);
        TimerTask tt = new TimerTask() {
            int countTime = 5;

            public void run() {
                if (countTime > 0) {
                    countTime--;
                }
                Message msg = new Message();
                msg.what = countTime;
                mOffHandler.sendMessage(msg);
            }
        };
        mOffTime.schedule(tt, 1000, 1000);
    }
}
