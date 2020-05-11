package com.example.maplefi.util;

import java.util.ArrayList;
import java.util.List;

public class DetailReport {
    private String report = "";
    private List<String> positives = new ArrayList<String>();
    private List<String> negatives = new ArrayList<String>();
    private List<String> solutions = new ArrayList<String>();

    private final String postPosit = "은 좋으나, ";
    private final String postNegat = "니다.";
    private final String prevSol = "더 나은 보안을 위해서는 ";
    private final String bestPolicy = "추천 형태: 숨겨진 네트워크, \\n[WPA2-EAP-CCMP] or [WPA2-PSK-CCMP]";

    // CJWiFi_9C1A, [WPA-PSK-CCMP+TKIP][WPA2-PSK-CCMP+TKIP][WPS][ESS]   - ~점, ~것, ~부분
//            textViewReport.setText("인증 및 암호화 방식으로 WPA2를 사용한 것과 암호화 규칙으로 CCMP를 사용한 것은 좋으나, " +
//                               "PSK 키 관리 방식을 사용하여 해커에게 도청당할 약간의 위험과, " +
//                               "네트워크 이름이 숨겨져 있지 않아 해커가 접근하기 용이한 점이 있습니다.\n\n" +
//                               "더 나은 보안을 위해서는 와이파이의 네트워크(디바이스) 숨기기 기능을 활성화하시고 " +
//                               "기업의 경우, EAP 키 관리 방식을 적용 하는 것을 추천합니다.\n\n" +
//                               "추천 형태: 숨겨진 네트워크, \n[WPA2-EAP-CCMP] or [WPA2-PSK-CCMP]");

    public DetailReport(String ssid, String capabilities, int eapType){
        // PW Encryption Type
        if(capabilities.contains("WPA2")) {     // Positive
            this.positives.add("인증 및 암호화 방식으로 WPA2를 사용한 것");
        }
        else if(capabilities.contains("WPA")) {
            this.negatives.add("");
            this.solutions.add("");
        }
        else if(capabilities.contains("WEP")) {
            this.negatives.add("");
            this.solutions.add("");
        }
        else {
            this.negatives.add("");
            this.solutions.add("");
        }

        // SSID Hide
        if(ssid.equals("")) {
            this.negatives.add("네트워크 이름이 숨겨져 있지 않아 해커가 접근하기 용이한 점");
            this.solutions.add("와이파이의 네트워크(디바이스) 숨기기 기능을 활성화");
        }
        else {  // Positive
            this.positives.add("");
        }

        // Data Encryption Type
        if(capabilities.contains("EAP")) {
            this.positives.add("");
        }
        else if(capabilities.contains("PSK")) {
            this.negatives.add("PSK 키 관리 방식을 사용하여 해커에게 도청당할 약간의 위험");
            this.solutions.add("기업의 경우, EAP 키 관리 방식을 적용");
        }
        else if(capabilities.contains("TKIP")) {
            this.negatives.add("");
            this.solutions.add("");
        }
        else {
            this.negatives.add("");
            this.solutions.add("");
        }

        // Packet Rule
        if(capabilities.contains("CCMP")) {
            this.positives.add("암호화 규칙으로 CCMP를 사용한 것");
        }
        else {
            this.negatives.add("");
            this.solutions.add("");
        }

        // EAP Type
        if(eapType == -1){
            this.negatives.add("");
            this.solutions.add("");
        }
        else {
            this.positives.add("");
        }

        this.generateReport();
    }

    private void generateReport(){


        this.positives.get(0);
        this.negatives.get(0);
        this.solutions.get(0);

        this.report += this.prevSol;


        this.report += "\n\n";
        this.report += this.bestPolicy;
    }

    public String getReport(){
        return this.report;
    }

}
