/**
 * 表示Json文件内容的对象.该对象用于临时保存Json文件内容.
 * 
 * ----------------------------------------------------------------
 * 每个Json文件中的内容包括:
 * 	1.网页链接
 * 	2.网页标题
 * 	3.帖子(作者, 内容)
 * ----------------------------------------------------------------
 * 
 * 注意:
 * 	1.起初想使用Map对象保存所有的帖子. 其中author作为key, content作为value. 但是Map不能保存相同的key会导致同一个标题下的一个作者只能有一篇帖子.
 * */
package collection.json;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JsonObject
{
	private String url;		//保存网页链接
	private String title;	//保存网页标题
	//保存该页面所有的帖子,其中每个帖子有一个ArrayList保存.第一个元素为author,第二个是content.
	private List<ArrayList<String>> newPosts;	
	
	/**
	 * 初始化JsonObject对象,该对象的创建需要网页链接和标题.
	 * 同时会创建一个集合保存所有的帖子.
	 * @param url 网页链接
	 * @param title 网页标题
	 * */
	public JsonObject(String url, String title)
	{
		this.url = url;
		this.title = title;
		newPosts = new LinkedList<ArrayList<String>>();
	}
	
	public void setURL(String urlString)
	{
		this.url = urlString;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * 向该对象中添加帖子内容, 包括作者和正文
	 * @param author 帖子作者
	 * @param content 帖子正文
	 * */
	public void addPost(String author, String content)
	{
		ArrayList<String> post = new ArrayList<String>();
		post.add(author);
		post.add(content);
		newPosts.add(post);
	}
	
	public String getURL()
	{
		return this.url;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	/**
	 * 返回保存所有帖子的集合.
	 * @return 使用List集合保存的所有帖子.
	 * */
	public List<ArrayList<String>> getNewPosts()
	{
		return newPosts;
	}
	
	@Override
	public String toString()
	{
		return "Title is: " + this.title + ", and url is: " + this.url;
	}
}
