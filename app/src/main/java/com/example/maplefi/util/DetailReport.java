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


//            textViewReport.setText("인증 및 암호화 방식으로 WPA2를 사용한 것과 암호화 규칙으로 CCMP를 사용한 것은 좋으나, " +
//                               "PSK 키 관리 방식을 사용하여 해커에게 도청당할 위험이 약간 있고, " +
//                               "네트워크 이름이 숨겨져 있지 않아 해커가 접근하기 용이합니다.\n\n" +
//                               "더 나은 보안을 위해서는 와이파이의 네트워크(디바이스) 숨기기 기능을 활성화하시고 " +
//                               "기업의 경우, EAP 키 관리 방식 적용을 추천합니다.\n\n" +
//                               "추천 형태: 숨겨진 네트워크, \n[WPA2-EAP-CCMP] or [WPA2-PSK-CCMP]");

    public DetailReport(String capabilities){
        if(capabilities.contains("WPA2")){     // Y
            this.positives.add("인증 및 암호화 방식으로 WPA2를 사용한 것");
        }
        else if(capabilities.contains("WPA")){

        }
        else if(capabilities.contains("WEP")){

        }
        else {

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
