package shishkoam.archivemanager;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ав on 21.12.2015.
 */
public class ParseUtils {
    private static ArrayList<String> list = new ArrayList<String>();
    private static final String TAG = "FeedFData";
    private static ArrayList<String> listFeeds = new ArrayList<String>();

    //parser from text
    public ArrayList<String> getPackages(String inputdata) {
            StringBuilder sb = new StringBuilder();
            String data = inputdata;
                int i = 0;
                String s = "";
                boolean whileBoolean = true;
                    while ( whileBoolean) {
                        sb.append(s);
                        i = data.indexOf("</name>");
                        sb.append(data.substring(0, i));
                        if (i > data.length() || i < 0) whileBoolean = false;
                        Log.i("shishkoamonsucces", "parsergetstring" + i );
                        listFeeds.add(sb.toString());
                        data = new String(data.substring(i + 7, data.length()));
                        Log.i("shishkoamonsucces", data.length() + " / " + data);
                        if (data.length()<i) break;
                    }
        for (int j = 0; j < listFeeds.size(); j++) {
            Log.i("shishkoamonsucces", "parsergetAtribute" + listFeeds.get(j));
        }
        return procFeed(listFeeds);
    }

    private ArrayList<String> procFeed(ArrayList<String> feed) {
        ArrayList<String> arrayString = new ArrayList<String>();
        for (int j = 0; j < feed.size(); j++) {
            arrayString.add(deleteTags(getTitle(feed.get(j))));
        }
        return arrayString;
    }

    private String getTitle(String inString) {
        String betweenSubString = "";
        //       Matcher matcher = Pattern.compile("\\>(.*?)\\<span").matcher(inString);
        Matcher matcher = Pattern.compile("name\\>(.*?)\\<name").matcher(inString);
        while (matcher.find())
            betweenSubString = matcher.group();
        return betweenSubString;
    }

    private String deleteTags(String inString) {
        String noTagsString = "";
        noTagsString = inString.replaceAll("\\<.*?\\>", "");
        noTagsString = noTagsString.replaceFirst(".*?\\>", "");
        noTagsString = noTagsString.replaceFirst("<.*", "");
        return noTagsString;
    }

    //parser for first data
    public static ArrayList<String> getXml(String data) throws Exception {
        XmlPullParser parser;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true); //если надо
        parser = factory.newPullParser();
//        URL input;
//            input = new URL("http://192.168.1.151:8080/UserManagement/rest/PackageService/packages/1");
//        parser.setInput(input.openStream(), null);
        StringReader sw=new StringReader(data); //s содержит ваш XML
        parser.setInput(sw); //подаем на вход парсера
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                    && parser.getName().equals("package")) {
                list.add(parser.getAttributeValue(0) + " "
                        + parser.getAttributeValue(1) + "\n"
                        + parser.getAttributeValue(2));
            }
        parser.next();
        }
        return list;
    }
}
