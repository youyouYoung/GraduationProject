/**
 * collection.crawler包被定义为爬虫程序包
 * */
package collection.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Crawler
{
	private String urlFilePath; //保存原始url的文件
	private String urlDoneFilePath; //保存已读取过的url的文件
	private final ExecutorService threadPool;
	
	public Crawler(String filePath)
	{
		this.urlFilePath = filePath;
		this.urlDoneFilePath = filePath + ".done";
		threadPool = Executors.newFixedThreadPool(30);
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
				while ((line = buffReader.readLine()) != null)
				{
					if (line.contains("forum.php?mod=forumdisplay"))
					{
						continue;
					}
					//threadPool.submit(new PageContent(line));
					new PageContent(line).run();
					buffWriter.write(line + "\n");
					buffWriter.flush();
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
	}
}
