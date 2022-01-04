package etu.simonzo.competition.util;

public class Math {
    public static boolean isPowerOfTwo(int n) {
        int square = 1;
        while(n >= square){
            if(n == square){
                return true;
            }
            square = square*2;
        }
        return false;
    }
}
