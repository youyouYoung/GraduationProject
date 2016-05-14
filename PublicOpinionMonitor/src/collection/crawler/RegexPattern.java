/**
 * 匹配规则类. 该类用于创建一个特殊匹配规则的Pattern对象.
 * 
 * 获取传入字符串中需要的匹配项.
 * */
package collection.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import collection.json.JiSuObject;

public class RegexPattern
{
	private final String data;
	private JiSuObject jisuObject;
	
	public RegexPattern(String allData)
	{
		this.data = allData;
		jisuObject = new JiSuObject();
	}
	
	public JiSuObject getObject()
	{
		String title = null;
		boolean foundAuthor = false; //是否找到作者, 初始设定没有
		boolean foundDate = false;	//是否找到时间
		boolean postBegin = false; //帖子是否开始
		boolean isQuote = false; //是否为引用
		String author = null;
		StringBuffer post = null;
		String date = null;
		
		for (String line : data.split("\\n"))
		{
			//找标题
			if (title == null)
			{
				Matcher matcher = getTitle().matcher(line);
				if (matcher.find())
				{
					title = matcher.group(1);
					jisuObject.setTitle(title);
				}
			}
			
			//找作者
			if (!foundAuthor && title != null)
			{
				post = new StringBuffer();
				Matcher matcher = getAuthor().matcher(line);
				if (matcher.find())
				{
					author = matcher.group(1);
					foundAuthor = true;
				}
			}
			
			//找发布时间
			if (foundAuthor && !foundDate && title != null)
			{
				Matcher matcher = getTime().matcher(line);
				if (matcher.find())
				{
					date = matcher.group(1);
					foundDate = true;
				}
			}
			
			//找帖子
			if (foundAuthor && foundDate && title != null)
			{
				if (!postBegin)
				{
					Matcher matcher = startPost().matcher(line);
					if (matcher.find())
					{
						postBegin = true;
					}
				}
				if (postBegin)
				{
					Matcher startQuoteMatcher = startQuote().matcher(line);
					Matcher endQuoteMatcher = endQuote().matcher(line);
					//判断是否为引用
					if (startQuoteMatcher.find())
					{
						isQuote = true;
					}
					//如果是引用部分
					if (isQuote)
					{
						//判断引用是否结束
						if (endQuoteMatcher.find())
						{
							isQuote = false;
							continue;
						}
						else 
						{
							continue;
						}						
					}
					else
					{
						//非引用部分
						post.append(line);
						Matcher matcher = endPost().matcher(line);
						if (matcher.find())
						{
							foundAuthor = false;
							foundDate = false;
							postBegin = false;
							
							jisuObject.addPost(author, post.toString().replaceAll("<.*?>", " "), date);
						}
					}
				}
				
			}
		}
		
		return jisuObject;
	}
	
	private Pattern getTitle()
	{
		return Pattern.compile("<meta name=\"keywords\" content=\"(.*?)\" />");
	}
	
	private Pattern getAuthor()
	{
		return Pattern.compile("<a href=\"home\\.php.*\" target=\"_blank\" class=\"xw1\"><font color=\".*?\">(.*?)</font></a>");
	}
	
	private Pattern getTime()
	{
		return Pattern.compile("发表于.*(\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2})");
	}
	
	/**
	 * 帖子的开始位置
	 * */
	private Pattern startPost()
	{
		return Pattern.compile("<table cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"t_f\" id=");
	}
	
	/** 引用开始位置 */
	private Pattern startQuote()
	{
		return Pattern.compile("<div class=\"quote\"><blockquote>");
	}
	
	/** 引用结束位置 */
	private Pattern endQuote()
	{
		return Pattern.compile("</blockquote></div><br />");
	}
	
	/**帖子的结束位置*/
	private Pattern endPost()
	{
		return Pattern.compile("</td></tr></table>");
	}
}
