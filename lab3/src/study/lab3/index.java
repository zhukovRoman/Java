package study.lab3;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.*;


public class index
{
	
	public static void mining (String url)
	{
		truncate();
		Parser parserThread = new Parser(url, 1);
		Thread t = new Thread(parserThread);
		t.start();
	}
	
	public static String search (String word)
	{
		Connection conn;
		//String result = "";
		JSONArray list = new JSONArray();
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(Parser.bdURL, "sa", "");
			Statement stat = conn.createStatement();
			ResultSet rs;
			rs = stat.executeQuery("select * from INDEX WHERE LOWER(WORD)='"+word+"' Order By Freq Desc");
			while (rs.next()) {
				JSONObject obj=new JSONObject();
				obj.put("word", word);
				obj.put("freq", rs.getString("FREQ"));
				obj.put("url", rs.getString("URL"));
				list.add(obj);
				//result += "Встречается  " +rs.getString("FREQ") + " раз(а) на странице " +rs.getString("URL")+"\n";
			    //System.out.println("Meets " +rs.getString("FREQ") + " time(s) on page " +rs.getString("URL"));
			}
			stat.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jsonString = JSONValue.toJSONString(list);
		return jsonString;
		//return result;
	}
	
	public static void truncate ()
	{
		Connection conn;
		String createSQLScript = "CREATE TABLE INDEX(ID INT PRIMARY KEY AUTO_INCREMENT,WORD VARCHAR (255),FREQ INT,URL VARCHAR (500));";
		String dropSQLScript = "DROP TABLE IF EXISTS INDEX";
		
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(Parser.bdURL, "sa", "");
			Statement stat = conn.createStatement();
			stat.execute(dropSQLScript);
			stat.execute(createSQLScript);
			stat.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
}