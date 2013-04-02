package study.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class index
{
	public static void main(String[] args) 
	{
				String url = "http://"+args[0];
				String filename = args[1];
				System.out.println(url);
				System.out.println(filename);
				
				String text = jSoupParse(getFrom(url));
				String[] words = text.split(" ");
				HashMap<String, Integer> index = indexer(words);
				saver(index,filename);
				
	}
	
	public static String getFrom(String urlSrc) {
		  String res = "";        
		  try { 

		    URL url = new URL(urlSrc);
		    URLConnection conn = url.openConnection();

		   
		    BufferedReader reader = new BufferedReader(
		      new InputStreamReader(conn.getInputStream()));
		    
		    String s;
		    while((s = reader.readLine()) != null) {
		      res += s + "\r\n";
		    }
		    reader.close(); 

		  } catch(Exception e) {
		    
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
	
	public static String jSoupParse (String dirtyHTML)
	{
		Document htmlPage = Jsoup.parse(dirtyHTML);
		String res = htmlPage.body().text();
		return res;
	}
	
	
	public static HashMap<String, Integer> indexer (String[] input)
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
	
	public static void saver (HashMap<String, Integer> map, String filename) 
	{ 
		
		PrintWriter out = null;
        try {
		       out = new PrintWriter(new FileOutputStream(filename));
		       for (Map.Entry<String, Integer> entry : map.entrySet())
				{
					out.println(entry.getKey()+"   "+entry.getValue());		
				}

        }
        catch(FileNotFoundException e){
	             System.out.println("Ошибка открытия файла my_test_file.txt");
	             System.exit(0);
	        }
		   finally {
			   out.close();
		   }
		
	}
}