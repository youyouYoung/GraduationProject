/**
 * 基于时间顺序的舆情分析
 * */
package trend.analysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import topic.lda.LdaUtil;
import util.PropertiesKey;
import util.PropertiesReader;

public class PublicOpinionByTime extends PublicOpinion
{
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Calendar calendar;// = Calendar.getInstance();
	private Date beginDate; //= dateFormat.format(calendar.getTime());
	private List<Future<ThemeAnalysis.Result>> themesResult = new LinkedList<Future<ThemeAnalysis.Result>>();
	//需要获取的周的数量
	private final int weekCount = new Integer(PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_WEEKNUMBER)).intValue(); 
	

	public PublicOpinionByTime()
	{
		super();
		calendar = Calendar.getInstance();
		beginDate = calendar.getTime();
	}
	
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
	
	@Override
	public void publicOpinionAnalysis()
	{
		calendar.setTime(beginDate);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//本周周一的日期
		calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - calendar.get(Calendar.DAY_OF_WEEK));
		
		for (int i = 0; i < weekCount; i++)
		{
			String startDate = dateFormat.format(calendar.getTime()); //本周一
			//int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR); //本周是这一年的第几周
			calendar.add(Calendar.DATE, 6); //跳转到本周周日
			String endDate = dateFormat.format(calendar.getTime()); //本周日
						
			String sql = "select posts_content from page_content where posts_date between '" + startDate + "' and '" + endDate + "'";
			Future<ThemeAnalysis.Result> result = getAnalysisTask(sql);
			if (result != null)
			{
				themesResult.add(result);
			}
						
			calendar.add(Calendar.DATE, -13); //跳转到上周一
		}
	}
	
	@Override 
	public void displayResult()
	{
		for (Future<ThemeAnalysis.Result> task : themesResult)
		{
			ThemeAnalysis.Result result;
			try
			{
				result = task.get();
				BlockingQueue<String> keywords = result.getKeywords();
				Map<String, Double>[] themes = result.getThemes();
				
				//System.out.println("第 " + weekNumber + " 周:");
				for (String keyword : keywords)
				{
					System.out.println(keyword);
				}
				LdaUtil.explain(themes);
			}
			catch (InterruptedException | ExecutionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
