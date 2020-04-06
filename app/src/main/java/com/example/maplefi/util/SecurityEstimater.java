package com.example.maplefi.util;

//TODO : 보안성 모듈 점수 계산 함수 짜고, more info에 들어갈 스트링도 넣기
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

import com.example.maplefi.ui.Apinfo;

import java.util.ArrayList;
import java.util.Objects;

public class SecurityEstimater {
    //test 용 임의 정보들
    ArrayList<Apinfo> apinfos = new ArrayList<>();
//    int grade = 0;
    String TAG = "SecurityEstimater";

    public SecurityEstimater(ArrayList<Apinfo> apinfos/*, int position*/){
        this.apinfos = apinfos;
        int position = 0;
//        while(apinfos.get(position) != null){
            pw_checker(apinfos, position);
            ssid_checker(apinfos, position);
            packet_checker(apinfos, position);
            packetRule_checker(apinfos, position);
        Log.d(TAG, "SecurityEstimater: final = "+apinfos.get(position).getGrade());
            position++;
//        }
//        pw_checker(apinfos, position);
//        ssid_checker(apinfos, position);
//        packet_checker(apinfos, position);
    }

    //포지션에 해당하는 리스트의 로그온 암호적용, 암호화 방식평가 여부
    public void pw_checker(ArrayList<Apinfo> apinfos, int position) {
        if (!(Objects.equals(apinfos.get(position).getPwEncType(), ""))) {
            Log.d(TAG, "pw_checker: pwtype 존재");
            apinfos.get(position).addGrade(50);//임의
            Log.d(TAG, "pw_checker: pwtype"+apinfos.get(position).getGrade());

            switch (apinfos.get(position).getPwEncType()){//if로 할지 스위치로 할지 고민스러움 함께 고민
                case "wpa":
                    Log.d(TAG, "pw_checker: wpa");
                    apinfos.get(position).addGrade(20);
                    Log.d(TAG, "pw_checker: "+apinfos.get(position).getGrade());
                    break;
                case "wpa2":
                    Log.d(TAG, "pw_checker: wpa2");
                    apinfos.get(position).addGrade(40);
                    Log.d(TAG, "pw_checker: "+apinfos.get(position).getGrade());
                    break;
                case "wep" :
                    Log.d(TAG, "pw_checker: wep");
                    apinfos.get(position).addGrade(10);
                    Log.d(TAG, "pw_checker: "+apinfos.get(position).getGrade());
                    break;
                default:
                    Log.d(TAG, "pw_checker: not in our case");
                    break;
            }
        }
    }

    //ssid check
    public void ssid_checker(ArrayList<Apinfo> apinfos, int position){
        if (Objects.equals(apinfos.get(position).getSsid(), "")){//숨김모드일경우
            Log.d(TAG, "ssid_checker: 숨김모드");
            apinfos.get(position).addGrade(60);
        }else{
            Log.d(TAG, "ssid_checker: 숨김모드 아님");
        }
    }

    //프로토콜 방식 판별
    public void packet_checker(ArrayList<Apinfo> apinfos, int position){
        if(Objects.equals(apinfos.get(position).getPacketEncType(), "psk")){
            Log.d(TAG, "packet_checker: protocol psk");
            apinfos.get(position).addGrade(30);
            Log.d(TAG, "packet_checker: "+apinfos.get(position).getGrade());

        }else if(Objects.equals(apinfos.get(position).getPacketEncType(), "eap")){
            Log.d(TAG, "packet_checker: protocol eap");
            apinfos.get(position).addGrade(40);
            Log.d(TAG, "packet_checker: "+apinfos.get(position).getGrade());
            //eap tls?면? 뭐면?

        }else if(Objects.equals(apinfos.get(position).getPacketEncType(), "tkip")){
            Log.d(TAG, "packet_checker: protocol tkip");//wep에서 사용하는 암호화 방식
            apinfos.get(position).addGrade(10);
            Log.d(TAG, "packet_checker: "+apinfos.get(position).getGrade());

        }else{
            Log.d(TAG, "packet_checker: not in our packet case");
        }
    }

    //패킷 규칙 체크 ccmp -> 필요한가??
    public void packetRule_checker(ArrayList<Apinfo> apinfos, int position){
        if(Objects.equals(apinfos.get(position).getProtocolEncType(), "ccmp")) {
            Log.d(TAG, "packetRule_checker: ccmp");
            apinfos.get(position).addGrade(20);
            Log.d(TAG, "packetRule_checker: grade "+apinfos.get(position).getGrade());
        }else{
            Log.d(TAG, "packetRule_checker: not in packetrule case");
        }
    }

//    public void packet_snif_checker(){//eap -tls등 고려하여 도청 가능한지 판별
//        apinfo.get(position).addGrade(50);
//    }

}
