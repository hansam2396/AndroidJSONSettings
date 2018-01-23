package settings.circlevrgearvrapp.clicked.co.kr.settings;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String ip;
    int port;
    String userId;

    EditText ipTxt;
    EditText portTxt;

    Button btn_0;
    Button btn_1;
    Button btn_2;
    Button btn_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipTxt = (EditText) findViewById(R.id.ipInput);
        portTxt = (EditText) findViewById(R.id.portInput);
        btn_0 = (Button) findViewById(R.id.btn0);
        btn_1 = (Button) findViewById(R.id.btn1);
        btn_2 = (Button) findViewById(R.id.btn2);
        btn_3 = (Button) findViewById(R.id.btn3);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        if(parseJSONData()){
            ipTxt.setText(new String(ip));
            portTxt.setText(new String(String.valueOf(port)));
            int index = Integer.parseInt(new String(userId));

            if(index == 0)
                btn_0.setPressed(true);
            else if(index == 1)
                btn_1.setPressed(true);
            else if(index == 2)
                btn_2.setPressed(true);
            else if(index == 3)
                btn_3.setPressed(true);
        }

        ipTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setJson();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        portTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setJson();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_0.setPressed(true);
                btn_1.setPressed(false);
                btn_2.setPressed(false);
                btn_3.setPressed(false);

                setJson();
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_0.setPressed(false);
                btn_1.setPressed(true);
                btn_2.setPressed(false);
                btn_3.setPressed(false);

                setJson();
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_0.setPressed(false);
                btn_1.setPressed(false);
                btn_2.setPressed(true);
                btn_3.setPressed(false);

                setJson();
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_0.setPressed(false);
                btn_1.setPressed(false);
                btn_2.setPressed(false);
                btn_3.setPressed(true);

                setJson();
            }
        });
    }

    public  void setJson()
    {
        JSONObject data = new JSONObject();
        int index=0;

        if(btn_0.isPressed())
            index = 0;
        else if(btn_1.isPressed())
            index = 1;
        else if(btn_2.isPressed())
            index = 2;
        else if(btn_3.isPressed())
            index = 3;

        try {
            data.put("gcsAddress", ipTxt.getText().toString());
            data.put("gcsPort", Integer.parseInt(portTxt.getText().toString()));
            data.put("UserID", String.valueOf(index));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        parseStringData(data);
    }

    public void parseStringData(Object inputObject)
    {
        try{
            FileOutputStream fOut = new FileOutputStream("/sdcard/circlevr/config.json");
            byte[] bytes = inputObject.toString().getBytes();
            fOut.write(bytes);
            fOut.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean parseJSONData()
    {
        String JSONString = null;
        JSONObject JSONObject = null;
        try{
            FileInputStream is = new FileInputStream("/sdcard/circlevr/config.json");
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            is.close();
            JSONString = new String(bytes, "UTF-8");
            JSONObject = new JSONObject(JSONString);

            ip = JSONObject.getString("gcsAddress");
            port = JSONObject.getInt("gcsPort");
            userId = JSONObject.getString("UserID");

            Toast.makeText(this, "로드 완료", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
