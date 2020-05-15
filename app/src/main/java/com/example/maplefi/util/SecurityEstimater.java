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
    int scores[] = new int[5];//score array
    //0 : 총점,  1 : pw, 2 : packet,packet rule , 3 : packet snif, 4 : ssid,

    public SecurityEstimater(String ap_name, String capabilities, int eap_type) {
        Log.d(TAG, "SecurityEstimater: after apItem set");
//        int scores[] = new int[5];   //score array
        Log.d(TAG, "SecurityEstimater: 계산 전 " +scores[0]);

        checkPassword(capabilities);
        checkSsid(ap_name);
        checkAsign(capabilities);
        checkEnc(capabilities);
//        checkPacketsnif(eap_type);

        Log.d(TAG, "SecurityEstimater: final = " + scores[0]);
    }

    //포지션에 해당하는 리스트의 로그온 암호적용, 암호화 방식평가 여부
    public void checkPassword(String capabilities) {
        if (!(capabilities.toUpperCase().isEmpty())) {
            Log.d(TAG, "pw_checker: pwtype 존재");
//            scores[1] += 50;//pwchecker 개별 점수// 기본점수를 제하자
////            ap_Item.addSec_Score(50);//임의
//            Log.d(TAG, "pw_checker: pwtype = " + scores);

            if (capabilities.contains("WPA")) {
                Log.d(TAG, "pw_checker: wpa");
//                ap_Item.addSec_Score(20);
                scores[1] += 20;
                scores[3] += 20;
                Log.d(TAG, "pw_checker: score[1] = " + scores[1]);
            }
            if (capabilities.contains("WPA2")) {
                Log.d(TAG, "pw_checker: wpa2");
//                capabilities.addSec_Score(40);
                scores[1] += 40;
                scores[3] += 30;

                Log.d(TAG, "pw_checker: score[1] = " + scores[1]);

            }
            if (capabilities.contains("WEP")) {
                Log.d(TAG, "pw_checker: wep");
//                ap_Item.addSec_Score(10);
                scores[1] += 10;
                scores[3] += 0;

                Log.d(TAG, "pw_checker: " + scores[1]);
            }
            scores[0] += scores[1];// 총점에 개별점수 더하기
            scores[0] += scores[3];//총점에 패킷도청점수 더하기
            Log.d(TAG, "pw_checker: score[1] = " + scores[1]);
            Log.d(TAG, "pw_checker: score[0] = "+ scores[0]);

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
    }

    //프로토콜 방식 판별
    public void checkAsign(String capabilities) {
        if (capabilities.contains("PSK")) {
            Log.d(TAG, "packet_checker: protocol psk");
//            ap_Item.addSec_Score(30);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
            scores[2] += 30;
            scores[3] += 30;
            Log.d(TAG, "packet_checker: "+ scores);

        } else if (capabilities.contains("EAP")) {
            Log.d(TAG, "packet_checker: protocol eap");
//            ap_Item.addSec_Score(40);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
            scores[2] += 40;
            scores[3] += 40;
            Log.d(TAG, "packet_checker: "+ scores);
            //just

        } else {
            Log.d(TAG, "packet_checker: not in our packet case");
            Log.d(TAG, "packet_checker: "+ scores);
        }
        scores[0] += scores[2];
        scores[0] += scores[3];
        Log.d(TAG, "packet_checker: "+ scores);
    }


    //패킷 암호화 규칙 체크
    public void checkEnc(String capabilities){
       if (capabilities.contains("CCMP")) {
           Log.d(TAG, "packetRule_checker: ccmp");
//           ap_Item.addSec_Score(20);
//           Log.d(TAG, "packetRule_checker: grade " + ap_Item.getSecScore());
           scores[2] += 30;
           scores[3] += 30;
       }else if (capabilities.contains("TKIP")) {
               Log.d(TAG, "packet_checker: protocol tkip");//wep에서 사용하는 암호화 방식
//            ap_Item.addSec_Score(10);
//            Log.d(TAG, "packet_checker: " + ap_Item.getSecScore());
               scores[2] += 20;
               scores[3] += 20;
               Log.d(TAG, "packet_checker: "+ scores);
       } else {
           Log.d(TAG, "packetRule_checker: not in packetrule case");
       }
       scores[0] += scores[2];
       scores[0] += scores[3];
        Log.d(TAG, "packetRule_checker: "+ scores[0]);
    }

    //도청 난이도 판별
//    public void checkPacketsnif(int eap_type){
//        if (eap_type == -1) {
//            Log.d(TAG, "packetsnif_checker: this is not eap");
//        } else {
//            Log.d(TAG, "packetsnif_checker: not in our eap case");
////            score[3] += 50;
//        }
//        scores[0] += scores[3];
//    }

    public int[] getScores() {
       return this.scores;
    }
}


