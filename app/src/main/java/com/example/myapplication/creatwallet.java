package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.database.CursorWindowCompat;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.widget.Toast.LENGTH_SHORT;

public class creatwallet extends AppCompatActivity {
    private static final String DB_FILE = "wallets.db", DB_TABLE = "wallets";
    private String callSQL;
    private EditText name, money;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creatwallet);
        setTitle("新增一個錢包");
        hideBottomUIMenu();

        name = findViewById(R.id.name);
        money = findViewById(R.id.money);
        enter = findViewById(R.id.enter);
        enter.setOnClickListener(creat);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent gointent = new Intent();
                        gointent.setClass(creatwallet.this, HOME.class);
                        startActivity(gointent);
                        finish();
                        break;
                    case R.id.search:
                        Intent intent = new Intent();
                        intent.setClass(creatwallet.this, search.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.setting:
                        Toast.makeText(creatwallet.this, "敬請期待", LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private Button.OnClickListener creat = new
            Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
                    Cursor cursor = wallets.rawQuery("SELECT * FROM table01 WHERE wallet='"+name.getText().toString()+"'",null);
                    if(cursor.getCount() > 0){
                        Toast toast = Toast.makeText(creatwallet.this, '<'+name.getText().toString()+">這個錢包已經存在囉~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                        name.setText("");
                        money.setText("");
                    }
                    else if(money.getText().toString().equals("")){
                        Toast toast = Toast.makeText(creatwallet.this, "請輸入金額喔~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                    else if(name.getText().toString().equals("")){
                        Toast toast = Toast.makeText(creatwallet.this, "請輸入名稱喔~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                    else {
                        callSQL = "INSERT INTO table01 (wallet, money) values ('" + name.getText().toString()
                                + "'," + money.getText().toString() + ")";
                        wallets.execSQL(callSQL);
                        wallets.close();
                        String str = "成功建立錢包~" + money.getText().toString();
                        Toast.makeText(creatwallet.this, "成功建立錢包~" + name.getText().toString(), Toast.LENGTH_SHORT).show();
                        HOME.h.finish();
                        Intent intent = new Intent();
                        intent.setClass(creatwallet.this, HOME.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
    protected void hideBottomUIMenu() {
        //隱藏虛擬按鍵，並且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {

            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
            _window.setAttributes(params);
        }
    }
}