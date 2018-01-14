package settings.circlevrgearvrapp.clicked.co.kr.settings;

import android.Manifest;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    String ip;
    int port;
    String userId;

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
            }
        });
        

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
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    void ReadFile (){
        try {
            File yourFile = new File(Environment.getExternalStorageDirectory(),"/circlevr/config.json");
            FileInputStream stream = new FileInputStream(yourFile);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                stream.close();
            }
            JSONObject jsonObj = new JSONObject(jsonStr);

            ip = jsonObj.getString("gcsAddress");
            port = jsonObj.getInt("gcsPort");
            userId = jsonObj.getString("UserID");
                // tmp hashmap for single node
                //HashMap<String, String> parsedData = new HashMap<String, String>();
            Toast.makeText(this, "Json error", Toast.LENGTH_SHORT).show();
                // adding each child node to HashMap key => value
                //parsedData.put("gcsAddress", ip);
                //parsedData.put("gcsPort", port);
                //parsedData.put("UserID", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void ReadConfigJson()
    {
        //asset 폴더의 내용을 가져오기
        AssetManager assetManager = getResources().getAssets();

        try{
            //사용하고자 하는 json 파일 open
            AssetManager.AssetInputStream ais = (AssetManager.AssetInputStream)assetManager.open("/sdcard/circlevr/config.json");

            //stream을 리더로 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(ais));

            //StringBuilder 사용
            StringBuilder sb = new StringBuilder();

            //json파일의 내용이 용량이 클경우 Stirng 의 허용점인 4096 byte 를 넘어가면 오류발생
            int bufferSize = 1024 * 1024;

            //char 로 버프 싸이즈 만큼 담기위해 선언
            char readBuf [] = new char[bufferSize];
            int resultSize = 0;

            //파일의 전체 내용 읽어오기
            while((resultSize = br.read(readBuf))  != -1){
                if(resultSize == bufferSize){
                    sb.append(readBuf);
                }else{
                    for(int i = 0; i < resultSize; i++){
                        //StringBuilder 에 append
                        sb.append(readBuf[i]);
                    }
                }
            }
            // 수정 - 새로운 문자열을 만들어서 내부 버퍼의 내용을 복사하고 반환한다.
            String jString = sb.toString();

            //JSONObject 얻어 오기
            JSONObject jsonObject =  new JSONObject(jString);

            //json value 값 얻기
            ip = jsonObject.getString("gcsAddress");
            port = Integer.parseInt(jsonObject.getString("gcsPort"));
            userId = jsonObject.getString("UserID");
        }catch(JSONException je){
            Toast.makeText(this, "Json error", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, "파일이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
