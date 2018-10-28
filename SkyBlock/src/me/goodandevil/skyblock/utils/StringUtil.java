package me.goodandevil.skyblock.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
    private static StringUtil instance;

    public static StringUtil getInstance(){
        if(instance == null) {
            instance = new StringUtil();
        }
        
        return instance;
    }
	
	public String capatilizeUppercaseLetters(String string) {
	    StringBuilder stringBuilder = new StringBuilder(string);
	    Matcher matcher = Pattern.compile("[A-Z]").matcher(string);
	    int extraFeed = 0;
	    
	    while(matcher.find()){
	        if(matcher.start()!=0){
	        	stringBuilder = stringBuilder.insert(matcher.start() + extraFeed, " ");
	            extraFeed++;
	        }
	    }
	    
	    return stringBuilder.toString();
	}
}
