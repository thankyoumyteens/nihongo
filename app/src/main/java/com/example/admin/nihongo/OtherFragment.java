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
import com.example.admin.nihongo.util.DatabaseOperation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
        view.findViewById(R.id.importWords).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    // 请求权限
                    requestPermissions(
                            new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                            2);
                } else {
                    importWords();
                }
            }
        });
        view.findViewById(R.id.wordsCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = getCount();
                Toast.makeText(getContext(), "单词总数: " + count, Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Word> list = DatabaseOperation.getAllWords();
                System.out.println();
            }
        });
        return view;
    }

    private int getCount() {
        String select = "select count(*) from JapaneseWords";
        int count = DatabaseOperation.getRecordCount(select, null);
        count--; // 去掉一条多余的数据
        return count;
    }

    private void importWords() {
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path, "words.json");
        try {
            if (!file.exists()) {
                Toast.makeText(getActivity(), "失败, words.json文件不存在于根目录", Toast.LENGTH_LONG).show();
            }
            FileReader reader = new FileReader(file);
            List<Word> list = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            char []buf = new char[1024];
            int len = 0;
            while((len = reader.read(buf)) != -1){
                sb.append(buf, 0, len);
            }
            reader.close();
            JSONArray array = new JSONArray(sb.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = (JSONObject) array.get(i);
                Word word = new Word();
                word.setId(item.getInt("Id"));
                word.setJapanese(item.getString("Japanese"));
                word.setKanJi(item.getString("KanJi"));
                word.setNominal(item.getString("Nominal"));
                word.setChinese(item.getString("Chinese"));
                list.add(word);
            }
            // 清空原来的单词
            String clear = "delete from JapaneseWords";
            DatabaseOperation.exec(clear, null);
            /**
             * 蜜汁bug: 表创建后第一次insert无效, 只好先insert一条无用数据
             */
            String first = "insert into JapaneseWords(Japanese, KanJi, Nominal, Chinese) values('','','','')";
            DatabaseOperation.exec(first, null);

            for (Word item : list) {
                String insert = "insert into JapaneseWords(Japanese, KanJi, Nominal, Chinese) values(" +
                        "'" + item.getJapanese() + "', " +
                        "'" + item.getKanJi() + "', " +
                        "'" + item.getNominal() + "', " +
                        "'" + item.getChinese() + "' " +
                        ")";
                DatabaseOperation.exec(insert, null);
            }
            Toast.makeText(getActivity(), "完成", Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
                List<Word> list = DatabaseOperation.getAllWords();

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
                writer.flush();
                writer.close();
                Toast.makeText(getActivity(), "完成, 文件是根目录下的word.json", Toast.LENGTH_LONG).show();
            }catch (Exception e) {
                Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {

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
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importWords();
                } else {
                    Toast.makeText(getActivity(), "请授权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
