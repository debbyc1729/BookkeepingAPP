package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class chspend extends AppCompatActivity {
    private static final String DB_FILE = "wallets.db", DB_TABLE = "wallets";
    private String callSQL, ID, name, Money;
    private int mYear, mMonth, mDay;
    private Button ok, delete;
    private TextView date, payfor, money, ps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chspend);


        hideBottomUIMenu();
        setTitle("修改資料");

        ok = findViewById(R.id.ok);
        ok.setOnClickListener(OK);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(DEL);
        date = findViewById(R.id.date);
        date.setOnClickListener(chosedate);
        payfor = findViewById(R.id.payfor);
        money = findViewById(R.id.money);
        ps = findViewById(R.id.ps);

        Intent getintent = chspend.this.getIntent();
        Bundle getbundle = getintent.getExtras();
        ID= getbundle.getString("_id");
        SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
        Cursor cursor = wallets.rawQuery("SELECT * FROM table02 WHERE _id = " + ID, null);
        cursor.moveToFirst();

        name = cursor.getString(cursor.getColumnIndex("wallet"));
        Money = cursor.getString(cursor.getColumnIndex("money"));
        if (cursor.getCount() > 0) {
            payfor.setText(cursor.getString(5));
            money.setText(String.valueOf(cursor.getInt(6)));
            date.setText(String.valueOf(cursor.getInt(4)));
            ps.setText(cursor.getString(7));
        }
        wallets.close();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent gointent = new Intent();
                        gointent.setClass(chspend.this, HOME.class);
                        startActivity(gointent);
                        finish();
                        break;
                    case R.id.search:
                        Intent intent = new Intent();
                        intent.setClass(chspend.this, search.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.setting:
                        Toast.makeText(chspend.this, "敬請期待", LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(chspend.this);
                builder.setMessage("你想刪除這筆資料嗎?");
                builder.setTitle("刪除資料");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
                        Cursor cursor = wallets.rawQuery("SELECT * FROM table02 WHERE _id = " + ID, null);
                        cursor.moveToFirst();
                        Money = String.valueOf(Long.parseLong(Money)+cursor.getInt(cursor.getColumnIndex("pay")));
                    /*ContentValues cv = new ContentValues();
                    cv.put("money", Integer.valueOf(Money));
                    wallets.update("table01", cv, "wallet="+name, null);*/
                        callSQL="UPDATE table01 SET money=" + Money+" WHERE wallet='"+name+"'";
                        wallets.execSQL(callSQL);
                        callSQL = "DELETE FROM table02 WHERE _id="+ID;
                        wallets.execSQL(callSQL);
                        wallets.close();
                        Intent intent = new Intent(chspend.this, wallet.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("money", Money);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
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
            new DatePickerDialog(chspend.this, new DatePickerDialog.OnDateSetListener() {
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
    private Button.OnClickListener DEL = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(chspend.this, wallet.class);
            startActivity(intent);
            finish();
        }
    };
    private Button.OnClickListener OK = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
            Cursor cursor = wallets.rawQuery("SELECT * FROM table02 WHERE _id = " + ID, null);
            cursor.moveToFirst();
            Money = String.valueOf(Long.parseLong(Money)+cursor.getInt(cursor.getColumnIndex("pay")));
            Money = String.valueOf(Long.parseLong(Money)-Long.parseLong(money.getText().toString()));
            //callSQL = "CREATE TABLE table02(_id INTEGER PRIMARY KEY, wallet TEXT, money INTERGER, date TEXT , payfor TEXT, pay INTERGER, note TEXT)";
            callSQL = "UPDATE table02 SET money=" + Money +", payfor='"+payfor.getText().toString()+"', date='"+date.getText().toString()
                    +"', pay="+money.getText().toString()+", note='"+ps.getText().toString()+ "' WHERE wallet='" + name + "' AND _id=" + ID;
            wallets.execSQL(callSQL);
            callSQL = "UPDATE table01 SET money=" + Money +" WHERE wallet='" + name + "'";
            wallets.execSQL(callSQL);
            wallets.close();
            String str = "成功修改花費~" + payfor.getText().toString();
            Toast.makeText(chspend.this, str, Toast.LENGTH_SHORT).show();
            //wallet.w.finish();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("money", Money);
            intent.putExtras(bundle);
            wallet.w.finish();
            intent.setClass(chspend.this, wallet.class);
            startActivity(intent);
            finish();
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