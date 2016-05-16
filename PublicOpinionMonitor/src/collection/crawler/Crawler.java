/**
 * collection.crawler包被定义为爬虫程序包
 * 
 * 功能:
 * 		由于时间限制目前的爬虫只能爬取指定链接的内容, 首先将链接保存在一个文件中然后提供文件路径即可.
 * 
 * 缺陷:
 * 		在多线程执行爬取任务时, 由于使用同一个cookie爬取多个网页时会无法获取, 此问题还没解决.
 * 		具体描述: 爬取极速论坛数据时需要添加cookie才能获取内容, 串行获取url内容时可获取网页内容, 并行获取时会出现503错误.
 * */
package collection.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Crawler
{
	private String urlFilePath; //保存原始url的文件
	private String urlDoneFilePath; //保存已读取过的url的文件
	private final ExecutorService threadPool;
	private List<Future<String>> answer;
	
	public Crawler(String filePath)
	{
		this.urlFilePath = filePath;
		this.urlDoneFilePath = filePath + ".done";
		threadPool = Executors.newFixedThreadPool(300);
		answer = new LinkedList<Future<String>>();
	}
	
	public void start()
	{
		File inputFile = new File(urlFilePath);
		File outputFile = new File(urlDoneFilePath);
		
		try
		{
			outputFile.createNewFile();
			BufferedReader buffReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter buffWriter = new BufferedWriter(new FileWriter(outputFile));
			
			try
			{
				String line = null;
				
				/*
				 * version 1
//				while ((line = buffReader.readLine()) != null)
//				{
//					new PageContent(line).call();
//					buffWriter.write(line + "\n");
//					buffWriter.flush();
//				}
				*/
				
				/*
				 * version 2
				 * */
				while ((line = buffReader.readLine()) != null)
				{
					if (line.contains("forum.php?mod=viewthread"))
					{
						answer.add(threadPool.submit(new PageContent(line)));
					}
				}
				Iterator<Future<String>> iterators = answer.iterator();
				while (iterators.hasNext())
				{
					Future<String> future = iterators.next();
					if (future.isDone())
					{
						try
						{
							String url = future.get();
							if (url != null)
							{
								buffWriter.write(url + "\n");
								buffWriter.flush();
							}
						}
						catch (ExecutionException e)
						{
							// TODO Auto-generated catch block
							e.getCause().printStackTrace();
						}
					}
				}
			}
			finally
			{
				buffReader.close();
				buffWriter.close();
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
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
