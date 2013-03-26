package study.lab2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class index
{
	public static void main(String[] args) throws IOException
	{
		System.out.println("��������� ����� �������� � 2 �������:\n 1 - ������ �� ����������� \n 2 - c��� �����������");
		System.out.print("�������� �����: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        input = br.readLine(); 
		if (input.equals("2"))
		{
			if (args.length == 2 )
			{
				truncate();
				System.out.println(args[0]);
				System.out.println(args[1]);
				Parser parserThread = new Parser(args[0], 1);
				Thread t = new Thread(parserThread);
				t.start();
				
			}
			else System.out.println("�������� ��������� ��� �� ����������\n");
		}
		if (input.equals("1"))
		{
			System.out.println("�������� ����������....");
			System.out.print("������� ����� ��� ������: ");
			input = br.readLine();
			search(input);
			
			
		}
	}
	
	public static void search (String word)
	{
		Connection conn;
		String createSQLScript = "CREATE TABLE INDEX(ID INT PRIMARY KEY AUTO_INCREMENT,WORD VARCHAR (255),FREQ INT,URL VARCHAR (500));";
		String dropSQLScript = "DROP TABLE IF EXISTS INDEX";
		
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(Parser.bdURL, "sa", "");
			Statement stat = conn.createStatement();
			ResultSet rs;
			rs = stat.executeQuery("select * from INDEX WHERE WORD='"+word+"' Order By Freq Desc");
			while (rs.next()) {
			    System.out.println("����������� " +rs.getString("FREQ") + " ���(�) �� �������� " +rs.getString("URL"));
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