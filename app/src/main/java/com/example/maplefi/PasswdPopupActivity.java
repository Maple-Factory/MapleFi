package com.example.maplefi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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

        final EditText passwdEditText = (EditText) findViewById(R.id.et_passwd);
        Button okButton = (Button) findViewById(R.id.btn_ok);
        Button noButton = (Button) findViewById(R.id.btn_no);

        okButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TEST","Ok Click");
                Intent intent = new Intent();
                intent.putExtra("ssid", ssid);
                intent.putExtra("capabilities", capabilities);
                intent.putExtra("password", passwdEditText.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        noButton.setOnClickListener(new Button.OnClickListener() {
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
