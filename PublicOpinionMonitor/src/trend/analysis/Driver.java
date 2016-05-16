/**
 * 该类用于驱动系统的所有模块.
 * 
 * 功能:
 * 	1. 启动爬虫程序.
 * 	2. 启动分词工具
 * 	3. 关闭系统.
 * */
package trend.analysis;

import java.io.File;

import topic.participle.CLibrary;
import topic.participle.ICTCLASParameter;

import collection.crawler.Crawler;
import database.BaseConnection;

public class Driver
{
	private static volatile Driver driver = null;
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
	
	public boolean participleDriver()
	{
		int init_flag = CLibrary.Instance.NLPIR_Init(ICTCLASParameter.getDataLocation(), 1, "0");
		
		return init_flag == 0 ? false : true;
	}

	/***/
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
