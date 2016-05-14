/**
 * 极速论坛中每一个页面对应一个JiSuObject.
 * 
 * ----------------------------------------------------------------
 * 每个JiSuObject对象包括:
 * 	1.网页链接
 * 	2.网页标题
 * 	3.使用集合保存的帖子, 每个帖子为集合中的一个元素. 每个帖子具有作者,发表时间,正文这些内容.
 * 	4.网页原始内容.
 * ----------------------------------------------------------------
 * 待改进:
 * 	1.判断两个JsonObject相同
 * ----------------------------------------------------------------
 * 
 * 注意:
 * 	1.起初想使用Map对象保存所有的帖子. 其中author作为key, content作为value. 但是Map不能保存相同的key会导致同一个标题下的一个作者只能有一篇帖子.
 * */
package collection.json;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JiSuObject
{
	private String url;		//保存网页链接
	private String title;	//保存网页标题
	private String pageContent;	//网页全部内容
	//保存该页面所有的帖子,其中每个帖子有一个ArrayList保存.第一个元素为author,第二个是content,第三个保存时间
	private List<ArrayList<String>> newPosts;	
	
	/**
	 * 初始化JsonObject对象,该对象的创建需要网页链接和标题.
	 * 同时会创建一个集合保存所有的帖子.
	 * @param url 网页链接
	 * @param title 网页标题
	 * */
	public JiSuObject(String url, String title)
	{
		this.url = url;
		this.title = title;
		newPosts = new LinkedList<ArrayList<String>>();
	}
	
	public JiSuObject()
	{
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
	
	public void setPageContent(String content)
	{
		this.pageContent = content;
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
	
	/**
	 * 向该对象中添加帖子内容, 包括作者和正文
	 * @param author 帖子作者
	 * @param content 帖子正文
	 * */
	public void addPost(String author, String content, String date)
	{
		ArrayList<String> post = new ArrayList<String>();
		post.add(author);
		post.add(content);
		post.add(date);
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
	
	public String getPageContent()
	{
		return this.pageContent;
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
