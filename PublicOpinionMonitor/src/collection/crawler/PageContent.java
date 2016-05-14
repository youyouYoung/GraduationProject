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

import collection.ObjectToDB;
import collection.json.JiSuObject;

public class PageContent implements Runnable
{
	private final String pageUrl;
	
	public PageContent(String url)
	{
		this.pageUrl = url;
	}
	
	public void run()
	{
		try
		{
			URL url = new URL(pageUrl);
			URLConnection connection = url.openConnection();
			BufferedReader buffReader = null;
			
			//为访问极速论坛添加cookie
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
			connection.setRequestProperty("Cookie", "WRoT_2132_saltkey=hjR9LBk9; WRoT_2132_lastvisit=1460986155; WRoT_2132_nofavfid=1; resetedvote=1; btuid=34635; btpassword=7751d5dc37857576735cb383c8948234; WRoT_2132_auth=1f1dfb%2BPYiJPE1YXkuFUs76JQVT%2FHcXLSaRZbfimEBIr6LPDJrAsCWzbnJbRGr1Sqbcb5u%2FJUl3cjmN9HP1XosimCQ; WRoT_2132_forum_lastvisit=D_257_1462172514D_153_1462624432D_177_1462624514D_50_1462624557; WRoT_2132_home_readfeed=1462627992; WRoT_2132_visitedfid=50D6D4D177D153D27D257D26D92D32; WRoT_2132_smile=5D1; ver=2; st=1; PHPSESSID=b8f7328a845f1a1ca60f4f0b1d5bec15; WRoT_2132_ulastactivity=2c33RI8Medp%2FGAK4jPmCEhHP71TbeVHc0xrzJXvGiFW0EYC6NHfF; WRoT_2132_home_diymode=1; WRoT_2132_sendmail=1; WRoT_2132_onlineusernum=625; WRoT_2132_sid=VVRWGw; WRoT_2132_lastcheckfeed=45553%7C1462878968; WRoT_2132_checkfollow=1; WRoT_2132_lastact=1462878968%09home.php%09spacecp; WRoT_2132_checkpm=1; CNZZDATA1747350=cnzz_eid%3D1875484574-1460955811-http%253A%252F%252Fbitpt.cn%252F%26ntime%3D1462878399");
			
			try
			{
				buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer allData = new StringBuffer();//保存所有数据
				connection.connect();
				String line = null;
				
				//读取网页中全部数据
				while ((line = buffReader.readLine()) != null)
				{					
					//保存该行数据
					allData.append(line.replaceAll("&nbsp[;]?", " ").trim()+"\n");
				}
				
				RegexPattern regexPattern = new RegexPattern(allData.toString());
				JiSuObject jisuObject = regexPattern.getObject();
				jisuObject.setPageContent(allData.toString());
				jisuObject.setURL(pageUrl);
				
				ObjectToDB objectToDB = new ObjectToDB();
				objectToDB.putJiSuObject(jisuObject);
				objectToDB.flush();
			}
			finally
			{
				buffReader.close();
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
	}
}
