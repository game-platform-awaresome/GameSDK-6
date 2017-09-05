package com.gamater.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.game.tobin.common.GameEncrypt;
import com.game.tobin.sdk.GameSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        String[] str = new String[2];
        str[0] = "Tobin";
        str[1] = "Jni";

        String str1 = GameEncrypt.encrypt(this ,str);
        tv.setText("Tobin Jni encrypt: " + str1);

        GameSDK.getInstance().initSDK(MainActivity.this, "1023", true);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameSDK.getInstance().GameLogin();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GameSDK.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
