package com.example.contactviwer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Uri uri = Uri.parse("content://com.example.contact/contact");
    ContentResolver resolver;
    ContentValues values;
    TextView tv;
    EditText edName;
    EditText edPhoneNum;
    EditText edCompany;
    RadioGroup rd;
    Button btnShowAll;
    Button btnAdd;
    Button btnDelete;
    Button btnEdit;
    String name;
    private String company;
    private String phoneNum;
    private int vip = 0;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_viewer);
        tv = (TextView)findViewById(R.id.viewContact);
        edName = (EditText)findViewById(R.id.edit_name);
        edCompany = (EditText)findViewById(R.id.edit_company);
        edPhoneNum = (EditText)findViewById(R.id.edit_phoneNum);
        rd = (RadioGroup)findViewById(R.id.rg_VIP);
        btnShowAll = (Button)findViewById(R.id.showAll);
        btnAdd = (Button)findViewById(R.id.add);
        btnDelete = (Button)findViewById(R.id.delete);
        btnEdit = (Button)findViewById(R.id.edit);
        btnShowAll.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.vip_yes){
                    vip = 1;
                }
            }
        });
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.showAll:
                text = "";
                resolver = getContentResolver();
                Cursor cursor = resolver.query(uri, new String[]{"name", "phoneNum", "company", "vip"}, null, null, null);
                if(cursor != null && cursor.getCount() > 0){
                    tv.setVisibility(View.VISIBLE);
                    while (cursor.moveToNext()){
                        text += "姓名：" + cursor.getString(0) + "\n";
                        text += "电话：" + cursor.getString(1) + "\n";
                        text += "公司：" + cursor.getString(2) + "\n";
                        if(cursor.getInt(3) == 1){
                            text += "是否为vip：是" + "\n\n";
                        } else {
                            text += "是否为vip：否" + "\n\n";
                        }
                        tv.setText(text);
                    }
                }
                break;
            case R.id.add:
                resolver = getContentResolver();
                name = edName.getText().toString();
                company = edCompany.getText().toString();
                phoneNum = edPhoneNum.getText().toString();
                values = new ContentValues();
                values.put("name", name);

                values.put("company", company);
                values.put("phoneNum", phoneNum);
                values.put("vip", vip);
                Uri u = resolver.insert(uri, values);
                Toast.makeText(this,"添加联系人成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                resolver = getContentResolver();
                name = edName.getText().toString();
                company = edCompany.getText().toString();
                phoneNum = edPhoneNum.getText().toString();
                int count = resolver.delete(uri, "name=?", new String[]{name});
                if (count <= 0){
                    Toast.makeText(this, "删除失败，没有该联系人“" + name + "”", Toast.LENGTH_SHORT).show();
                    break;
                }
                Toast.makeText(this, "成功删除“" + name + "”", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit:
                resolver = getContentResolver();
                name = edName.getText().toString();
                company = edCompany.getText().toString();
                phoneNum = edPhoneNum.getText().toString();

                values = new ContentValues();
                values.put("name", name);
                values.put("company", company);
                values.put("phoneNum", phoneNum);
                values.put("vip", vip);
                int updateCount = resolver.update(uri, values, "name=?", new String[]{name});
                if (updateCount <= 0){
                    Toast.makeText(this,"编辑失败，没有该联系人！", Toast.LENGTH_SHORT).show();
                    break;
                }
                Toast.makeText(this,"编辑联系人成功！", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}