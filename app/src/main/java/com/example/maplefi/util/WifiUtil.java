package com.example.maplefi.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class WifiUtil {
    private WifiManager wifiManager;

    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private final String TAG = "WIFIIUTILL";

    public WifiUtil(@NonNull Context context, @NonNull Activity activity){
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
    public WifiInfo getConnectionInfo(){
        return this.wifiManager.getConnectionInfo();
    }
    public WifiManager getWifiManager(){    // For Debug
        return this.wifiManager;
    }

    // Sub Functions
    public void scan(){
        this.wifiManager.startScan();
    }
    public void disconnect(){
        this.wifiManager.disconnect();
    }
    public boolean connect(int net_id){
        boolean isDisconnected = this.wifiManager.disconnect();
        Log.v(TAG, "isDisconnected : " + isDisconnected);

        boolean isEnabled = this.wifiManager.enableNetwork(net_id, true);
        Log.v(TAG, "isEnabled : " + isEnabled);

        boolean isReconnected = this.wifiManager.reconnect();
        Log.v(TAG, "isReconnected : " + isReconnected);

        if(isEnabled & isReconnected) return true;
        return false;
    }
    public boolean connect(String ssid){
        int net_id = getProfileId(ssid);
        if(net_id != -1) {
            connect(net_id);
            return true;
        }
        return false;
    }
    public boolean connect(String ssid, String capabilities) {
        try {
            // [-] Can't Connect Machine Connection Point
            Log.d(TAG, "[*] Connect SSID : " + ssid + "\n [+] Capabilities : " + capabilities);

            // Connect to the network
            int net_id = getProfileId(ssid);
            if(net_id == -1) {
                net_id = addProfile(ssid, capabilities);
            }

            connect(net_id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Log.v(TAG, "[+] Connected ! ");
        return true;
    }
    public boolean connect(String ssid, String capabilities, String password) {
        // Ref. https://stackoverflow.com/questions/8818290/how-do-i-connect-to-a-specific-wi-fi-network-in-android-programmatically
        try {
            // [-] Can't Connect Machine Connection Point
            Log.d(TAG, "[*] Connect SSID : " + ssid + "\n [+] Capabilities : " + capabilities + "\n [+] Password : " + password);

            // Connect to the network
            int net_id = getProfileId(ssid);
            if(net_id == -1) {
                net_id = addProfile(ssid, capabilities, password);
            }

            connect(net_id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Log.v(TAG, "[+] Connected ! ");
        return true;
    }

    public String getCapabilities(String bssid){
        List<ScanResult> networkList = wifiManager.getScanResults();
        if (networkList != null) {
            for (ScanResult network : networkList){
                if(network.BSSID == bssid) {
                    return network.capabilities;
                }
            }
        }
        return "";  // 예외 처리 구문 필요
    }
    public boolean isNeedPassword(@NonNull String capabilities){
        String cap = capabilities.toUpperCase();
        if(cap.contains("WEP")) return true;
        if(cap.contains("WPA")) return true;
        return false;
    }

    public int getProfileId(String ssid){
        List<WifiConfiguration> list = this.wifiManager.getConfiguredNetworks();
        for (WifiConfiguration wifi_conf : list) {
            if (wifi_conf.SSID != null && wifi_conf.SSID.equals("\"" + ssid + "\"")) {
                return wifi_conf.networkId;
            }
        }
        return -1;
    }
    public int addProfile(String ssid, String capabilities){
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + ssid + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;

            // Check if network is open network
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

            //Connect to the network
            int net_id = this.wifiManager.addNetwork(conf);
            Log.v(TAG, "Add result: " + net_id);

            return net_id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int addProfile(String ssid, String capabilities, String password){
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + ssid + "\"";   // Please note the quotes. String should contain ssid in quotes
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

                conf.wepKeys[0] = "\"" + password + "\"";
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

                conf.preSharedKey = "\"" + password + "\"";
            }
            else {
                // Exception
                Log.d(TAG,"addProfile - password logic - capabilites else");
                return -1;
            }

            //Connect to the network
            int net_id = this.wifiManager.addNetwork(conf);
            Log.v(TAG, "Add result: " + net_id);

            return net_id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public void removeProfile(int net_id){
        wifiManager.removeNetwork(net_id);
    }
    public void removeProfile(String ssid){
        int net_id = getProfileId(ssid);
        wifiManager.removeNetwork(net_id);
    }

    // ScanResult Processing Functions
    public String parseEapType(@NonNull String to_stringed){
        String front_delimiter = ", Carrier AP EAP Type: ";
        String back_delimiter = ", Carrier name: ";

        int front_i = to_stringed.indexOf(front_delimiter);
        int front_len = front_delimiter.length();
        int back_i = to_stringed.indexOf(back_delimiter);

        return to_stringed.substring(front_i + front_len, back_i);
    }
}
