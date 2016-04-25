/**
 * 将文件中的内容保存在数据库中, 文件中为json字符串.
 * 
 * --------------------------------------------------------------------------
 * 待改进:
 * 	1. 将读文件保存在对象中和读取对象中内容保存在数据库中改为多线程可以同时执行
 * 	2. JsonObject保存在List中可能会使内存越界
 * --------------------------------------------------------------------------
 * 
 * */
package collection;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import collection.database.BaseConnection;
//import collection.json.JiSuToJsonObject;
import collection.json.JsonObject;
import collection.json.ToJsonObject;

public class FileToDB
{
	//处理文件内容的方式.
	private ToJsonObject toObject;
	private final List<JsonObject> objects = new LinkedList<JsonObject>();
	
	/**
	 * 初始化FileToDB
	 * @param toObject 传入一个解析文件中json内容的方法类
	 * */
	public FileToDB(ToJsonObject toObject)
	{
		this.toObject = toObject;
	}
	
	/**
	 * 将文件中的内容转化为JsonObject保存在List中.
	 * @param jsonFile json文件或者文件所在的目录
	 * */
	private void toObject(File jsonFile)
	{
		if (!jsonFile.isDirectory())
		{
			JsonObject object;
			if ((object = toObject.toJsonObject(jsonFile)) != null)
			{
				objects.add(object);
			}
		}
		else
		{
			File[] files = jsonFile.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				//递归调用
				toObject(files[i]);
			}
		}		
	}
	
	/**
	 * 
	 * 将List中的内容保存在数据库中.
	 * 一个JsonObject中含有一组url和title, 同时有多个帖子.
	 * 所以每个JsonObject会page_info中插入一条数据, 在page_content中插入多条数据.
	 * 
	 * */
	private void toDB()
	{
		BaseConnection connection = BaseConnection.getInstance();
		String sql = "";
		try
		{
			Statement state = connection.getStatement();
			for (JsonObject object : objects)
			{
				//将url, title保存在page_info中.
				String title = object.getTitle();
				String url = object.getURL();
				sql = "insert into page_info (page_link, page_title) values ('" + url + "', '" + title+ "')";
				state.executeUpdate(sql);
				
				//获取刚刚向表page_info中插入数据的pi_id的值
				sql = "select LAST_INSERT_ID() as pi_id from page_info limit 1";
				ResultSet set = state.executeQuery(sql);
				set.last();
				int pi_id = set.getInt(1);

				//将JsonObject中的所有帖子插入page_content中.
				List<ArrayList<String>> newPosts = object.getNewPosts();
				for (ArrayList<String> post : newPosts)
				{
					sql = "insert into page_content (pi_id, posts_author, posts_content) values ('" + pi_id + "', '" + post.get(0) + "', '" + post.get(1) + "')";
					state.executeUpdate(sql);
				}
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			System.err.println("This SQL is: " + sql);
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			connection.close();
		}
	}
	
	public void filetoDB(String filePath)
	{
		toObject(new File(filePath));
		toDB();
	}
	
	//public static void main(String[] args)
	//{
		//FileToDB jsonToDB = new FileToDB(new JiSuToJsonObject());
		//jsonToDB.filetoDB("/home/youyou/Public/bitpt.cn/");
	//}
}
