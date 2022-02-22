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
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            //返回按钮触发代码，直接返回resultCode=0结束该活动
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        rgVip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.vip_yes){
                    vip = 1;
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editName.getText().toString();
                company = editCompany.getText().toString();
                phoneNum = editPhoneNum.getText().toString();

                Intent intent = new Intent(AddContactActivity.this, MainActivity.class);

                values.put("name", name);
                values.put("phoneNum", phoneNum);
                values.put("company", company);
                values.put("vip", vip);

                Uri newUri = resolver.insert(uri, values);
                Toast.makeText(AddContactActivity.this, "添加联系人成功！", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
