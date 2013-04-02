package study.lab3;


import static spark.Spark.*;
import java.io.UnsupportedEncodingException;
import spark.*;


public class startpage {

   public static void main(String[] args) {
      
      get(new Route("/API/search/:word") {
		@Override
         public Object handle(Request request, Response response) {
        	 
        	String wordForSearch = request.params(":word");
        	try {
				wordForSearch = java.net.URLDecoder.decode(wordForSearch, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            //String res = "Поиск слова "+wordForSearch+": \n";
            //res += index.search(wordForSearch);
            return index.search(wordForSearch);
         }
      });

      get(new Route("/API/mining/:url") {
  		@Override
           public Object handle(Request request, Response response) {
          	 
          	String url = request.params(":url");
          	index.mining(url);
          	
            return "{running}";
           }
        });
   }

}