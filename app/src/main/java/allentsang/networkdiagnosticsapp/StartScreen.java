package allentsang.networkdiagnosticsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartScreen extends AppCompatActivity {

    WifiManager wifiManager;
    WifiInfo wifiInfo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshInfo(null);

        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_wifi:
                //Go to signals screen
                Intent intent = new Intent(this, SignalsScreen.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshInfo(View view) {
        wifiManager = (WifiManager)getSystemService(this.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        Button scanButton = (Button)findViewById(R.id.scan_network_button);
        TextView ssidContent = (TextView) findViewById(R.id.ssid_content);
        TextView ipContent = (TextView) findViewById(R.id.ip_content);

        if(wifiInfo.getSSID().equals("0x")) {
            ssidContent.setText("Connection not found");
            scanButton.setEnabled(false);
            ipContent.setText("");
        } else {
            ssidContent.setText(wifiInfo.getSSID());
            scanButton.setEnabled(true);
            int ipAddr = wifiInfo.getIpAddress();
            String ipString = String.format("%d.%d.%d.%d", (ipAddr & 0xff), (ipAddr >> 8 & 0xff), (ipAddr >> 16 & 0xff), (ipAddr >> 24 & 0xff));
            ipContent.setText(ipString);
        }
    }

    public void scanNetwork(View view) {
        //Go to network screen
        //Toast.makeText(getApplication(), "Go to network screen", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NetworkScreen.class);
        startActivity(intent);
    }
}
