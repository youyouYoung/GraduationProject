/**
 * 所有舆情分析的父类
 * */
package trend.analysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import util.PropertiesKey;
import util.PropertiesReader;

import database.BaseConnection;
import database.DBConnection;

public abstract class PublicOpinion
{
	protected Driver driver;
	protected DBConnection connection;
	
	public PublicOpinion()
	{
		driver = Driver.getSystemDriver();
		connection = BaseConnection.getInstance();
	}
	
	/**
	 * 关闭分析系统
	 * */
	public boolean shutDownSystem()
	{
		return driver.shutDown();
	}
	
	/**
	 * 分析方法, 每一个具体实现类必须实现一种舆情分析方法.
	 * */
	public abstract void publicOpinionAnalysis();
	
	/**
	 * 为一次数据查询创建一个多线程任务.
	 * @return 返回一个多线程执行的任务, 如果发生异常返回null.
	 * */
	protected Future<ThemeAnalysisPlus.Result> getAnalysisTask(String sql)
	{
		FutureTask<ThemeAnalysisPlus.Result> task = null;
		try
		{
			final Statement statement = connection.getStatement();
			final ResultSet set = statement.executeQuery(sql);
			if (set != null)
			{
				int resultCount = -1;
				if (set.last())
					resultCount = set.getRow();				
				set.beforeFirst();
				
				if (resultCount > 0 && driver.participleDriver())
				{
					final ThemeAnalysisPlus themesAnalysis = new ThemeAnalysisPlus(resultCount);
					//此线程向ThemeAnalysisPlus的对象中添加待分析的数据
					new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							try
							{
								while (set.next())
								{
									themesAnalysis.addOrignData(set.getString(1));
								}
								statement.close();
							}
							catch (SQLException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							finally
							{
								themesAnalysis.addOrignData("ORIGNDATA_STOP"); //结束标志
							}
						}
					}).start();
					
					task = new FutureTask<ThemeAnalysisPlus.Result>(themesAnalysis);
					//此线程开启ThemeAnalysisPlus的call方法,进行主题分析.
					new Thread(task).start(); 
					
//					while (set.next())
//					{
//						themesAnalysis.addOrignData(set.getString(1));
//					}
//					themesAnalysis.addOrignData("ORIGNDATA_STOP"); //结束标志
//					statement.close();
				}
			}
			
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return task;
	}
	
	/**
	 * 展现结果的方法
	 * */
	public abstract ArrayList<ArrayList<String>> displayThemes();
	
	/**
	 * 去掉主题数组中贡献率相对较小的词, 获取最终的主题
	 * */
	protected Map<String, Double> lastTheme(Map<String, Double>[] themes)
	{
		Map<String, Double> theme = new HashMap<String, Double>();
		
		//将传入的map数组中的所有元素保存在一个map对象中
		for (Map<String, Double> map : themes)
		{
			for (Map.Entry<String, Double> word : map.entrySet())
			{
				if (theme.containsKey(word.getKey()))
				{
					theme.put(word.getKey(), theme.get(word.getKey()) + word.getValue());
				}
				else
				{
					theme.put(word.getKey(), word.getValue());
				}
			}
		}
		
		//去除低于最低贡献率的词组
		double minRate = new Double(PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_CONTRIBUTION)).doubleValue();
		double totle = 0;
		
		for (Map.Entry<String, Double> map : theme.entrySet())
		{
			totle += map.getValue();
		}
		if (totle > 0)
		{
			Object[] keys = theme.keySet().toArray();
			for (Object key : keys)
			{
				if (theme.get(key.toString()).doubleValue() / totle < minRate)
				{
					theme.remove(key.toString());
				}
			}
		}
		
		return theme;
	}
}
