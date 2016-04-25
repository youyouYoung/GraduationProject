/**
 * 
 * 为了尽快完成项目,先创建一个简单的数据库连接类.
 * 该类使用单例模式,是为了只能具有一个Connection对象.
 * 
 * -------------------------------------------------------------------------------
 * 改进:
 * 	1. 从配置文件中读取数据库连接的信息,如用户名和密码之类的.
 * 
 * -------------------------------------------------------------------------------
 * 
 * */
package collection.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseConnection implements DBConnection
{
	private static volatile BaseConnection object;
	private Connection con;

	private BaseConnection()
	{
		//单例模式
		getConnection();
	}
	
	/**
	 * 
	 * 创建一个Connection对象.
	 * 
	 * */
	private void getConnection()
	{
		final String server = "localhost";
		final String dbName = "db";
		final String user = "root";
		final String pwd = "mysqlforyouyou";
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://" + server + ":3306/" + dbName + "", user, pwd);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			System.err.println("Database connection fail");
		}
	}
	
	@Override 
	public Statement getStatement() throws SQLException
	{		
		Statement statement = con.createStatement();
		return statement;
	}
	
	@Override
	public void close()
	{
		try
		{
			if (con != null)
			{
				con.close();
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			System.err.println("Connection close failed.");
		}
	}
	
	public static BaseConnection getInstance()
	{
		if (object == null)
		{
			//为保证线程安全
			synchronized (BaseConnection.class)
			{
				if (object == null)
				{
					object = new BaseConnection();
				}
			}
		}
		
		return object;
	}
}
