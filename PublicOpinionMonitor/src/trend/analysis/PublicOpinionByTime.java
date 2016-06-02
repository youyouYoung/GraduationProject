/**
 * 基于时间顺序的舆情分析
 * */
package trend.analysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import util.PropertiesKey;
import util.PropertiesReader;

public class PublicOpinionByTime extends PublicOpinion
{
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	//private Calendar calendar;// = Calendar.getInstance();
	private Date beginDate; //= dateFormat.format(calendar.getTime());
	//需要获取的周的数量
	private final int MaxWeekCount = new Integer(PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_WEEKNUMBER)).intValue(); 
	//获取数量的最大值
	private final int MaxRecordCount = new Integer(PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_DATALIMIT)).intValue();
	private ArrayList<PublicOpinionByTime.ResultByWeek> result = new ArrayList<PublicOpinionByTime.ResultByWeek>();

	public PublicOpinionByTime()
	{
		super();
		//calendar = Calendar.getInstance();
		//beginDate = calendar.getTime();
	}
		
	/**
	 * 设置要查询的日期.
	 * @param dateString 日期, 格式为yyyy-MM-dd
	 * @return 是否成功设置
	 * */
	public boolean setBeginDate(String dateString)
	{
		try
		{
			this.beginDate = dateFormat.parse(dateString);
			return true;
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			// beginDate值不变
		}
		return false;
	}
	
	private String startDate;
	private String endDate;
	private final CountDownLatch doneSignal = new CountDownLatch(MaxWeekCount);
	/**
	 * 以一周时间为周期分析舆情
	 * */
	@Override
	public void publicOpinionAnalysis()
	{
		Calendar calendar = Calendar.getInstance();
		if (beginDate != null)
		{
			calendar.setTime(beginDate);
		}
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//本周周一的日期
		calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - calendar.get(Calendar.DAY_OF_WEEK));
				
		for (int i = 0; i < MaxWeekCount; i++)
		{
			startDate = dateFormat.format(calendar.getTime()); //本周一
			int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR); //本周是这一年的第几周
			calendar.add(Calendar.DATE, 6); //跳转到本周周日
			endDate = dateFormat.format(calendar.getTime()); //本周日
			
			final ResultByWeek resultByWeek = new ResultByWeek(startDate, weekNumber);
			new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					String sql = "select count(page_link) from page_info where page_date between '" + startDate + "' and '" + endDate + "'"; 
					Statement statement;
					try
					{
						statement = connection.getStatement();
						ResultSet set = statement.executeQuery(sql);
						
						if (set.next())
						{
							resultByWeek.setPostsNumber(set.getInt(1));
						}
						else {
							resultByWeek.setPostsNumber(0);
						}
						//resultByWeek.setPostsNumber(10);
						
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						System.err.println("Error: " + sql);
						e.printStackTrace();
					}
					finally
					{
						doneSignal.countDown();
					}
				}
			}).start();
			
			
//			String sql = "select count(pi_id) from page_info where page_date between '" + startDate + "' and '" + endDate + "'"; 
//			Statement statement;
//			try
//			{
//				statement = connection.getStatement();
//				ResultSet set = statement.executeQuery(sql);
//				
//				if (set.next())
//				{
//					//添加时间信息
//					
//					resultByWeek.setPostsNumber(set.getInt(1));
					
					//获取这周帖子主题
					String sql = "select posts_content from page_content where posts_date between '" + startDate + "' and '" + endDate + "' limit " + MaxRecordCount;
					Future<ThemeAnalysisPlus.Result> future = getAnalysisTask(sql);
					if (future != null)
					{
						resultByWeek.setTheme(future);
						result.add(resultByWeek);
					}
					
					
					//获取帖子各类型占比
//					sql = "select count(pi_id), page_type from page_info where page_date between '2016-4-8' and '2016-4-14' group by page_type";
//					set = statement.executeQuery(sql);
//					while (set.next())
//					{
//						resultByWeek.setTypeNumber(set.getString(2), set.getInt(1));
//					}
//										
//				}
//				
//			}
//			catch (SQLException e)
//			{
//				// TODO Auto-generated catch block
//				System.err.println("Error: " + sql);
//				e.printStackTrace();
//			}
			
			calendar.add(Calendar.DATE, -13); //跳转到上周一
		}
	}
			
	@Override 
	/**
	 * 0. 时间,格式yyyy-MM-dd
	 * 1. 周数
	 * 2. 主题列表
	 * 3. 主题贡献率列表
	 * */
	public ArrayList<ArrayList<String>> displayThemes()
	{
		ArrayList<ArrayList<String>> arrayList = new ArrayList<>();
		for (ResultByWeek resultByWeek : result)
		{
			ArrayList<String> strings = new ArrayList<>();
			strings.add(resultByWeek.date);
			strings.add(resultByWeek.dateNumber+"");
			//System.out.println("时间: " +resultByWeek.date+ ", 周数: " + resultByWeek.dateNumber);
			//System.out.println("本周帖子数量: " + resultByWeek.postsNumber);
			Map<String, Double> map;
			StringBuffer words = new StringBuffer("[");
			StringBuffer values = new StringBuffer("[");
			try
			{
				map = lastTheme(resultByWeek.theme.get().getThemes());
				for (Map.Entry<String, Double> entry : map.entrySet())
				{
					words.append("'"+entry.getKey()+"', ");
					values.append("{value:"+entry.getValue()+", name:'"+entry.getKey()+"'}, ");
					//System.out.println(entry);
				}
				if (values.length() > 2)
				{
					values = values.delete(values.length() -2, values.length());
				}
				values.append("]");
				if (words.length() > 2)
				{
					words = words.delete(words.length() -2, words.length());
				}
				words.append("]");
				strings.add(words.toString());
				strings.add(values.toString());
				arrayList.add(strings);
			}
			catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			Map<String, Integer> typeMap = resultByWeek.postsType;
//			for (Map.Entry<String, Integer> entry : typeMap.entrySet())
//			{
//				System.out.println(entry);
//			}
			
			//System.out.println();
		}
		
		return arrayList;
		
//		for (Map.Entry<DateInfo, Future<ThemeAnalysisPlus.Result>> entry : resultMap.entrySet())
//		{
//			ThemeAnalysisPlus.Result result;
//			DateInfo dateInfo;
//			try
//			{
//				result = entry.getValue().get();
//				dateInfo = entry.getKey();
//				//BlockingQueue<String> keywords = result.getKeywords();
//				Map<String, Double> map = lastTheme(result.getThemes());
//				
//				System.out.println("第 " + dateInfo.getWeekNumber() + " 周. 具体日期: " + dateInfo.getStartDate().toString());
//				for (Map.Entry<String, Double> theme : map.entrySet())
//				{
//					System.out.println(theme);
//				}
//				
//				System.out.println();
//				
//				//LdaUtil.explain(themes);
//			}
//			catch (InterruptedException | ExecutionException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	}
	
	/**
	 * 0. 时间序列
	 * 1. 数量序列
	 * */
	public ArrayList<String> displayPostsCount()
	{
		ArrayList<String> strings = new ArrayList<>();
		try
		{
			doneSignal.await();
			
			StringBuffer timeBuffer = new StringBuffer("[");
			StringBuffer countBuffer = new StringBuffer("[");
			for (ResultByWeek item : result)
			{
				
				timeBuffer.append("'"+item.date+" 第"+item.dateNumber+"周', ");
				countBuffer.append(item.postsNumber+", ");
//				System.out.println(item.postsNumber);
			}
			timeBuffer = timeBuffer.delete(timeBuffer.length()-2, timeBuffer.length());
			timeBuffer.append("]");
			countBuffer = countBuffer.delete(countBuffer.length()-2, countBuffer.length());
			countBuffer.append("]");
			strings.add(timeBuffer.toString());
			strings.add(countBuffer.toString());
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strings;
	}
	/**
	 * 按周保存的分析结果
	 * */
	private class ResultByWeek
	{
		String date;
		int dateNumber;
		int postsNumber;
		Future<ThemeAnalysisPlus.Result> theme;
//		Map<String, Integer> postsType;
		
		public ResultByWeek(String date, int weekNumber)
		{
			this.date = date;
			this.dateNumber = weekNumber;
		}
		
		public void setPostsNumber(int number)
		{
			this.postsNumber = number;
		}
		
		public void setTheme(Future<ThemeAnalysisPlus.Result> theme)
		{
			this.theme = theme;
		}
		
//		public void setTypeNumber(String typeName, int typeCount)
//		{
//			this.postsType.put(typeName, new Integer(typeCount));
//		}
	}
}
