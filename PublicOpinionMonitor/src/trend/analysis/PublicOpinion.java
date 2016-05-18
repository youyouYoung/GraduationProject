/**
 * 所有舆情分析的父类
 * */
package trend.analysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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
	protected Future<ThemeAnalysis.Result> getAnalysisTask(String sql)
	{
		FutureTask<ThemeAnalysis.Result> task = null;
		try
		{
			Statement statement = connection.getStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set != null)
			{
				set.last();
				int resultCount = set.getRow();
				set.beforeFirst();
				
				if (resultCount > 0 && driver.participleDriver())
				{
					ThemeAnalysis themesAnalysis = new ThemeAnalysis(resultCount);
					task = new FutureTask<ThemeAnalysis.Result>(themesAnalysis);
					new Thread(task).start();
					while (set.next())
					{
						themesAnalysis.addOrignData(set.getString(1));
					}
					themesAnalysis.addOrignData("ORIGNDATA_STOP"); //结束标志
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
	public abstract void displayResult();
}
