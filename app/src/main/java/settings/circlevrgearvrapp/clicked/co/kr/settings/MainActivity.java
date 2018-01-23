package settings.circlevrgearvrapp.clicked.co.kr.settings;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioGroup;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipTxt = (EditText) findViewById(R.id.ipInput);
        portTxt = (EditText) findViewById(R.id.portInput);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        if(parseJSONData()){
            ipTxt.setText(new String(ip));
            portTxt.setText(new String(String.valueOf(port)));
            int index = Integer.parseInt(new String(userId));

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


    }

    public  void setJson()
    {
        JSONObject data = new JSONObject();
        int index = 0;

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
