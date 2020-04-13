package com.example.maplefi.util;

//TODO : Security 함수 추가// 4,5,6번
//deadline : 2020.4.5
    /*평가 항목
    1. 무선랜 단말에 로그온 암호 적용? (o,x) ->완
    2. SSID 숨김모드로 설정?(o,x) ->완
    3. 사용자 인증과 데이터 암호화를 위해 AP에서 제공하는 암호기능 제공?(O,x) -> 완
    4. 무선랜 도청 가능?(o,x) -> eap tls, peap 등 판별하여 고려// eap-tls 등의 세부정보를 받아올 수 있을지 몰라서 일단은 둠
    5. 사용하고 있는 무선랜의 인증방식 보안 강도-> 인증 방식별 분기 , 방식별 점수 배분 ->따로 할것인가..? 고민스럽
    6. 무선랜에서 사용하고 있는 전송 데이터 암호화 방식의 강도는? -> 프로토콜에서 사용하는 암호화 방식별분기 ,  강도 점수
    -> 따로 함수 만들어서? 합쳐?
    */


import android.util.Log;

import com.example.maplefi.ui.ApItem;
import com.example.maplefi.ui.Apinfo;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class SecurityEstimater {
    //test 용 임의 정보들
    ArrayList<Apinfo> apinfos = new ArrayList<>();//debug
    ApItem ap_Item = null;
    String ssid = "";
    String pwEncType = "wpa";//와이파이 패스워드 암호화 type
    String packetRule = "";//ex) ccmp
    String packetEncType = ""; //ex)eap
    int rssi = 0;
    int score = 0;
    String TAG = "SecurityEstimater";

    public SecurityEstimater(/*ArrayList<Apinfo> apinfos,*/ ArrayList<ApItem> apItems, int position){
        Log.d(TAG, "SecurityEstimater: before apitemset");
        this.ap_Item = apItems.get(position);
        ap_Item.setSec_score(0);
        Log.d(TAG, "SecurityEstimater: after apItem set");

        pw_checker(ap_Item);
        ssid_checker(ap_Item);
        packet_checker(ap_Item);
        packetRule_checker(ap_Item);
        packetsnif_checker(ap_Item);
        apItems.get(position).setSec_score(ap_Item.getSecScore());
        Log.d(TAG, "SecurityEstimater: final = "+apItems.get(position).getSecScore());

    }

    //포지션에 해당하는 리스트의 로그온 암호적용, 암호화 방식평가 여부
    //TODO : contain 사용해서 다시 짜기
    public void pw_checker(ApItem ap_Item) {
        if (!/*(Objects.equals(*/(ap_Item.getCaps().toUpperCase().isEmpty())) {
            Log.d(TAG, "pw_checker: pwtype 존재");
            ap_Item.addSec_Score(50);//임의
            Log.d(TAG, "pw_checker: pwtype"+ap_Item.getSecScore());
            String pw = "";
            pw = ap_Item.getCaps().split(Pattern.quote("/[A-Z]{3}/i"))[0].toString();
            Log.d(TAG, "pw_checker: split: "+ pw/*apItems.get(position).getCaps().split("/[A-Z]{3}/i")*/);

            if(ap_Item.getCaps().contains("WPA")){
                Log.d(TAG, "pw_checker: wpa");
                ap_Item.addSec_Score(20);
                Log.d(TAG, "pw_checker: " + ap_Item.getSecScore());

            }else if(ap_Item.getCaps().contains("WPA2")){
                Log.d(TAG, "pw_checker: wpa2");
                ap_Item.addSec_Score(40);
                Log.d(TAG, "pw_checker: " + ap_Item.getSecScore());

            }else if(ap_Item.getCaps().contains("WEP")){
                Log.d(TAG, "pw_checker: wep");
                ap_Item.addSec_Score(10);
                Log.d(TAG, "pw_checker: " + ap_Item.getSecScore());
            }else{
                Log.d(TAG, "pw_checker: not in our case");
            }
        }
    }

    //ssid check
    public void ssid_checker(ApItem ap_Item){
        Log.d(TAG, "ssid_checker: 여기");
        if (Objects.equals(ap_Item.getSsid(), "")){//숨김모드일경우
            Log.d(TAG, "ssid_checker: 숨김모드");
            ap_Item.addSec_Score(60);
        }else{
            Log.d(TAG, "ssid_checker: 숨김모드 아님");
        }
    }

    //프로토콜 방식 판별
    public void packet_checker(ApItem ap_Item){
        if(ap_Item.getCaps().contains("PSK")){
            Log.d(TAG, "packet_checker: protocol psk");
            ap_Item.addSec_Score(30);
            Log.d(TAG, "packet_checker: "+ap_Item.getSecScore());

        }else if(ap_Item.getCaps().contains("EAP")){
            Log.d(TAG, "packet_checker: protocol eap");
            ap_Item.addSec_Score(40);
            Log.d(TAG, "packet_checker: "+ap_Item.getSecScore());
            //eap tls?면? 뭐면?

        }else if(ap_Item.getCaps().contains("TKIP")){
            Log.d(TAG, "packet_checker: protocol tkip");//wep에서 사용하는 암호화 방식
            ap_Item.addSec_Score(10);
            Log.d(TAG, "packet_checker: "+ap_Item.getSecScore());

        }else{
            Log.d(TAG, "packet_checker: not in our packet case");
        }
    }

    //패킷 규칙 체크 ccmp -> 필요한가??
    public void packetRule_checker(ApItem ap_Item){
        if(ap_Item.getCaps().contains("CCMP")) {
            Log.d(TAG, "packetRule_checker: ccmp");
            ap_Item.addSec_Score(20);
            Log.d(TAG, "packetRule_checker: grade "+ap_Item.getSecScore());
        }else{
            Log.d(TAG, "packetRule_checker: not in packetrule case");
        }
    }

    public void packetsnif_checker(ApItem ap_Item){//eap -tls등 고려하여 도청 가능한지 판별
        if(ap_Item.getEap_type()==-1) {
            Log.d(TAG, "packetsnif_checker: this is not eap");            
        }else{
            Log.d(TAG, "packetsnif_checker: not in our eap case");
        }
    }

    public static int getScore(ApItem ap_Item){
        return ap_Item.getSecScore();
    }

}
