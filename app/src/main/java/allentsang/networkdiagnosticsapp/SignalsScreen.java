package allentsang.networkdiagnosticsapp;

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
import java.util.ArrayList;
import java.util.List;

public class SignalsScreen extends AppCompatActivity {

    WifiManager wifiManager;
    WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signals_screen);

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
                //Already on signals screen, refresh info instead
                refreshInfo(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshInfo(View view) {
        wifiManager = (WifiManager)getSystemService(this.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();

        TextView ssidContent = (TextView) findViewById(R.id.ssid_content);
        TextView ipContent = (TextView) findViewById(R.id.ip_content);

        if(wifiInfo.getSSID().equals("0x")) {
            ssidContent.setText("Connection not found");
            ipContent.setText("");
        } else {
            ssidContent.setText(wifiInfo.getSSID());
            int ipAddr = wifiInfo.getIpAddress();
            String ipString = String.format("%d.%d.%d.%d", (ipAddr & 0xff), (ipAddr >> 8 & 0xff), (ipAddr >> 16 & 0xff), (ipAddr >> 24 & 0xff));
            ipContent.setText(ipString);
        }

        //Populate signal list
        ArrayList<String> list = new ArrayList<String>();
        wifiManager.startScan();
        List<ScanResult> results = wifiManager.getScanResults();

        for(ScanResult sr : results) {
            String s = String.format("SSID:\t%s\nMAC:\t%s\nStrength:\t%d dBm", sr.SSID.equals("") ? "Name not found" : sr.SSID, sr.BSSID, sr.level);
            list.add(s);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ((ListView)findViewById(R.id.signal_list)).setAdapter(adapter);
    }
}
