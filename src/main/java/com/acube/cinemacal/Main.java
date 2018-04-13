/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acube.cinemacal;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.server.UID;
import java.text.ParseException;
import java.util.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author alex-adrienauger
 */
public class Main {
    
    public static String parseMonth(String wrongMonth) {
        String monthString = "";
        switch (wrongMonth) {
            case "janvier":  monthString = "01";
                     break;
            case "fevrier":  monthString = "02";
                     break;
            case "mars":  monthString = "03";
                     break;
            case "avril":  monthString = "04";
                     break;
            case "mai":  monthString = "05";
                     break;
            case "juin":  monthString = "06";
                     break;
            case "juillet":  monthString = "07";
                     break;
            case "aout":  monthString = "08";
                     break;
            case "septembre":  monthString = "09";
                     break;
            case "octobre": monthString = "10";
                     break;
            case "novembre": monthString = "11";
                     break;
            case "décembre": monthString = "12";
                     break;
            default: monthString = "13";
                     break;
        }
        return monthString;
    }

    public static String parseDate(String wrongDate) {
        String goodDate = "";
        
        String currentDay = wrongDate.split(" ")[0];
        String currentMonth = wrongDate.split(" ")[1];
        String currentYear = wrongDate.split(" ")[2];
        
        goodDate += currentYear;
        goodDate += parseMonth(currentMonth);
        goodDate += currentDay;
        
        return goodDate;
    }

    public static String addEvent(String previousEvents, String name, String date, String desc) {
        String eventContent = "BEGIN:VEVENT\r\n"
                + "UID:"+new UID()+"\r\n"
                + "DTSTAMP:20180413T114357Z\r\n"
                + "DTSTART:" + parseDate(date) + "\r\n"
                + "DTEND:" + parseDate(date) + "\r\n"
                + "SUMMARY:" + name + "\r\n"
                + "LOCATION:\r\n"
                + "DESCRIPTION:" + "http://allocine.fr" +desc + "\r\n"
                + "END:VEVENT\r\n";
        return previousEvents + eventContent;
    }

    public static String wrapCalendar(String events) {
        String fullCall = "BEGIN:VCALENDAR\r\n"
                + "VERSION:2.0\r\n"
                + "PRODID:http://YOUR_DOMAIN\r\n"
                + events
                + "END:VCALENDAR";
        return fullCall;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        Document doc = Jsoup.connect("http://www.allocine.fr/film/agenda/").get();

        String events = "";

        Element body = doc.body();
        Elements elementsByClass = body.getElementsByClass("hred");
        for (int i = 0; i < elementsByClass.size(); i++) {
            String currenttitle = elementsByClass.get(i).getElementsByClass("meta-title-link").html();
            Elements dates = elementsByClass.get(i).getElementsByClass("date");
            String currentDate = dates.get(dates.size()-1).html();
            String currentLink = elementsByClass.get(i).getElementsByClass("meta-title-link").attr("href");
            events = addEvent(events, currenttitle, currentDate, currentLink);
        }
        
        String calendar = wrapCalendar(events);
//        System.out.println(calendar);
        PrintWriter writer = new PrintWriter("/var/www/html/cinemagenda/calendar.ics", "UTF-8");
        writer.println(calendar);
        writer.close();

        
    }

}
