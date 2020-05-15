package com.example.maplefi.util;

    /*평가 항목
    1. 무선랜 단말에 로그온 암호 적용? (o,x) ->완
    2. SSID 숨김모드로 설정?(o,x) ->완
    3. 사용자 인증과 데이터 암호화를 위해 AP에서 제공하는 암호기능 제공?(O,x) -> 완
    4. 무선랜 도청 가능 난이도? -> eap tls, peap 등 판별하여 고려// eap-tls 등의 세부정보를 받아올 수 있을지 몰라서 일단은 둠
    5. 사용하고 있는 무선랜의 인증방식 보안 강도-> 인증 방식별 분기 , 방식별 점수 배분 ->따로 할것인가..? 고민스럽
    6. 무선랜에서 사용하고 있는 전송 데이터 암호화 방식의 강도는? -> 프로토콜에서 사용하는 암호화 방식별분기 ,  강도 점수
    -> 따로 함수 만들어서? 합쳐?-> 완 (따로)
    */

import android.util.Log;
import java.util.Objects;

public class SecurityEstimater {
    //test 용 임의 정보들
//    ApItem ap_Item = null;
    String TAG = "SecurityEstimater";
    int scores[] = new int[5];   /* = new int[5];*///score array
    //0 : 총점,  1 : pw, 2 : packet,packet rule , 3 : packet snif, 4 : ssid,

    public SecurityEstimater(String ap_name, String capabilities, int eap_type) {
        Log.d(TAG, "SecurityEstimater: after apItem set");
//        int scores[] = new int[5];   //score array
        for(int i=0; i<5; i++){
            scores[i] = 0;
            Log.d(TAG, "SecurityEstimater: 초기화 후 값 확인 "+i+"번째"+scores[i]);
        }

        checkPassword(capabilities);
        checkSsid(ap_name);
        checkAsign(capabilities);
        checkEnc(capabilities);
//        checkPacketsnif(eap_type);

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
//            scores[1] += 50;//pwchecker 개별 점수// 기본점수를 제하자
////            ap_Item.addSec_Score(50);//임의
//            Log.d(TAG, "pw_checker: pwtype = " + scores);

            if (capabilities.contains("WEP")) {
                Log.d(TAG, "pw_checker: wep");
//                ap_Item.addSec_Score(10);
                scores[1] += 10;
                scores[3] += 0;

                Log.d(TAG, "pw_checker: " + scores[1]);
            }
            else if (capabilities.contains("WPA-")) {
                Log.d(TAG, "pw_checker: wpa");
//                ap_Item.addSec_Score(20);
                scores[1] += 20;
                scores[3] += 20;
                Log.d(TAG, "pw_checker: score[1] = " + scores[1]);
            }
            else if (capabilities.contains("WPA2")) {
                Log.d(TAG, "pw_checker: wpa2");
//                capabilities.addSec_Score(40);
                scores[1] += 40;
                scores[3] += 30;

                Log.d(TAG, "pw_checker: score[1] = " + scores[1]);

            }
            else if (capabilities.contains("RSN")) {
                Log.d(TAG, "pw_checker: wpa2");
//                capabilities.addSec_Score(40);
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
//            ap_Item.addSec_Score(60);
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
//            ap_Item.addSec_Score(30);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
            scores[2] += 30;
            scores[3] += 30;
            for(int i=0; i<5; i++){
                Log.d(TAG, "SecurityEstimater: checkAsign 후 "+i+"번째"+scores[i]);
            }

        } else if (capabilities.contains("EAP")) {
            Log.d(TAG, "packet_checker: protocol eap");
//            ap_Item.addSec_Score(40);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
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
//           ap_Item.addSec_Score(20);
//           Log.d(TAG, "packetRule_checker: grade " + ap_Item.getSecScore());
           scores[2] += 40;
           scores[3] += 40;
       }else if (capabilities.contains("TKIP")) {
               Log.d(TAG, "packet_checker: protocol tkip");//wep에서 사용하는 암호화 방식
//            ap_Item.addSec_Score(10);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
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


