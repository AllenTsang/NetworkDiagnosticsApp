package allentsang.networkdiagnosticsapp;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {

    WifiManager wifiManager;
    WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        wifiManager = (WifiManager)getSystemService(this.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();

        ((TextView)findViewById(R.id.start_ssid_content)).setText(wifiInfo.getSSID());

        int ipAddr = wifiInfo.getIpAddress();
        String ipString = String.format("%d.%d.%d.%d", (ipAddr & 0xff), (ipAddr >> 8 & 0xff), (ipAddr >> 16 & 0xff), (ipAddr >> 24 & 0xff));
        ((TextView)findViewById(R.id.start_ip_content)).setText(ipString);

        Toolbar startToolbar = (Toolbar)findViewById(R.id.start_toolbar);
        setSupportActionBar(startToolbar);
    }
}
