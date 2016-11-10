package allentsang.networkdiagnosticsapp;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NetworkScreen extends AppCompatActivity {

    WifiManager wifiManager;
    WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_screen);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshInfo(null);
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

        ((TextView)findViewById(R.id.ssid_content)).setText(wifiInfo.getSSID());

        int ipAddr = wifiInfo.getIpAddress();
        String ipString = String.format("%d.%d.%d.%d", (ipAddr & 0xff), (ipAddr >> 8 & 0xff), (ipAddr >> 16 & 0xff), (ipAddr >> 24 & 0xff));
        ((TextView)findViewById(R.id.ip_content)).setText(ipString);

        //Populate device list
        ArrayList<String> list = new ArrayList<String>();
        wifiManager.startScan();
        List<ScanResult> results = wifiManager.getScanResults();

        for(ScanResult sr : results) {
            String s = String.format("SSID:\t%s\nStrength:\t%d dBm", sr.SSID, sr.level);
            list.add(s);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ((ListView)findViewById(R.id.device_list)).setAdapter(adapter);
    }
}
