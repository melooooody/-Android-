package com.example.contact;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Contact> contacts; //动态存储联系人信息的List
    private MyAdapter myAdapter;    //定义数据适配器
    ListView lv;    //初始化界面ListView组件
    ContentResolver resolver;
    Uri uri = Uri.parse("content://com.example.contact/contact");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_view);
        Log.i("x","1");
        resolver = this.getContentResolver();
        createDB();     //向空表中添加几条数据
        lv = (ListView) findViewById(R.id.contact);
        contacts = createList();
        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);

        this.registerForContextMenu(lv);
    }

    private List<Contact> createList() {  //初始化List，将数组中信息存入List中
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "name", "phoneNum", "company", "vip"}, null,null,null);
        while (cursor.moveToNext()) {
            contacts.add(new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4)));
        }
        cursor.close();
        return contacts;
    }


    class MyAdapter extends BaseAdapter{       //定义新对象继承BaseAdapter适配器，并重写相关方法方法

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.contact_list_item, null);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.name);
            holder.tvPhoneNum = (TextView) convertView.findViewById(R.id.phoneNum);
            holder.tvCompany = (TextView) convertView.findViewById(R.id.company);

            Contact contact = contacts.get(position);
            holder.tvName.setText(contact.getName().toString());
            holder.tvPhoneNum.setText(contact.getPhoneNum().toString());
            holder.tvCompany.setText(contact.getCompany().toString());

            if(contact.getVip() == 1){
                convertView.setBackgroundColor(Color.rgb(255,255,0));
            }else {
                convertView.setBackgroundColor(Color.rgb(255,255,255));
            }


            return convertView;
        }

        class ViewHolder{
            TextView tvName;
            TextView tvPhoneNum;
            TextView tvCompany;
        }
    }

    //复写上下文菜单方法
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0, Menu.NONE,"添加联系人");
        menu.add(0,1, Menu.NONE,"编辑联系人");
        menu.add(0,2, Menu.NONE,"删除该联系人");
        menu.add(0,3, Menu.NONE,"通话");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()){
            case 0:
                //添加联系人菜单选项，数据回传
                Intent intent =  new Intent(this, AddContactActivity.class);
                startActivity(intent);
                break;
            case 1:
                //编辑联系人菜单选项，首先找到选中的联系人位置，将该联系人信息传入编辑联系人活动进行编辑
                Intent intent1 = new Intent(this,EditContactActivity.class);
                Contact c = contacts.get(menuInfo.position);
                intent1.putExtra("name", c.getName());
                intent1.putExtra("phoneNum", c.getPhoneNum());
                intent1.putExtra("company", c.getCompany());
                intent1.putExtra("vip", c.getVip());
                intent1.putExtra("position",c.getId()); //标记该选中联系人在list中位置
                startActivity(intent1);
                break;
            case 2:
                //删除联系人菜单选项，弹出确定框，点击确定后删除该联系人
                String n = contacts.get(menuInfo.position).getName();
                String id = String.valueOf(contacts.get(menuInfo.position).getId());
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(this).setMessage("确定删除联系人“" + n + "”").
                        setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deleteCount = resolver.delete(uri, "_id=?", new String[]{id.toString()});
                        contacts.remove(menuInfo.position);
                        myAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "成功删除“" + n + "”", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消",null).create();
                dialog.show();
                break;
            case 3:
                //拨号联系人菜单选项框，跳转至拨号程序后自动输入选中的联系人电话
                Contact c1 = contacts.get(menuInfo.position);
                String num = c1.getPhoneNum();
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:"+num));
                startActivity(intent2);
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void createDB(){
        MyOpenHelper myOpenHelper = new MyOpenHelper(this);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("contact", null,null,null,null,null,"_id");
        int count = cursor.getCount();
        if (count != 0){
            return;
        }
        ContentValues values = new ContentValues();
        values.put("name", "张三");
        values.put("phoneNum", "000001");
        values.put("company", "华为");
        values.put("vip", 1);
        db.insert("contact",null, values);
        db.close();
        Log.i("DB","1");
    }
}