/**
 * 测试类, 不会被上传
 * 
 * */
package collection.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTest
{
	public static void main(String[] args)
	{
		CrawlerTest demo = new CrawlerTest();
		
		demo.test5();
	}
	
	public void test1()
	{
		//	待匹配的字符串
		String line = "<div class=\"authi\"><a href=\"home.php?mod=space&amp;uid=61565\" target=\"_blank\" class=\"xw1\"><font color=\"#E60383\">董雅茹♀</font></a>";
		//匹配模式
		final String pattern = "<a href=\"home\\.php.*\" target=\"_blank\" class=\"xw1\"><font color=\".*?\">(.*?)</font></a>";//(.*)(\\d+)(.*)
		
		final Pattern r = Pattern.compile(pattern);
		Matcher matcher = r.matcher(line);
		
		if (matcher.find())
		{
			System.out.println(matcher.group(1));
			//System.out.println("Found value: " + matcher.group(0) );
	        //System.out.println("Found value: " + matcher.group(1) );
	        //System.out.println("Found value: " + matcher.group(2) );
		}
		else
		{
			System.out.println("NO MATCH");
		}
	}
	
	public void test2(File inFile, File outFile)
	{
		if (inFile.isDirectory())
		{
			File[] files = inFile.listFiles();
			for (File file2 : files)
			{
				test2(file2, outFile);
			}
		}
		else
		{
			try
			{
				BufferedReader bufReader = null;
				BufferedWriter bufWriter = null;
				//FileReader fileReader = null;
				try
				{
					bufReader = new BufferedReader(new FileReader(inFile));
					String line = bufReader.readLine().substring(4).trim();//去掉开头的url:
					bufWriter = new BufferedWriter(new FileWriter(outFile));
					bufWriter.append(line + "\n");
					bufWriter.flush();
				}
				finally
				{
						//fileReader.close();
					bufReader.close();
					bufWriter.close();
				}
				
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
		
	public void test4()
	{
		String line = "'sasf'qwrw\\qr\"afsf\"";
		System.out.println(line.replaceAll("\\\\", "\\\\\\\\"));
	}
	
	public void test5()
	{
		Crawler crawler = new Crawler("/home/youyou/Public/urls.txt");
		crawler.start();
	}
}
