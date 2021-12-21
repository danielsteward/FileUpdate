/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatefileutils;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author danst
 */
public class DateTimeUtil {
    
    private static boolean dateTimeFormatOK;

    public static boolean isDateTimeFormatOK() {
        return dateTimeFormatOK;
    }

    public static void setDateTimeFormatOK(boolean aDateTimeFormatOK) {
        dateTimeFormatOK = aDateTimeFormatOK;
    }
    /**
     * Takes any time/date String format and outputs a String in the style
     * of the UF header format such as 201506231215 which can then be used to create other formats.
     * @param dtInput
     * @return 
     */
    public DateTimeUtil(){
    dateTimeFormatOK = false;
}
    public static String stripNonDigits(String input) {
        String num = new String();
        String regex = "(\\d+)";//search for digits only.
        Matcher matcher = Pattern.compile(regex).matcher(input);
        while (matcher.find()&&num.length()<14) {//while there are numbers to find and the number is 14 digits or less....
            num += matcher.group();
        }
        if(num.length() == 12){//if num doesn't include seconds field then add it.
            num = num + "00";
        }
        return num;
    }
    /**
     * Takes the output from stripNonDigits(String) such as 20150623121500 and creates a String that can be parsed into
     * a LocalDateTime object or for storage such as 2015-09-29T11:07:25.
     */
    public static String createLDTStyleString(String dt){
        if(dt.length()>=12){
            dt = stripNonDigits(dt);
        }
        String secs = dt.substring(12,14);
        String min = dt.substring(10,12);
        String hour = dt.substring(8,10);
        String day = dt.substring(6,8);
        String month = dt.substring(4,6);
        String year = dt.substring(0,4);
        String daTi = (year + "-" + month + "-" + day + "T" + hour + ":" + min+ ":"+secs);
        return daTi;
    }
    /**
     * Takes any date/time String and creates a LocalDateTime object.
     */
    public static LocalDateTime createLDT(String daTi){
        LocalDateTime ldt = null;
        try{
        String dtString = stripNonDigits(daTi);
        String ldtStyleString = createLDTStyleString(dtString);
        ldt = LocalDateTime.parse(ldtStyleString);
        }
        catch(Exception ex){
            Logger.getLogger(DateTimeUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ldt;
    }
    /**
     * Takes any String and returns a display style format String e.g. 2015-09-29 11:07:25
     */
    public static String createDisplayString(String in){
        String daTi;
        if(in.length()<12){
            daTi = "Unknown";
        } else{
        String dt = stripNonDigits(in);
        String secs = dt.substring(12,14);String min = dt.substring(10,12);
        String hour = dt.substring(8,10);String day = dt.substring(6,8);
        String month = dt.substring(4,6);String year = dt.substring(0,4);
        daTi = (year+ "-" + month +"-" + day +" " +hour+":"+min+":"+secs);
        }
        return daTi;
    }
/**
 * Creates a LocalDateTime object representing NOW.
 */
    public static LocalDateTime dateTimeNow(){
        LocalDateTime t = LocalDateTime.now();
        return t;
    }
    
    
    /**
     * Creates date now and returns it as String that the DB can take.
     */
    public static String dateTimeNowAsString()
    {
        LocalDateTime t = LocalDateTime.now();
        String s = t.toString();
        return s;
    }

    /**
     * Checks a String is ok from which to create a LDT.
     */
    public static boolean dtStringCheck(String  dt){
        boolean b=false;
        try{
        if(createLDT(dt)!=null){
            b=true;
        }
        }
        catch(Exception ex){
            StackTraceElement[] ste = ex.getStackTrace();
            System.out.println("Exception thrown in dtStringCheck" + ex.getMessage());
        }
        return b;
    }
    
}
