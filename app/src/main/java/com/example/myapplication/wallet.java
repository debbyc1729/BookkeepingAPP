package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class wallet extends AppCompatActivity {
    private static final String DB_FILE = "wallets.db", DB_TABLE = "wallets";
    private Button addspend;
    private ListView spend;
    private List<Integer> ID = new ArrayList<Integer>();
    public static wallet w=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);
        Intent intent = this.getIntent();
        w = this;
        hideBottomUIMenu();
        Bundle bundle = intent.getExtras();///////
        setTitle(bundle.getString("name")+"    餘額："+bundle.getString("money"));
        /////
        //listlayoutadapter adasports = new listlayoutadapter(this);
        spend = findViewById(R.id.spend);
        spend.setOnItemClickListener(change);
        loadViewData();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent gointent = new Intent();
                        gointent.setClass(wallet.this, HOME.class);
                        startActivity(gointent);
                        finish();
                        break;
                    case R.id.search:
                        Intent intent = new Intent();
                        intent.setClass(wallet.this, search.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.setting:
                        Toast.makeText(wallet.this, "敬請期待", LENGTH_SHORT).show();
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
                Intent getintent = wallet.this.getIntent();
                Bundle getbundle = getintent.getExtras();
                Bundle bundle = new Bundle();
                bundle.putString("name", getbundle.getString("name"));
                bundle.putString("money", getbundle.getString("money"));
                intent.putExtras(bundle);
                intent.setClass(wallet.this, addspend.class);
                startActivity(intent);
            }
        });
    }
    private void loadViewData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        SQLiteDatabase wallets = openOrCreateDatabase(DB_FILE, MODE_PRIVATE, null);
        try {
            Cursor cursor = wallets.rawQuery("SELECT * FROM table02 WHERE wallet = ?", new String[]{bundle.getString("name")});
            if (cursor.getCount() > 0) {
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.wallet_listview,
                        cursor, new String[]{"payfor", "pay", "date", "_id"},
                        new int[]{R.id.name_view, R.id.money_view, R.id.data_view, R.id._id}, 0){
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView name_view = (TextView) view.findViewById(R.id.name_view);
                        TextView money_view = (TextView) view.findViewById(R.id.money_view);
                        TextView data_view = (TextView) view.findViewById(R.id.data_view);
                        TextView _id = (TextView) view.findViewById(R.id._id);
                        //text1.setTextColor(Color.RED);
                        //text1.setTextSize(25);
                        //text2.setTextSize(20);
                        return view;
                    };
                };
                spend.setAdapter(adapter);
            }
        }catch(Exception e){
            ;
        }
        wallets.close();
    }
    private ListView.OnItemClickListener change = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView name_view = (TextView) view.findViewById(R.id.name_view);
            TextView money_view = (TextView) view.findViewById(R.id.money_view);
            TextView data_view = (TextView) view.findViewById(R.id.data_view);
            TextView _id = (TextView) view.findViewById(R.id._id);
            //Toast.makeText(wallet.this, _id.getText().toString(), LENGTH_SHORT).show();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("_id", _id.getText().toString());
            intent.putExtras(bundle);
            intent.setClass(wallet.this, chspend.class);
            startActivity(intent);
            finish();
            //cursor.getInt(0)
                //addspend.setText(name_view.getText().toString());
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