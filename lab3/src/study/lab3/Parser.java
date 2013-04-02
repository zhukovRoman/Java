
package study.lab3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.sql.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser implements Runnable {

	final static String bdURL ="jdbc:h2:bd/index";
	private int currentDepth;
	private String currentUrl;
	
	
	public Parser (String url, int depth)
	{
		currentDepth = depth;
		if (!url.startsWith("http"))
			currentUrl = "http://"+url;
		else 
			currentUrl = url;
	}
	@Override
	public void run() {
		//System.out.println(currentUrl);
		String html = getFrom(currentUrl);
		
		if (currentDepth>0)
		{
			ArrayList<String> links = parseLinks(html);
			for (String link : links)
			{
				Parser parserThread = new Parser(link, currentDepth-1);
				Thread t = new Thread(parserThread);
				t.start();
			}
		}
		
		String text = jSoupParse(html);
		String[] index = text.split(" ");
		HashMap<String, Integer> hm = index(index);
		saver(hm, currentUrl);
	}
	
	public static String jSoupParse (String dirtyHTML)
	{
		Document htmlPage = Jsoup.parse(dirtyHTML);
		String res = htmlPage.body().text();
		//System.out.println(res);
		return res;
	}
	
	public static ArrayList<String> parseLinks (String HTML)
	{
		ArrayList<String> links = new ArrayList<String>();
		Document doc = Jsoup.parse(HTML);
		Elements hrefs = doc.select("a[href]");
		for (Element link : hrefs)
		{
			
			if (link.attr("href").length()>5 && 
					link.attr("href").indexOf('#')<0 &&
					link.attr("href").startsWith("http"))
					
			{
					links.add(link.attr("href"));
					//System.out.println(link.attr("href"));
			}
		}
		return links;
	}
	
	public static String parse (String text)
	{
		text = Pattern.compile("<!--.*?-->", Pattern.CANON_EQ|Pattern.DOTALL).matcher(text).replaceAll("");
		text = Pattern.compile("<script.*?>.*?</script>", Pattern.CANON_EQ|Pattern.DOTALL).matcher(text).replaceAll("");
		text = Pattern.compile("<style>.*?</style>", Pattern.CANON_EQ|Pattern.DOTALL).matcher(text).replaceAll("");
		text = text.replaceAll("\\<.*?\\>", "");
		text = Pattern.compile("<img.*?>", Pattern.CANON_EQ|Pattern.DOTALL).matcher(text).replaceAll("");
		text = text.replaceAll("\r\n", " ");
		text = text.replaceAll("\\s{1,}", " ");
		return text;
	}
	
	public static String getFrom(String urlSrc) {
		  String res = "";        
		  try { 

		    //открыть соединение по URL
		    URL url = new URL(urlSrc);
		    URLConnection conn = url.openConnection();

		    //создать поток для чтения
		    BufferedReader reader = new BufferedReader(
		      new InputStreamReader(conn.getInputStream()));
		    
		    String s;
		    while((s = reader.readLine()) != null) {
		      res += s + "\r\n";
		    }
		    reader.close(); 

		  } catch(Exception e) {
		    //обработка ошибки
		    res = "URLRequester error:\n" + e; 
		  } 
		  try {
			return new String(new String(res.getBytes(),"utf-8").getBytes("Cp1251"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return res; 
		}

	public static HashMap<String, Integer> index (String[] input)
	{
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		
		for (String word : input)
		{
			if (word.length()>=2)
			{
				if (!hm.containsKey(word))
				{
					hm.put(word, new Integer(1));
				}
				else 
				{
					int current = hm.get(word)+1;
					hm.remove(word);
					hm.put(word, current);
				}
			}
			
		}
		
		return hm;
	}
	
	public static synchronized void saver (HashMap<String, Integer> map, String url) 
	{ 
		System.out.println(map.size() + " ---- " +url );
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection(Parser.bdURL, "sa", "");
			Statement stat = conn.createStatement();
			for (Map.Entry<String, Integer> entry : map.entrySet())
				{
					String key = entry.getKey().toString().replace('\'', '`');
					
					stat.execute("insert into index (word, freq, url) values('"
							+ key + "', "
							+ entry.getValue() + ", '"
							+ url +"')");
				     
				}

	        stat.close();
	        conn.close();

			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}


