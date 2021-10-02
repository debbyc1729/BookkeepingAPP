package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import static android.R.layout.simple_list_item_2;
import static android.widget.Toast.LENGTH_SHORT;

public class search extends AppCompatActivity {

    private EditText schwalletname, schpayfor, schmoney, srchdate, schkeyword;
    private Button searchbtn;
    private ListView view;
    private int mYear, mMonth, mDay;
    private static final String DB_FILE = "wallets.db", DB_TABLE = "wallets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        setTitle("搜尋");
        hideBottomUIMenu();

        schwalletname = findViewById(R.id.schwalletname);
        schpayfor = findViewById(R.id.schpayfor);
        schmoney = findViewById(R.id.schmoney);
        srchdate = findViewById(R.id.srchdate);
        srchdate.setOnClickListener(chosedate);

        schkeyword = findViewById(R.id.schkeyword);
        view = findViewById(R.id.view);
        //view.setOnItemClickListener(click);

        searchbtn = findViewById(R.id.searchbtn);

        searchbtn.setOnClickListener(querylistener);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent gointent = new Intent();
                        gointent.setClass(search.this, HOME.class);
                        startActivity(gointent);
                        finish();
                        break;
                    case R.id.search:
                        break;
                    case R.id.setting:
                        Toast.makeText(search.this, "敬請期待", LENGTH_SHORT).show();
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
            new DatePickerDialog(search.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String format = String.valueOf(year);
                    if(month < 10)
                        format = format+'0'+String.valueOf(month+1);
                    else
                        format = format+String.valueOf(month+1);
                    if(dayOfMonth < 10)
                        format = format+'0'+String.valueOf(dayOfMonth);
                    else
                        format = format+String.valueOf(dayOfMonth);
                    //+String.valueOf(month+1)+String.valueOf(dayOfMonth);
                    srchdate.setText(format);
                    //String.valueOf
                }
            }, mYear,mMonth, mDay).show();
        }
    };

    private Button.OnClickListener querylistener = new//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String temp = "" , rowtemp = "";
                    String strwhere = " WHERE ";
                    SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
                    if(!schwalletname.getText().toString().equals("")){
                        temp = schwalletname.getText().toString();
                        //rowtemp = "wallet";
                        if(!strwhere.equals(" WHERE "))
                            strwhere = strwhere + " AND wallet='" + temp + "'";
                        else
                            strwhere = strwhere + "wallet='" + temp + "'";
                    }
                    if (!schpayfor.getText().toString().equals("")){
                        temp = schpayfor.getText().toString();
                        //rowtemp = "payfor";
                        if(!strwhere.equals(" WHERE "))
                            strwhere = strwhere + " AND payfor='" + temp + "'";
                        else
                            strwhere = strwhere + "payfor='" + temp + "'";
                    }
                    if (!schmoney.getText().toString().equals("")){
                        temp = schmoney.getText().toString();
                        //rowtemp = "money";
                        if(!strwhere.equals(" WHERE "))
                            strwhere = strwhere + " AND pay=" + temp;
                        else
                            strwhere = strwhere + "pay=" + temp;
                    }
                    if (!srchdate.getText().toString().equals("")){
                        temp = srchdate.getText().toString();
                        //rowtemp = "date";
                        if(!strwhere.equals(" WHERE "))
                            strwhere = strwhere + " AND date='" + temp + "'";
                        else
                            strwhere = strwhere + "date='" + temp + "'";
                    }
                    if (!schkeyword.getText().toString().equals("")){//???????????????????
                        temp = schkeyword.getText().toString();
                        rowtemp = "pay";//, note
                        if(!strwhere.equals(" WHERE "))
                            strwhere = strwhere + " AND ";

                        strwhere = strwhere + "date='" + temp + "' OR wallet='" + temp + "'" + " OR payfor='" + temp + "' OR pay=" + temp + "'"+ " OR note='" + temp + "'";
                    }
                    /*else{
                        String str = "請輸入至少一項";
                        Toast.makeText(search.this, str, Toast.LENGTH_SHORT).show();
                    }*/
                    Log.d("where",strwhere);
                    try {
                        Cursor cursor;
                        if(!strwhere.equals(" WHERE "))
                            cursor = wallets.rawQuery("SELECT * FROM table02" +  strwhere , null);
                        else
                            cursor = wallets.rawQuery("SELECT * FROM table02", null);
                        if (cursor.getCount() > 0) {
                            SimpleCursorAdapter adapter = new SimpleCursorAdapter(search.this, R.layout.wallet_listview,
                                    cursor, new String[]{"payfor", "pay", "date"},
                                    new int[]{R.id.name_view, R.id.money_view, R.id.data_view}, 0){
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    TextView name_view = (TextView) view.findViewById(R.id.name_view);
                                    TextView money_view = (TextView) view.findViewById(R.id.money_view);
                                    TextView data_view = (TextView) view.findViewById(R.id.data_view);
                                    //text1.setTextColor(Color.RED);
                                    //text1.setTextSize(25);
                                    //text2.setTextSize(20);
                                    return view;
                                };
                            };
                            view.setAdapter(adapter);
                        }
                        else{
                            String str = "沒有這筆花費";
                            Toast.makeText(search.this, str, Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        ;
                    }
                    wallets.close();
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