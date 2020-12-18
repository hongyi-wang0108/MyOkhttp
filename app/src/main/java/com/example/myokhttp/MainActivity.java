package com.example.myokhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button btn_okhttp,btn_pull,btn_sax,btn_josnobject,btn_gson;
    private TextView tv_okhttp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_okhttp = findViewById(R.id.btn_okhttp);
        btn_pull = findViewById(R.id.btn_pull);
        btn_sax = findViewById(R.id.btn_sax);
        tv_okhttp = findViewById(R.id.tv_okhttp);
        btn_josnobject = findViewById(R.id.btn_josnobject);
        btn_gson = findViewById(R.id.btn_gson);
        String resdata ="<apps>\n" +
                "    <app>\n" +
                "        <id>1<id>\n" +
                "        <name>map1<name>\n" +
                "        <version>1.0<version>\n" +
                "    </app>\n" +
                "    <app>\n" +
                "        <id>2<id>\n" +
                "        <name>map2<name>\n" +
                "        <version>2.0<version>\n" +
                "    </app>\n" +
                "    <app>\n" +
                "        <id>3<id>\n" +
                "        <name>map3<name>\n" +
                "        <version>3.0<version>\n" +
                "    </app>\n" +
                "</apps>";
        btn_gson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        List<App> list = gson.fromJson(resdata,new TypeToken<List<App>>(){}.getType());
                        for (App app : list) {
                            String id = app.getId();
                            String name = app.getName();
                            String version = app.getVersion();
                            Log.d("gson", "从这开始" + id + name + version);
                        }
                    }
                }).start();
            }
        });
        btn_josnobject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //1.request.uri("...json")
                        try {
                            JSONArray jsonArray = new JSONArray(resdata);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String version = jsonObject.getString("version");
                                Log.d("json", "从这开始" + id + name + version);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btn_sax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SAXParserFactory factory = SAXParserFactory.newInstance();
                            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
                            MyContentHandler handler = new MyContentHandler();
                            xmlReader.setContentHandler(handler);
                            xmlReader.parse(new InputSource(new StringReader(resdata)));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
        btn_pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(resdata));
            int enventype = parser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (enventype != XmlPullParser.END_DOCUMENT){
                String nodename = parser.getName();
                switch (enventype){
                    case XmlPullParser.START_TAG:
                        if("id".equals(nodename)){
                            id = parser.nextText();
                        }else if("name".equals(nodename)){
                            name =  parser.nextText();
                        }else if("verison".equals(nodename)){
                            version =  parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("app".equals(nodename)){
                            Log.d("TAG", "从这开始" + id + name + version);
                        }
                        break;
                }
                enventype = parser.next();
            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btn_okhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody body = new FormBody.Builder()
                                    .add("name","admin")
                                    .add("password","111")
                                    .build();
                            Request request = new Request.Builder()
                                .url("http://www.baidu.com")
                                .post(body)
                                .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String data = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_okhttp.setText(data);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }
}