package com.example.maplefi.util;


import android.util.Log;
import java.util.Objects;

public class SecurityEstimater {
    //test 용 임의 정보들
    String TAG = "SecurityEstimater";
    int scores[] = new int[5];
    //0 : 총점,  1 : pw, 2 : packet,packet rule , 3 : packet snif, 4 : ssid,

    public SecurityEstimater(String ap_name, String capabilities, int eap_type) {
        Log.d(TAG, "SecurityEstimater: after apItem set");
        for(int i=0; i<5; i++){
            scores[i] = 0;
            Log.d(TAG, "SecurityEstimater: 초기화 후 값 확인 "+i+"번째"+scores[i]);
        }

        checkPassword(capabilities);
        checkSsid(ap_name);
        checkAsign(capabilities);
        checkEnc(capabilities);

        for(int i=1; i<5; i++){
            scores[0]+=scores[i];
            Log.d(TAG, "SecurityEstimater: checkAsign 후 "+i+"번째"+scores[i]);
        }


        Log.d(TAG, "SecurityEstimater: final = " + scores[0]);
    }

    //포지션에 해당하는 리스트의 로그온 암호적용, 암호화 방식평가 여부
    public void checkPassword(String capabilities) {
        if (!(capabilities.toUpperCase().isEmpty())) {
            Log.d(TAG, "pw_checker: pwtype 존재");

            if (capabilities.contains("WEP")) {
                Log.d(TAG, "pw_checker: wep");
                scores[1] += 10;
                scores[3] += 0;

                Log.d(TAG, "pw_checker: " + scores[1]);
            }
            else if (capabilities.contains("WPA-")) {
                Log.d(TAG, "pw_checker: wpa");
                scores[1] += 20;
                scores[3] += 20;
                Log.d(TAG, "pw_checker: score[1] = " + scores[1]);
            }
            else if (capabilities.contains("WPA2")) {
                Log.d(TAG, "pw_checker: wpa2");
                scores[1] += 40;
                scores[3] += 30;

                Log.d(TAG, "pw_checker: score[1] = " + scores[1]);

            }
            else if (capabilities.contains("RSN")) {
                Log.d(TAG, "pw_checker: wpa2");
                scores[1] += 40;
                scores[3] += 30;

                Log.d(TAG, "pw_checker: score[1] = " + scores[1]);

            }

            for(int i=0; i<5; i++){
                Log.d(TAG, "SecurityEstimater: checkpassword 후 "+i+"번째"+scores[i]);
            }

        }
    }

    //ssid check
    public void checkSsid(String ap_name) {
        Log.d(TAG, "ssid_checker: 여기");
        if (Objects.equals(ap_name, "숨겨진 네트워크")) {//숨김모드일경우
            Log.d(TAG, "ssid_checker: 숨김모드");
            scores[4] += 60;
        } else {
            Log.d(TAG, "ssid_checker: 숨김모드 아님");
        }
        scores[0] += scores[4];
        for(int i=0; i<5; i++){
            Log.d(TAG, "SecurityEstimater: ssid 후 "+i+"번째"+scores[i]);
        }

    }

    //프로토콜 방식 판별
    public void checkAsign(String capabilities) {
        if (capabilities.contains("PSK")) {
            Log.d(TAG, "packet_checker: protocol psk");
            scores[2] += 30;
            scores[3] += 30;
            for(int i=0; i<5; i++){
                Log.d(TAG, "SecurityEstimater: checkAsign 후 "+i+"번째"+scores[i]);
            }

        } else if (capabilities.contains("EAP")) {
            Log.d(TAG, "packet_checker: protocol eap");
            scores[2] += 40;
            scores[3] += 40;
            for(int i=0; i<5; i++){
                Log.d(TAG, "SecurityEstimater: checkAsign 후 "+i+"번째"+scores[i]);
            }

        } else {
            Log.d(TAG, "packet_checker: not in our packet case");
            Log.d(TAG, "packet_checker: "+ scores);
        }

        for(int i=0; i<5; i++){
            Log.d(TAG, "SecurityEstimater: checkAsign 후 "+i+"번째"+scores[i]);
        }

    }


    //패킷 암호화 규칙 체크
    public void checkEnc(String capabilities){
       if (capabilities.contains("CCMP")) {
           Log.d(TAG, "check_Enc: ccmp");
           scores[2] += 40;
           scores[3] += 40;
       }else if (capabilities.contains("TKIP")) {
               Log.d(TAG, "packet_checker: protocol tkip");//wep에서 사용하는 암호화 방식
               scores[2] += 20;
               scores[3] += 20;
               Log.d(TAG, "packet_checker: "+ scores[2]);
       } else {
           Log.d(TAG, "packetRule_checker: not in packetrule case");
       }

        for(int i=0; i<5; i++){
            Log.d(TAG, "SecurityEstimater: checkEnc 후 "+i+"번째"+scores[i]);
        }

    }

    public int[] getScores() {
       return this.scores;
    }

    public int getFinal(){
        return this.scores[0];
    }
}


