package net.embsys.homeiot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    static final int REQUEST_CODE_LOGIN_ACTIVITY = 100;
    ClientThread clientThread;
    static Handler mainHandler;
    int bottomNavigationindex=0;
    Fragment1Home fragment1Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1Home = new Fragment1Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1Home).commit();
        mainHandler = new MainHandler();

    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String data = bundle.getString("msg");
            Log.d("MainActivity00", "TEST00 " + data );
            if (data.indexOf("New con") != -1) {
                clientThread.sendDataCheck("[" + ClientThread.dbId + "]" + "[" + "DATA@SEND\n" + "]");

                return;
            } else if((data.indexOf("Authentication Error") != -1) || (data.indexOf("Already logged") != -1)) {
                return;
            }
            if (data.indexOf("START") != -1) {
                fragment1Home.recvDataProcess(data);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.login:
                Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                intent.putExtra("serverIp",ClientThread.serverIp);
                intent.putExtra("serverPort",ClientThread.serverPort);
                intent.putExtra("clientId",ClientThread.clientId);
                intent.putExtra("clientPw",ClientThread.clientPw);
                startActivityForResult(intent,REQUEST_CODE_LOGIN_ACTIVITY);
                break;
            case R.id.setting:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_LOGIN_ACTIVITY) {
            if(resultCode == RESULT_OK)
            {
                ClientThread.serverIp = data.getStringExtra("serverIp")  ;
                ClientThread.serverPort = data.getIntExtra("serverPort",ClientThread.serverPort)  ;
                ClientThread.clientId = data.getStringExtra("clientId")  ;
                ClientThread.clientPw = data.getStringExtra("clientPw")  ;
                clientThread = new ClientThread(ClientThread.serverIp,ClientThread.clientId);
                clientThread.start();
//                fragment1Home.buttonSetText("서버 연결");
            } else {
                clientThread.stopClient();
                if(bottomNavigationindex == 0) {
                    //fragment1Home.buttonEnable(false);
                } else if(bottomNavigationindex == 2) {
                    //fragment3Telnet.buttonEnable(false);
                }
            }
        }
    }
}
