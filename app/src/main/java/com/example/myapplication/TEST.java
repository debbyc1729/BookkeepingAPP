package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class TEST extends AppCompatActivity {

    private EditText editName , editAddress , editPhone ,data_id;
    private Button confirm, cancel,newList,btnDelect;


    LinearLayout ll_in_sv , objectLayout;

    ArrayList<HashMap> objectList;

    View buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_e_s_t);
        /*buttonView = LayoutInflater.from(PersonalInfoDataMulti.this).inflate(R.layout.personal_object_button, null);

        ll_in_sv = (LinearLayout)findViewById(R.id.ll_in_sv);
        confirm = (Button)findViewById(R.id.info_dialog_confirm);
        cancel = (Button)findViewById(R.id.info_dialog_cancel);
        newList = (Button)buttonView.findViewById(R.id.info_dialog_new);

        addListView();
        setActions();
    }

    public void addListView(){
        objectList = new ArrayList<HashMap>();
        int btnId = 0;
        ll_in_sv.removeAllViews();

        //personal資料來源
        for (int i = 0; i < personal.size(); i++) {

            HashMap<String,EditText> editMap = new HashMap();
            View view = LayoutInflater.from(PersonalInfoDataMulti.this).inflate(R.layout.personal_object, null); //物件來源
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll); //取得personal_object中LinearLayout

            editName = (EditText)ll.findViewById(R.id.editText1); //獲取LinearLayout中各元件
            editName.setText(); //放入personal相關資料來源

            editPhone = (EditText)ll.findViewById(R.id.editText2);
            editPhone.setText();//放入personal相關資料來源

            editAddress = (EditText)ll.findViewById(R.id.editText3);
            editAddress.setText();//放入personal相關資料來源

            btnDelect = (Button)ll.findViewById(R.id.btn_del);
            btnDelect.setOnClickListener(clickHandler);//設定監聽method
            btnDelect.setId(btnId);//將按鈕帶入id 以供監聽時辨識使用
            btnId++;
            //將所有的元件放入map並存入list中
            editMap.put("ADDRESS", editAddress);
            editMap.put("NAME", editName);
            editMap.put("PHONE", editPhone);
            objectList.add(editMap);

            //將上面新建的例元件新增到主頁面的ll_in_sv中
            ll_in_sv.addView(view);
        }
        //最後一筆都放上新增按鈕
        ll_in_sv.addView(buttonView);*/
    }

    /*private void setActions(){
        //設定各元件的監聽
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("msg", "！！確定！！");
    .....//儲存方式
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "！！返回！！");
                finish();
            }
        });

        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg", "！！新增！！");
                //在view中新增一筆新的list 你可以直接在 personal list 直接增加一筆然後再創立一次view
                for(HashMap<String, EditText> editMap:objectList){
                    //我們Map中是存放EditText物件所以取出之後就像一般的物件使用喔
                    String name = editMap.get("NAME").getText().toString();
                }
                addListView();
            }
        });
    }

    //刪除
    private OnClickListener clickHandler= new OnClickListener() {

        @Override
        public void onClick(View v) {

            Button delBtn =  (Button)v; //在new 出所按下的按鈕
            int id = delBtn.getId()//獲取被點擊的按鈕的id
            objectList.get(id);//從 objectList得到此比資料
  .....刪除list（略）
            addListView(); //重新整理 view
        }
    };*/
}