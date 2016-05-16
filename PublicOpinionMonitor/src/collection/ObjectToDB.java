/**
 * 功能:
 * 		将一个或者多个JiSuObject对象保存在数据库中.
 * 
 * 描述:
 * 		该类会在初始化时创建一个集合, 用于保存JiSuObject对象.
 * 		调用flush()会将集合中的JiSuObject对象保存在数据库中.
 * 
 * */
package collection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import collection.json.JiSuObject;
import database.BaseConnection;

public class ObjectToDB
{
	private final List<JiSuObject> objects;
	private final BaseConnection connection;
	
	public ObjectToDB()
	{
		objects  = new LinkedList<JiSuObject>();
		connection = BaseConnection.getInstance();
	}
	
	/**
	 * 将objects队列中的JiSuObject元素保存在数据库中.
	 * @return 正确插入的JiSuObject元素的个数
	 * */
	private int toDB()
	{
		String sql = null;
		int count = 0;
		try
		{
			
			Statement state = connection.getStatement();
			for (JiSuObject object : objects)
			{
				//将url, title, content保存在page_info中.
				String title = formatString(object.getTitle());
				String url = formatString(object.getURL());
				String content = formatString(object.getPageContent());
				sql = "insert into page_info (page_link, page_title, page_content) values ('" + url + "', '" + title+ "', '" + content + "')";
				
				//成功在page_info中插入数据
				if (state.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS) == 1)
				{
					//获取刚刚向表page_info中插入数据的pi_id的值
					//sql = "select LAST_INSERT_ID() as pi_id from page_info limit 1";
					ResultSet set = state.getGeneratedKeys(); //state.executeQuery(sql);
					set.last();
					int pi_id = set.getInt(1);

					//将JsonObject中的所有帖子插入page_content中.
					List<ArrayList<String>> newPosts = object.getNewPosts();
					for (ArrayList<String> post : newPosts)
					{
						String author = formatString(post.get(0));	//作者信息
						String postContent = formatString(post.get(1)); //帖子正文
						String postDate = post.get(2);
						sql = "insert into page_content (pi_id, posts_author, posts_content, posts_date) values ('" + pi_id + "', '" + author + "', '" + postContent + "', '" + postDate + "')";
						if (state.executeUpdate(sql) == 1)
							count++;
					}
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
		catch (java.lang.NullPointerException e) 
		{
			// TODO: handle exception
			System.err.println();
		}
		return count;
	}
	
	/**
	 * 格式化原始数据, 效果如下:
	 * 原数据		格式化数据
	 * 	\			\\
	 * 	'			\'
	 * 	"			\"
	 * 
	 * @return 格式化的结果, 如果输入null返回null.
	 * */
	private String formatString(String orignString)
	{
		return orignString == null ? null : orignString.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\\\"");
	}

	/**
	 * 此方法是一个刷新方法, 目的是将该对象中的jisuObject对象保存在数据库中.
	 * @return 没有被保存数据的个数
	 * */
	public int flush()
	{
		if (objects.size() > 0)
		{
			return objects.size() - toDB();
		}
		return 0;
	}
	
	/**关闭该对象使用时调用的资源*/
	public void close()
	{
		connection.close();
	}
	
	/**
	 * 将jisuObject保存在该对象的队列中
	 * @param object jisuObject对象
	 * */
	public void addJiSuObject(JiSuObject object)
	{
		objects.add(object);
	}
}
