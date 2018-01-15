package settings.circlevrgearvrapp.clicked.co.kr.settings;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    boolean isLoaded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        Button loadBtn = (Button) findViewById(R.id.loadBtn);
        Button exitBtn = (Button) findViewById(R.id.exitBtn);

        final EditText ipTxt = (EditText) findViewById(R.id.ipInput);
        final EditText portTxt = (EditText) findViewById(R.id.portInput);
        final EditText userIDTxt = (EditText) findViewById(R.id.userIdInput);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseJSONData();
                ipTxt.setText(new String(ip));
                portTxt.setText(new String(String.valueOf(port)));
                userIDTxt.setText((new String(userId)));
                isLoaded = true;
            }
        });
        
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoaded == false) {
                    Toast.makeText(getApplicationContext(), "데이터를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("gcsAddress", ipTxt.getText().toString());
                        data.put("gcsPort", Integer.parseInt(portTxt.getText().toString()));
                        data.put("UserID", userIDTxt.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    parseStringData(data);
                }
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    public void parseStringData(Object inputObject)
    {
        try{
            FileOutputStream fOut = new FileOutputStream("/sdcard/circlevr/config.json");
            byte[] bytes = inputObject.toString().getBytes();
            fOut.write(bytes);
            fOut.close();
            Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void parseJSONData()
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
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
