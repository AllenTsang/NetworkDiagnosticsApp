package allentsang.networkdiagnosticsapp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
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
import java.util.Collection;

public class NetworkScreen extends AppCompatActivity {

    WifiManager wifiManager;
    WifiInfo wifiInfo;
    Context context = this;

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

        //Populate device list
        final ArrayList<String> list = new ArrayList<String>();

        WifiP2pManager P2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        WifiP2pManager.Channel c = P2pManager.initialize(this, getMainLooper(), null);

        P2pManager.discoverPeers(c, null);

        P2pManager.requestPeers(c, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                Collection<WifiP2pDevice> col = peers.getDeviceList();
                if(col.isEmpty()){
                    list.add("No devices detected.");
                } else {
                    for(WifiP2pDevice d : col) {
                        String s = String.format("Name:\t%s\nAddress:\t%s", d.deviceName, d.deviceAddress);
                        list.add(s);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
                ((ListView)findViewById(R.id.device_list)).setAdapter(adapter);
            }
        });
    }
}
