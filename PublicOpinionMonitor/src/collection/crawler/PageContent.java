/**
 * 多线程的方式获取url的内容.
 * 包括网页原始数据和匹配数据分别保存在数据库中.
 * */
package collection.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import collection.ObjectToDB;
import collection.json.JiSuObject;

public class PageContent implements Callable<String>
{
	private final String pageUrl;
	
	public PageContent(String url)
	{
		this.pageUrl = url;
	}
	
	public String call()
	{
		String result = null; 
		try
		{
			URL url = new URL(pageUrl);
			URLConnection connection = url.openConnection();
			BufferedReader buffReader = null;
			
			//为访问极速论坛添加cookie
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
			connection.setRequestProperty("Cookie", "WRoT_2132_saltkey=hjR9LBk9; WRoT_2132_lastvisit=1460986155; WRoT_2132_nofavfid=1; resetedvote=1; WRoT_2132_home_readfeed=1462627992; btuid=34635; btpassword=7751d5dc37857576735cb383c8948234; WRoT_2132_auth=e5e9G8AZv41nE%2FCbo9JKdneFnFLdmc5%2F7VkMrhtFNIyBm0az8C6QFA0f%2Bn0bA1RVZmkXm00vpxZ5xSFKHByWF0toRA; WRoT_2132_lastviewtime=45553%7C1463217359; PHPSESSID=b251f124e735b29d22b393049598beb9; WRoT_2132_forum_lastvisit=D_257_1462172514D_153_1462624432D_177_1462624514D_260_1462929562D_244_1462964186D_176_1463217367D_50_1463296334; ver=2; st=1; WRoT_2132_ulastactivity=a08eZKk5quVQfKoBhq12HCsD4haP3APZljJKRSCJrGdO7BFuWUoq; WRoT_2132_smile=5D1; WRoT_2132_visitedfid=153D244D50D4D176D149D177D257D27D82; WRoT_2132_viewid=tid_407986; WRoT_2132_sid=IqkoCH; WRoT_2132_checkpm=1; WRoT_2132_lastcheckfeed=45553%7C1463323649; WRoT_2132_checkfollow=1; WRoT_2132_lastact=1463323649%09home.php%09misc; WRoT_2132_sendmail=1; CNZZDATA1747350=cnzz_eid%3D1875484574-1460955811-http%253A%252F%252Fbitpt.cn%252F%26ntime%3D1463321293");
			
			buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer allData = new StringBuffer();//保存所有数据
			connection.connect();
			String line = null;
			if (buffReader != null)
			{
				try
				{
					//读取网页中全部数据
					while ((line = buffReader.readLine()) != null)
					{					
						//保存该行数据
						allData.append(line.replaceAll("&nbsp[;]?", " ").trim()+"\n");
					}
					if (allData.length() > 10)
					{
						RegexPattern regexPattern = new RegexPattern(allData.toString());
						JiSuObject jisuObject = regexPattern.getObject();
						jisuObject.setPageContent(allData.toString());
						jisuObject.setURL(pageUrl);
						
						ObjectToDB objectToDB = new ObjectToDB();
						objectToDB.addJiSuObject(jisuObject);
						if (objectToDB.flush() == 0)
							result = pageUrl;
					}
				}
				finally
				{
					buffReader.close();
				}
			}
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			// 无法创建url对象.
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			//无法创建urlconnection
			e.printStackTrace();
		}
		return result;
	}
}
