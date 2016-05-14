/**
 * 该类实现了ToJsonObject接口, 用于将从极速论坛上获取的文件转化为JsonObject对象.
 * 
 * ----------------------------------------------------------------------------
 * 待改进:
 * 	1.将json文件中key的名字使用属性文件获取.
 * ----------------------------------------------------------------------------
 * */
package collection.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.PropertiesKey;
import util.PropertiesReader;

public class JiSuToJsonObject implements ToJsonObject
{
	//json文件中保存url, title, authors, contents值的键的名称.
	private final String urlName = PropertiesReader.getjsonKeyInfo(PropertiesKey.JSON_JISUURL);
	private final String titleName = PropertiesReader.getjsonKeyInfo(PropertiesKey.JSON_JISUTITLE);
	private final String authorsName = PropertiesReader.getjsonKeyInfo(PropertiesKey.JSON_JISUAUTHORS);
	private final String contentName = PropertiesReader.getjsonKeyInfo(PropertiesKey.JSON_JISUPOSTS);

	@Override
	public JiSuObject toJsonObject(String json) throws JSONException
	{
		JiSuObject jsonObject = null;
		JSONObject obj = new JSONObject(json);
		
		String url =  obj.get(urlName).toString();
		String title = obj.get(titleName).toString().replace("'", "\\'");
		JSONArray authorArray = obj.getJSONArray(authorsName);
		JSONArray contentArray = obj.getJSONArray(contentName);

		jsonObject = new JiSuObject(url, title);
		for (int i = 0; i < contentArray.length(); i++)
		{
			String author = authorArray.length() > i ? authorArray.get(i).toString().replace("'", "\\'") : "lost";
			jsonObject.addPost(author, contentArray.get(i).toString().replace("'", "\\'"));
		}

		return jsonObject;
	}
	
	@Override
	public JiSuObject toJsonObject(File jsonFile)
	{
		JiSuObject object = null;
		try
		{
				BufferedReader bReader = new BufferedReader(new FileReader(jsonFile));
				
				try
				{
					String jsonString = bReader.readLine();
					if (jsonString != null)
					{
						object = toJsonObject(jsonString);
					}
				}
				catch (IOException e)
				{
					// TODO: 读取失败时处理的异常
					System.err.println("Can't read file in: "+jsonFile.getPath());
				}
				catch (JSONException e) 
				{
					// TODO: 文件内容无法被解析为json字符串时处理的异常
					System.err.println("Is that a real json file in: "+ jsonFile.getPath());
				}
				finally
				{
					bReader.close();
				}
		}
		catch (FileNotFoundException notFoundException)
		{
			// TODO: 无法找到需要读取的文件时处理的异常
			System.err.println("There is not file in: "+jsonFile.getPath());
		}
		catch (IOException e) 
		{
			// TODO: 无法关闭读写流时处理的异常
			System.err.println("Can't close BufferedReader of a file");
		}
		return object;
	}
}
