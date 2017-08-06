package com.example.admin.nihongo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin.nihongo.model.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static com.example.admin.nihongo.MainActivity.database;


public class OtherFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        view.findViewById(R.id.saveWords).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    // 请求权限
                    requestPermissions(
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            1);
                } else {
                    writeToFile();
                }
            }
        });
        return view;
    }

    private void writeToFile() {
        // 判断手机是否插入SD卡
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取SD卡路径
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path, "words.json");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
//                FileOutputStream out = new FileOutputStream(file);
                FileWriter writer = new FileWriter(file);
                List<Word> list = new ArrayList<>();

                String queryString = "select Id, Japanese, KanJi, Nominal, Chinese from JapaneseWords";
                //查询获得游标
                Cursor cursor = database.rawQuery(queryString, null);
                //判断游标是否为空
                if(cursor.moveToFirst()) {
                    //遍历游标
                    while(cursor.moveToNext()) {
                        int id = cursor.getInt(0);
                        String japanese = cursor.getString(1);
                        String kanJi = cursor.getString(2);
                        String nominal = cursor.getString(3);
                        String chinese = cursor.getString(4);
                        list.add(new Word(id, japanese, kanJi, nominal, chinese));
                    }
                    cursor.close();
                }
                JSONArray array = new JSONArray();
                for (Word item : list) {
                    JSONObject object = new JSONObject();
                    object.put("Id", item.getId());
                    object.put("Japanese", item.getJapanese());
                    object.put("KanJi", item.getKanJi());
                    object.put("Nominal", item.getNominal());
                    object.put("Chinese", item.getChinese());
                    array.put(object);
                }
                String buffer = array.toString();
                writer.write(buffer);
                Toast.makeText(getActivity(), "完成, 文件是根目录下的word.json", Toast.LENGTH_LONG).show();
            }catch (Exception e) {
                Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeToFile();
                } else {
                    Toast.makeText(getActivity(), "请授权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
