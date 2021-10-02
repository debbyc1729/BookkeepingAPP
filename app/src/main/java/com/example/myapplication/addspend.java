package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigInteger;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class addspend extends AppCompatActivity {
    private static final String DB_FILE = "wallets.db", DB_TABLE = "wallets";
    private String callSQL;
    private int mYear, mMonth, mDay;
    private Button ok, back;
    private EditText date, payfor, money, ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addspend);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        setTitle(bundle.getString("name")+"    餘額："+bundle.getString("money"));

        ok = findViewById(R.id.ok);
        ok.setOnClickListener(OK);
        back = findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        date = findViewById(R.id.date);
        date.setOnClickListener(chosedate);
        payfor = findViewById(R.id.payfor);
        money = findViewById(R.id.money);
        ps = findViewById(R.id.ps);
        hideBottomUIMenu();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent gointent = new Intent();
                        gointent.setClass(addspend.this, HOME.class);
                        startActivity(gointent);
                        finish();
                        break;
                    case R.id.search:
                        Intent intent = new Intent();
                        intent.setClass(addspend.this, search.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.setting:
                        Toast.makeText(addspend.this, "敬請期待", LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
    private EditText.OnClickListener chosedate = new EditText.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(addspend.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String format = String.valueOf(year);
                    if(month < 10)
                        format = format+'0'+String.valueOf(month+1);
                    else
                        format = format+'0'+String.valueOf(month+1);
                    if(dayOfMonth < 10)
                        format = format+'0'+String.valueOf(dayOfMonth);
                    else
                        format = format+'0'+String.valueOf(dayOfMonth);
                            //+String.valueOf(month+1)+String.valueOf(dayOfMonth);
                    date.setText(format);
                    //String.valueOf
                }
            }, mYear,mMonth, mDay).show();
        }
    };
    private Button.OnClickListener OK = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent getintent = addspend.this.getIntent();
            Bundle getbundle = getintent.getExtras();
            Long a;


            if(payfor.getText().toString().equals("")){
                Toast toast = Toast.makeText(addspend.this, "請輸入項目喔~", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
            else if(money.getText().toString().equals("")){
                Toast toast = Toast.makeText(addspend.this, "請輸入金額喔~", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
            else if(date.getText().toString().equals("")){
                Toast toast = Toast.makeText(addspend.this, "請輸入日期喔~", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
            }
            else {
                a = Long.parseLong(getbundle.getString("money"))-Long.parseLong(money.getText().toString());
                SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
                //callSQL = "CREATE TABLE table02(_id INTEGER PRIMARY KEY, wallet TEXT, money INTERGER, date TEXT , payfor TEXT, pay INTERGER, note TEXT)";
                callSQL = "INSERT INTO table02 (wallet, money, date, payfor, pay, note) values ('" + getbundle.getString("name")
                        + "'," + String.valueOf(a) + ",'" + date.getText().toString() + "','" + payfor.getText().toString()
                        + "'," + money.getText().toString() + ",'" + ps.getText().toString() + "')";
                wallets.execSQL(callSQL);
                callSQL = "UPDATE table01 SET money=" + String.valueOf(a) + " WHERE wallet='" + getbundle.getString("name") + "'";
                wallets.execSQL(callSQL);
                callSQL = "UPDATE table02 SET money=" + String.valueOf(a) + " WHERE wallet='" + getbundle.getString("name") + "'";
                wallets.execSQL(callSQL);
                wallets.close();
                String str = "成功建立花費~" + payfor.getText().toString();
                Toast.makeText(addspend.this, str, Toast.LENGTH_SHORT).show();
                wallet.w.finish();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name", getbundle.getString("name"));
                bundle.putString("money", String.valueOf(a));
                intent.putExtras(bundle);
                intent.setClass(addspend.this, wallet.class);
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