package io.github.lz233.meizugravity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import io.github.lz233.meizugravity.R;
import io.github.lz233.meizugravity.utils.QRCodeUtil;

public class AboutActivity extends BaseActivity {
    private ImageView aboutImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //
        aboutImageView = findViewById(R.id.aboutImageView);
        //
        aboutImageView.setImageBitmap(QRCodeUtil.createQRCodeBitmap("https://github.com/lz233/MeizuGravity",100,100));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}