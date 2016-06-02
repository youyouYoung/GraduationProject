package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import trend.analysis.PublicOpinionByTime;
import util.PropertiesKey;
import util.PropertiesReader;

public class MyResult
{
	private static final String basePath = System.getProperties().getProperty("user.dir");
	public static void main(String[] args)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("请输入需要查看的时间(格式:yyyy-MM-dd): ");
		Scanner in = new Scanner(System.in);
		String line = in.nextLine();
		//System.out.println(line);
		PublicOpinionByTime publicOpinion = new PublicOpinionByTime();
		if (line != null)
		{
			try
			{
				dateFormat.parse(line);
				publicOpinion.setBeginDate(line);
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				System.err.println("输入的时间格式错误, 程序将从当前时间开始分析.");
			}
		}
		in.close();
		
		publicOpinion.publicOpinionAnalysis();
		ArrayList<ArrayList<String>> result = publicOpinion.displayThemes();
		makeThemeHTML(result);
		
		ArrayList<String> name = publicOpinion.displayPostsCount();
		makeWeekHTML(name, line);
//		for (String string : name)
//		{
//			System.out.println(string);
//		}
	}
	
	public static void makeThemeHTML(ArrayList<ArrayList<String>> result)
	{
		String modelLocation = basePath + PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_THEMEPAGE);
		String themePage = basePath + PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_WEBPAGELOCATION);
		try
		{
			BufferedReader reader = null;
			for (ArrayList<String> arrayList : result)
			{
				reader = new BufferedReader(new FileReader(new File(modelLocation)));
				//System.out.println(themePage + arrayList.get(0) + ".jsp");
				File file = new File(themePage + arrayList.get(0) + "Theme.jsp");
				if (file.exists())
				{
					file.delete();
				}
				file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				
				String line = null;
				boolean chageTitle = false;
				boolean changTheme = false;
				while ((line = reader.readLine()) != null)
				{
					if (!chageTitle)
					{
						if (line.contains("<title>ECharts</title>"))
						{
							writer.write("<title>第"+arrayList.get(1)+"周主题分析</title>\n");
							chageTitle = true;
						}
						else
						{
							writer.write(line+"\n");
						}
					}
					else if (!changTheme)
					{
						if (line.contains("x: 'left',data:"))
						{
							writer.write("x: 'left',data:"+arrayList.get(2)+"\n");
							changTheme = true;
						}
						else
						{
							writer.write(line+"\n");
						}
					}
					else
					{
						if (line.contains("},data:"))
						{
							writer.write("},data:"+arrayList.get(3)+"\n");
						}
						else 
						{
							writer.write(line+"\n");
						}
					}
					writer.flush();
				}
				System.out.println("第"+arrayList.get(1)+"周主题分布图已生成,访问链接: http://localhost:8080/pom/"+file.getName());
				writer.close();
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void makeWeekHTML(ArrayList<String> arrayList, String title)
	{
		String modelLocation = basePath + PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_WEEKPAGE);
		String themePage = basePath + PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_WEBPAGELOCATION);

		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		try
		{
			reader = new BufferedReader(new FileReader(new File(modelLocation)));
			File file = new File(themePage + title + "week.jsp");
			writer = new BufferedWriter(new FileWriter(file));
			boolean changeTitle = false;
			boolean changeDate = false;
			String line = null;
			
			while ((line = reader.readLine()) != null)
			{
				if (!changeTitle)
				{
					if (line.contains("<title>ECharts</title>"))
					{
						writer.write("<title>截止"+title+"帖子发布量趋势</title>\n");
						changeTitle = true;
					}
					else
					{
						writer.write(line+"\n");
					}
				}
				else if (!changeDate)
				{
					if (line.contains("type : 'category',data :"))
					{
						writer.write("type : 'category',data :"+arrayList.get(0)+"\n");
						changeDate = true;
					}
					else
					{
						writer.write(line+"\n");
					}
				}
				else
				{
					if (line.contains("type:'bar',data:"))
					{
						writer.write("type:'bar',data:"+arrayList.get(1)+"\n");
					}
					else
					{
						writer.write(line+"\n");
					}
				}
				writer.flush();
			}
			System.out.println("截止"+title+"每周帖子数量变化趋势显示图已生成,访问链接: http://localhost:8080/pom/"+file.getName());
			reader.close();
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
