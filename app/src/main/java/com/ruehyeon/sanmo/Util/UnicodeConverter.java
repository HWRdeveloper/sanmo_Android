package com.ruehyeon.sanmo.Util;

public class UnicodeConverter {

    public static String convertToString(String val) {
// 변환할 문자를 저장할 버퍼 선언
        StringBuffer sb = new StringBuffer();
// 글자를 하나하나 탐색한다.
        for (int i = 0; i < val.length(); i++) {

            if ('\\' == val.charAt(i) && 'u' == val.charAt(i + 1)) {
// 그 뒤 네글자는 유니코드의 16진수 코드이다. int형으로 바꾸어서 다시 char 타입으로 강제 변환한다.
                Character r = (char) Integer.parseInt(val.substring(i + 2, i + 6), 16);
// 변환된 글자를 버퍼에 넣는다.
                sb.append(r);
// for의 증가 값 1과 5를 합해 6글자를 점프
                i += 5;
            } else {
// ascii코드면 그대로 버퍼에 넣는다.
                sb.append(val.charAt(i));
            }
        }
// 결과 리턴
        return sb.toString();
    }


    public static String convertToUnicode(String val) {
// 변환할 문자를 저장할 버퍼 선언
        StringBuffer sb = new StringBuffer();
// 글자를 하나하나 탐색한다.
        for (int i = 0; i < val.length(); i++) {
// 글자 추츨 int값으로 가져온다.
            int code = val.codePointAt(i);
// 128이하면 ascii코드로 변환하지 않는다.
            if (code < 128) {
                sb.append(String.format("%c", code));
            } else {
// 16진수 유니코드로 변환한다.
                sb.append(String.format("\\u%04x", code));
            }
        }
// 결과 리턴
        return sb.toString();
    }


}
