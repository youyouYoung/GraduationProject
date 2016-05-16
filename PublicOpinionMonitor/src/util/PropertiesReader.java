/**
 * 
 * 这是一个工具类,用于获取属性文件中的内容.
 * 其他类可以通过传入属性文件中的key获取对应的values.
 * 
 * */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader
{
	/**
	 * 获取systemParamter.properties文件中属性值
	 * 该文件保存了系统配置信息.
	 * @param key 属性文件中的key.
	 * */
	public static String getSystemParamter(String key)
	{
		final String sysParamterPath = "/properties/systemParamter.properties";
		return getPropertiesValue(sysParamterPath, key);
	}
	
	/**
	 * 获取quote.properties文件中的内容.
	 * 该文件保存了所有关于第三方引用的信息.
	 * @param key 该文件中的key.
	 * */
	public static String getQuoteInfo(String key)
	{
		final String quoteFilePath = "/properties/quote.properties";
		return getPropertiesValue(quoteFilePath, key);
	}
	
	/**
	 * 获取jsonKey.properties文件中的内容.
	 * 该文件保存了网络爬虫保存的json文件的格式信息.
	 * @param key 该文件中的key.
	 * */
	public static String getjsonKeyInfo(String key)
	{
		final String jsonKeyFilePath = "/properties/jsonKey.properties";
		return getPropertiesValue(jsonKeyFilePath, key);
	}
	
	/**
	 * 获取database.properties文件中的内容.
	 * 该文件保存了数据库的信息.
	 * @param key 该文件中的key.
	 * */
	public static String getDatabaseInfo(String key)
	{
		final String databaseFilePath =  "/properties/database.properties";
		return getPropertiesValue(databaseFilePath, key);
	}
	
	/**
	 * 获取属性文件中某一个key的values.
	 * @param path 属性文件相对于项目根目录的路径.
	 * @param key 属性文件中的key.
	 * */
	private static String getPropertiesValue(String path, String key)
	{
		String result = null;
		final String basePath = System.getProperties().getProperty("user.dir");
		//属性文件的绝对路径.
		final String propertiesFilePath = basePath + path;
		
		try
		{
			FileReader reader = new FileReader(new File(propertiesFilePath));
			try
			{
				Properties properties = new Properties();
				properties.load(reader);
				result = properties.getProperty(key);
			}
			finally
			{
				reader.close();
			}		
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			System.err.println("Please make sure you have properties file in this folder: " + System.getProperties().getProperty("user.dir") + "/properties");
			//e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.err.println("Can't read this properties file: " + propertiesFilePath +", Please check your permission");
			//e.printStackTrace();
		}
		
		return result;
	}
}
