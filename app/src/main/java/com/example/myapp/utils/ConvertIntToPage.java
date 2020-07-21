package com.example.myapp.utils;

public class ConvertIntToPage {

    public static String intToPage(int i) {
        String pageNum = "";
        int num = 0;
        int module = 10000;

        while (module>1) {
            module /= 10;
            if (num ==15){
                pageNum += "טו";
                break;
            }
            if (num ==16){
                pageNum += "טז";
                break;
            }
            num = i % module;
            if (num != i) {
                i -= num;
                pageNum += findPageFromInt(i);
                i=num;
            }
        }
        return pageNum;
    }

    public static String findPageFromInt (int intPage){
        char c;
        if (intPage<=10){
            c=(char)(intPage+1487);
        }else if (intPage <100){
            int num = intPage /10;
            int intUmiCode = 1497+ FixFinalLetters(intPage);
            c=(char)(num+intUmiCode);
        }else {
            int num = intPage /100;
            c=(char)(num+1510);
        }
        return String.valueOf(c);
    }

    private static int FixFinalLetters(int intPage) {
        int fixInt = 0;
        if (intPage>=40){
            fixInt ++;
        }if (intPage>=50){
            fixInt ++;
        }if (intPage>=80){
            fixInt ++;
        }if (intPage>=90){
            fixInt ++;
        }
        return fixInt;
    }
}
