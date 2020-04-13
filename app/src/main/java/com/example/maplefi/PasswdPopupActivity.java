package com.example.maplefi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PasswdPopupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set No_Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.passwd_popup_activity);

        // Get Intent Parameter
        Intent intent = getIntent();
        final String ssid = intent.getStringExtra("ssid");
        final String capabilities = intent.getStringExtra("capabilities");

        final EditText editTxt_passwd = (EditText)findViewById(R.id.et_passwd);
        Button button_ok = (Button)findViewById(R.id.btn_ok);
        Button button_no = (Button)findViewById(R.id.btn_no);


        button_ok.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TEST","Ok Click");
                Intent intent = new Intent();
                intent.putExtra("ssid", ssid);
                intent.putExtra("capabilities", capabilities);
                intent.putExtra("password", editTxt_passwd.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        button_no.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 바깥레이어 클릭 시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
    */
}
