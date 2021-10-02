package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import static android.R.layout.simple_list_item_2;
import static android.widget.Toast.LENGTH_SHORT;

public class HOME extends AppCompatActivity {
    private static final String DB_FILE = "wallets.db", DB_TABLE = "wallets";
    private String callSQL;
    private ListView view;
    private TwoLineListItem twoLine;
    public static HOME h=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        h=this;
        setTitle("我的錢包");
        hideBottomUIMenu();
        SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
        try {
            callSQL = "CREATE TABLE table01(_id INTEGER PRIMARY KEY, wallet TEXT, money INTERGER)";
            wallets.execSQL(callSQL);
            callSQL = "CREATE TABLE table02(_id INTEGER PRIMARY KEY, wallet TEXT, money INTERGER, type TEXT, date TEXT , payfor TEXT, pay INTERGER, note TEXT)";
            wallets.execSQL(callSQL);
        }
        catch (Exception ex){
            ;
        }
        wallets.close();
        view = findViewById(R.id.view);
        view.setOnItemClickListener(click);
        view.setOnItemLongClickListener(longclick);
        loadViewData();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //bottomNavigationView.getMenu().setGroupCheckable(0, false, false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.search:
                        Intent intent = new Intent();
                        intent.setClass(HOME.this, search.class);
                        HOME.this.startActivity(intent);
                        HOME.this.finish();
                        break;
                    case R.id.setting:
                        Toast.makeText(HOME.this, "敬請期待", LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HOME.this, creatwallet.class);
                startActivity(intent);
            }
        });
        /*
list.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
       Intent intent = new Intent(context, SendMessage.class);
       String message = "abcpqr";
       intent.putExtra(EXTRA_MESSAGE, message);
       startActivity(intent);
      }
     });*/
    }

    private void loadViewData() {
        SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
        try {

            Cursor cursor = wallets.rawQuery("SELECT * FROM table01", null);
            if (cursor.getCount() > 0) {
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, simple_list_item_2,
                        cursor, new String[]{"wallet", "money"},
                        new int[]{android.R.id.text1, android.R.id.text2}, 0){
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                            TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                            //text1.setTextColor(Color.RED);
                            text1.setTextSize(25);
                            text2.setTextSize(20);
                        return view;
                    };
                };
                view.setAdapter(adapter);
            }
        }catch(Exception e){
                ;
            }
        wallets.close();
    }
    private ListView.OnItemClickListener click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            twoLine = (TwoLineListItem)view;
            //Toast.makeText(HOME.this, twoLine.getText1().getText().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(HOME.this, wallet.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", twoLine.getText1().getText().toString());
            bundle.putString("money", twoLine.getText2().getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    };
    private ListView.OnItemLongClickListener longclick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
            twoLine = (TwoLineListItem)v;
            AlertDialog.Builder builder=new AlertDialog.Builder(HOME.this);
            builder.setMessage("你想刪除錢包\""+twoLine.getText1().getText().toString()+"\"嗎?");
            builder.setTitle("刪除錢包");
            builder.setPositiveButton("確定",  new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
                    callSQL = "DELETE FROM table01 WHERE wallet='"+twoLine.getText1().getText().toString()+"'";
                    wallets.execSQL(callSQL);
                    callSQL = "DELETE FROM table02 WHERE wallet='"+twoLine.getText1().getText().toString()+"'";
                    wallets.execSQL(callSQL);
                    wallets.close();
                    finish();
                    Intent intent = new Intent(HOME.this, HOME.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
            return true;
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
    /*SimpleAdapter adapter = new SimpleAdapter(this, aList,
            android.R.layout.simple_list_item_2, from, ids) {

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView text1 = (TextView) view.findViewById(android.R.id.text1);
            text1.setTextColor(Color.RED);
            return view;
        };
    };*/
}