package com.example.contact;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditContactActivity extends AppCompatActivity {
    private String name;
    private String company;
    private String phoneNum;
    private int vip;
    Button btnBack;
    Button btnSave;
    EditText editName;
    EditText editCompany;
    EditText editPhoneNum;
    RadioGroup rgVip;
    RadioButton rb1;
    RadioButton rb2;
    ContentResolver resolver;
    Uri uri = Uri.parse("content://com.example.contact/contact");
    ContentValues values;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        resolver = getContentResolver();
        values = new ContentValues();
        btnBack = (Button)findViewById(R.id.back);
        btnSave = (Button)findViewById(R.id.save);
        editName = (EditText)findViewById(R.id.edit_name);
        editCompany = (EditText)findViewById(R.id.edit_company);
        editPhoneNum = (EditText)findViewById(R.id.edit_phoneNum);
        rgVip = (RadioGroup) findViewById(R.id.rg_VIP);
        rb1 = (RadioButton)findViewById(R.id.vip_yes);
        rb2 = (RadioButton)findViewById(R.id.vip_no);

        //获取编辑的联系人数据，自动填充原数据
        Intent intent = getIntent();
        String id = String.valueOf(intent.getIntExtra("position", -1));
        editName.setText(intent.getStringExtra("name"));
        editCompany.setText(intent.getStringExtra("company"));
        editPhoneNum.setText(intent.getStringExtra("phoneNum"));

        if(intent.getIntExtra("vip", 10) == 1) {
            rb1.setChecked(true);
        } else if (intent.getIntExtra("vip", 10) == 0) {
            rb2.setChecked(true);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        rgVip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.vip_yes){
                    vip = 1;
                }else if(checkedId == R.id.vip_no){
                    vip = 0;
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = editName.getText().toString();
                company = editCompany.getText().toString();
                phoneNum = editPhoneNum.getText().toString();
                Intent intent = new Intent(EditContactActivity.this, MainActivity.class);

                values.put("name", name);
                values.put("phoneNum", phoneNum);
                values.put("company", company);
                values.put("vip", vip);

                int updateCount = resolver.update(uri, values, "_id=?", new String[]{id.toString()});
                Toast.makeText(EditContactActivity.this,"编辑联系人成功！", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}