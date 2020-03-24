package com.example.maplefi.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class WifiUtil {
    private WifiManager wifiManager;

    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private final String TAG = "WIFIIUTILL";

    public WifiUtil(Context context, Activity activity){
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // Permission Check
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
    }

    // WifiManager Functions
    public boolean isWifiEnabled(){
        return this.wifiManager.isWifiEnabled();
    }
    public boolean setWifiEnabled(boolean enabled){
        return this.wifiManager.setWifiEnabled(enabled);
    }
    public List<ScanResult> getScanResults() {
        return this.wifiManager.getScanResults();
    }

    // Sub Functions
    public boolean connect(String ssid, String password, String capabilities) {
        // Ref. https://stackoverflow.com/questions/8818290/how-do-i-connect-to-a-specific-wi-fi-network-in-android-programmatically
        try {
            // [+] If Same Network Case
            // [-] Can't Connect Machine Connection Point

            Log.d(TAG, "[*] Connect SSID : " + ssid + "\n [+] Capabilities : " + capabilities + "\n [+] Password : " + password);

            String networkSSID = ssid;
            String networkPass = password;

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;

            // Check if security type is WEP
            if (capabilities.toUpperCase().contains("WEP")) {
                Log.v(TAG, "Configuring WEP");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                conf.wepKeys[0] = "\"" + networkPass + "\"";
                conf.wepTxKeyIndex = 0;

                // Check if security type is WPA
            } else if (capabilities.toUpperCase().contains("WPA")) {
                Log.v(TAG, "Configuring WPA");
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                conf.preSharedKey = "\"" + networkPass + "\"";

                // Check if network is open network
            } else {
                Log.v(TAG, "Configuring OPEN network");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.clear();
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            //Connect to the network
            int networkId = this.wifiManager.addNetwork(conf);
            Log.v(TAG, "Add result: " + networkId);

            List<WifiConfiguration> list = this.wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    Log.v(TAG, "WifiConfiguration SSID " + i.SSID);

                    boolean isDisconnected = this.wifiManager.disconnect();
                    Log.v(TAG, "isDisconnected : " + isDisconnected);

                    boolean isEnabled = this.wifiManager.enableNetwork(i.networkId, true);
                    Log.v(TAG, "isEnabled : " + isEnabled);

                    boolean isReconnected = this.wifiManager.reconnect();
                    Log.v(TAG, "isReconnected : " + isReconnected);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v(TAG, "[+] Connected ! ");
        return true;
    }
}