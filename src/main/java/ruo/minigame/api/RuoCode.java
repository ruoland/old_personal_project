package ruo.minigame.api;

public class RuoCode {

    /**
     * 커스텀 클라이언트에서 쓰기 위해 만든 코드
     * 오류가 나도 오류가 났는지 알 수 없는 코드이니 주의
     */
    public static int safeParseInt(String parse, int defaultValue){
        if(parse.equalsIgnoreCase("")) {
            System.out.println("기본 값이 반환됨");
            return defaultValue;
        }
        try{
            return Integer.valueOf(parse);
        }
        catch (NumberFormatException e) {
            System.out.println("기본 값이 반환됨");
            return defaultValue;
        }
    }
}
