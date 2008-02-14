/* CPSDateValidator.java - created: Jan 30, 2008
 * Copyright (C) 2008 Clayton Carter
 * 
 * This file is part of the project "Crop Planning Software".  For more
 * information:
 *    website: http://cropplanning.googlecode.com
 *    email:   cropplanning@gmail.com 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package CPS.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class CPSDateValidator {
    
    public static String DATE_FORMAT_SHORT = "MM/dd";
    public static String DATE_FORMAT_BRIEFYEAR = "MM/dd/yy";
    public static String DATE_FORMAT_FULLYEAR = "MM/dd/yyyy";
    
    public static String DATE_FORMAT_SQL = "yyyy-MM-dd";
    
    private ArrayList<String> formatList;
    private String defaultFormat = DATE_FORMAT_FULLYEAR;
    
    public CPSDateValidator() {
        formatList = new ArrayList();
        formatList.add( DATE_FORMAT_BRIEFYEAR );
        formatList.add( DATE_FORMAT_SHORT );
//        formatList.add( DATE_FORMAT_FULLYEAR );
        
        defaultFormat = DATE_FORMAT_FULLYEAR;
        
    }
    
    /**
     * Add a date format.
     * @param f a format string for SimpleDateFormat
     * @see java.text.SimpleDateFormat
     */
    public void addFormat( String f ) {
        formatList.add(f);
    }
    
    /**
     * Sets the default format used for formatting dates.  The default value
     * is DATE_FORMAT_FULLYEAR.
     * @param f Format to use.
     * @see Constant Field Values
     */
    public void setDefaultFormat( String f ) {
        defaultFormat = f;
    }
    
    public String format( Date d ) {
        if ( d == null )
            return "";
        else
            return new SimpleDateFormat( defaultFormat ).format( d );
    }
    
    public Date parse( String s ) {
        if ( s.equals( "" ))
            return null;
        
        Date date = new Date(-1);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(false);
        
        String dateText = s;
        int dateShift = 0;
        // handles addition of negative numbers
        if ( dateText.matches( ".+\\+.+" ) ) {
            String[] sp = dateText.split( "\\+" );
            dateText = sp[0].trim();
            dateShift = Integer.parseInt( sp[1].trim() );
        }
        // does NOT handle subtract of negative numbers
        else if ( dateText.matches( ".+-.+" ) ) {
            String[] sp = dateText.split( "-" );
            // if we split into two, then there was just one -
            if ( sp.length == 2 ) {
                dateText = sp[0].trim();
                dateShift = -1 * Integer.parseInt( sp[1].trim() );
            }
            else
                System.err.println( "ERROR(CPSTable): Can't understand date:" + dateText + " [Too many '-'s]" );
        }
        
        
        for ( String format : formatList ) {
            sdf.applyPattern( format );
            try {
                // if the date parses, then break the for loop
                date = sdf.parse( dateText );
                System.out.println("DATE MATCHED: " + format );
                // if the matched format doesn't include a year component, 
                // then shift the date into this year.
                if ( format.indexOf("yy") == -1 ) {
                    GregorianCalendar c = new GregorianCalendar();
                    GregorianCalendar now = new GregorianCalendar();
                    c.setTime( date );
                    now.setTime( new Date() );
                    c.set( GregorianCalendar.YEAR, now.get( GregorianCalendar.YEAR ) );
                    date = c.getTime();
                }
                break;
            } 
            // if the date doesn't parse, try the next pattern
            catch ( Exception ignore ) {}
            System.err.println( "ERROR parsing date: " + s );
        }
        
        // if dateShift is not 0, then we should add it to the parsed date
        if ( dateShift != 0 ) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime( date );
            cal.add( GregorianCalendar.DAY_OF_YEAR, dateShift );
            date = cal.getTime();
        }
        
        return date;
    }
            

}