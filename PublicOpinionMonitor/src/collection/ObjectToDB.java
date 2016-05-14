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
	private final List<JiSuObject> objects = new LinkedList<JiSuObject>();
	private final BaseConnection connection = BaseConnection.getInstance();
	
	/**
	 * 将objects队列中的jisuobject元素保存在数据库中.
	 * 
	 * */
	private void toDB()
	{
		String sql = "";
		try
		{
			Statement state = connection.getStatement();
			for (JiSuObject object : objects)
			{
				try
				{
					//将url, title保存在page_info中.
					String title = object.getTitle().replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\\\"");
					String url = object.getURL().replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\\\"");
					String content = object.getPageContent().replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\\\"");
					sql = "insert into page_info (page_link, page_title, page_content) values ('" + url + "', '" + title+ "', '" + content + "')";
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
						String author = post.get(0).replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\\\"");
						String postContent = post.get(1).replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\\\"");
						String postDate = post.get(2);
						sql = "insert into page_content (pi_id, posts_author, posts_content, posts_date) values ('" + pi_id + "', '" + author + "', '" + postContent + "', '" + postDate + "')";
						state.executeUpdate(sql);
					}
				}
				catch (java.lang.NullPointerException e) 
				{
					// TODO: handle exception
					System.err.println();
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
	}

	/**
	 * 此方法是一个刷新方法, 目的是将该对象中的jisuObject对象保存在数据库中
	 * */
	public void flush()
	{
		if (objects.size() > 0)
		{
			toDB();
		}
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
	public void putJiSuObject(JiSuObject object)
	{
		objects.add(object);
	}
}
