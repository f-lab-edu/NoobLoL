package com.nooblol.global.utils;

public class SummonerUtils {
    /**
     * 라이엇에서 공백의 경우에는 %20으로 처리가 되기 떄문에 공통적으로 사용할 기능을 코드로 작성
     * @param str
     * @return str
     */
    public static String whitespaceReplace(String str) {
        return str.replaceAll(" ", "%20");
    }

}
