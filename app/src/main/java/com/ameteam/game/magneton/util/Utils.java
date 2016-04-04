package com.ameteam.game.magneton.util;

/**
 * Created by edson on 27/03/2016.
 */
public class Utils {

    public static String getHexStringByInt(int value, int digits){
        return String.format("%"+digits+"s", Integer.toHexString(value)).replaceAll(" ", "0");
    }
}
