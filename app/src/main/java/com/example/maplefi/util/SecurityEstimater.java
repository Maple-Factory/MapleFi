package com.example.maplefi.util;

    /*평가 항목
    1. 무선랜 단말에 로그온 암호 적용? (o,x) ->완
    2. SSID 숨김모드로 설정?(o,x) ->완
    3. 사용자 인증과 데이터 암호화를 위해 AP에서 제공하는 암호기능 제공?(O,x) -> 완
    4. 무선랜 도청 가능?(o,x) -> eap tls, peap 등 판별하여 고려// eap-tls 등의 세부정보를 받아올 수 있을지 몰라서 일단은 둠
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
    int score[] = new int[5];//score array
    //0 : 총점,  1 : pw, 2 : packet,packet rule , 3 : packet snif, 4 : ssid,

    public SecurityEstimater(String ap_name, String capabilities, int eap_type) {
        Log.d(TAG, "SecurityEstimater: after apItem set");
        int score[] = new int[5];//score array
        Log.d(TAG, "SecurityEstimater: 계산 전 " +score[0]);

        pw_checker(capabilities);
        ssid_checker(ap_name);
        packet_checker(capabilities);
        packetRule_checker(capabilities);
        packetsnif_checker(eap_type);

        Log.d(TAG, "SecurityEstimater: final = " + score[0]);

    }

    //포지션에 해당하는 리스트의 로그온 암호적용, 암호화 방식평가 여부
    public void pw_checker(String capabilities) {
        if (!(capabilities.toUpperCase().isEmpty())) {
            Log.d(TAG, "pw_checker: pwtype 존재");
            score[1] += 50;//pwchecker 개별 점수
//            ap_Item.addSec_Score(50);//임의
            Log.d(TAG, "pw_checker: pwtype = " + score);

            if (capabilities.contains("WPA")) {
                Log.d(TAG, "pw_checker: wpa");
//                ap_Item.addSec_Score(20);
                score[1] += 20;
                Log.d(TAG, "pw_checker: score[1] = " + score[1]);
            }
            if (capabilities.contains("WPA2")) {
                Log.d(TAG, "pw_checker: wpa2");
//                capabilities.addSec_Score(40);
                score[1] += 40;
                Log.d(TAG, "pw_checker: score[1] = " + score[1]);

            }
            if (capabilities.contains("WEP")) {
                Log.d(TAG, "pw_checker: wep");
//                ap_Item.addSec_Score(10);
                score[1] += 10;
                Log.d(TAG, "pw_checker: " + score[1]);
            }
            score[0] += score[1];// 총점에 개별점수 더하기
            Log.d(TAG, "pw_checker: score[1] = " + score[1]);
            Log.d(TAG, "pw_checker: score[0] = "+score[0]);

        }
    }

    //ssid check
    public void ssid_checker(String ap_name) {
        Log.d(TAG, "ssid_checker: 여기");
        if (Objects.equals(ap_name, "")) {//숨김모드일경우
            Log.d(TAG, "ssid_checker: 숨김모드");
//            ap_Item.addSec_Score(60);
            score[4] += 60;
        } else {
            Log.d(TAG, "ssid_checker: 숨김모드 아님");
        }
        score[0] += score[4];
    }

    //프로토콜 방식 판별
    public void packet_checker(String capabilities) {
        if (capabilities.contains("PSK")) {
            Log.d(TAG, "packet_checker: protocol psk");
//            ap_Item.addSec_Score(30);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
            score[2] += 30;
            Log.d(TAG, "packet_checker: "+score);

        } else if (capabilities.contains("EAP")) {
            Log.d(TAG, "packet_checker: protocol eap");
//            ap_Item.addSec_Score(40);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
            score[2] += 40;
            Log.d(TAG, "packet_checker: "+score);
            //just

        } else {
            Log.d(TAG, "packet_checker: not in our packet case");
            Log.d(TAG, "packet_checker: "+score);
        }

        if (capabilities.contains("TKIP")) {
            Log.d(TAG, "packet_checker: protocol tkip");//wep에서 사용하는 암호화 방식
//            ap_Item.addSec_Score(10);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
            score[2] += 10;
            Log.d(TAG, "packet_checker: "+score);
        }
        score[0] += score[2];
        Log.d(TAG, "packet_checker: "+score);
    }


        //패킷 규칙 체크 ccmp -> 필요한가??
    public void packetRule_checker (String capabilities){
       if (capabilities.contains("CCMP")) {
          Log.d(TAG, "packetRule_checker: ccmp");
//           ap_Item.addSec_Score(20);
//           Log.d(TAG, "packetRule_checker: grade " + ap_Item.getSecScore());
           score[2] += 20;
       } else {
           Log.d(TAG, "packetRule_checker: not in packetrule case");
       }
       score[0] += score[2];
        Log.d(TAG, "packetRule_checker: "+score[0]);
    }

    public void packetsnif_checker (int eap_type){//eap -tls등 고려하여 도청 가능한지 판별
        if (eap_type == -1) {
            Log.d(TAG, "packetsnif_checker: this is not eap");
        } else {
            Log.d(TAG, "packetsnif_checker: not in our eap case");
//            score[3] += 50;
        }
        score[0] += score[3];
    }

    public int[] getScore () {
       return this.score;
    }

}


