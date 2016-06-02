/**
 * 该类用于驱动系统的所有模块.
 * 
 * 功能:
 * 	1. 启动爬虫程序.
 * 	2. 启动分词工具
 * 	3. 关闭系统.
 * */
package trend.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import topic.participle.CLibrary;
import topic.participle.ICTCLASParameter;
import util.PropertiesKey;
import util.PropertiesReader;

import collection.crawler.Crawler;
import database.BaseConnection;

public class Driver
{
	private static volatile Driver driver = null;
	private HashSet<String> stopList = null;
	private int init_flag = -1;
	private Driver()
	{
		//单例模式
	}
	
	/**
	 * 获得Driver类的对象.
	 * */
	public static Driver getSystemDriver()
	{
		if (driver == null)
		{
			// 保存多线程访问下的安全性
			synchronized (Driver.class)
			{
				if (driver == null)
				{
					driver = new Driver();
				}
			}
		}
		
		return driver;
	}
	
	/**
	 * 爬虫模块驱动程序.
	 * @param urlFilePath 保存url的文件路径.
	 * @return 如果爬虫程序成功启动, 返回true. 否则false.
	 * */
	public boolean crawlerDriver(String urlFilePath)
	{
		if (new File(urlFilePath).exists())
		{
			Crawler crawler = new Crawler(urlFilePath);
			crawler.start();
			return true;
		}
		return false;
	}
	
	/**
	 * 启动分词程序
	 * */
	public boolean participleDriver()
	{
		if (init_flag == -1)
		{
			init_flag = CLibrary.Instance.NLPIR_Init(ICTCLASParameter.getDataLocation(), 1, "0");
		}
		return init_flag == 0 ? false : true;
	}
	
	/**
	 * 启动停用词表
	 * @return 返回一个HashSet保存的停用词表, 如果无法获取返回空
	 * */
	public HashSet<String> getStopList()
	{
		if (stopList == null)
		{
			final String listLocation = System.getProperties().getProperty("user.dir") + '/'
					+ PropertiesReader.getQuoteInfo(PropertiesKey.QUOTE_LISTLOCATION);
			BufferedReader reader = null;
			//System.out.println(listLocation);
			try
			{
				try
				{
					stopList = new HashSet<String>();
					reader = new BufferedReader(new FileReader(new File(listLocation)));
					String line = null;
					while ((line = reader.readLine()) != null)
					{
						//String newString = new String(line.getBytes(), "UTF-8");
						stopList.add(line.toLowerCase());
					}
				}
				finally
				{
					reader.close();
				}
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("Can't found stoplist on " + listLocation);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("无法操作当前文件: " + listLocation);
			}
		}
		
		return stopList;
	}
		
	/**
	 * 关闭系统资源
	 * */
	public boolean shutDown()
	{
		try
		{
			// 关闭数据库
			BaseConnection.getInstance().close();

			// 关闭分词系统
			CLibrary.Instance.NLPIR_Exit();
			return true;
		}
		finally
		{
			
		}
		//return false;
	}
}
