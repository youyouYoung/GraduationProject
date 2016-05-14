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
import collection.json.JiSuObject;
import collection.json.ToJsonObject;

public class FileToDB
{
	//处理文件内容的方式.
	private ToJsonObject toObject;
	private ObjectToDB objectToDB;
	
	/**
	 * 初始化FileToDB
	 * @param toObject 传入一个解析文件中json内容的方法类
	 * */
	public FileToDB(ToJsonObject toObject)
	{
		this.toObject = toObject;
		objectToDB = new ObjectToDB();
	}
	
	/**
	 * 将文件中的内容转化为JsonObject保存在List中.
	 * @param jsonFile json文件或者文件所在的目录
	 * */
	private void toObject(File jsonFile)
	{
		if (!jsonFile.isDirectory())
		{
			JiSuObject object;
			if ((object = toObject.toJsonObject(jsonFile)) != null)
			{
				objectToDB.putJiSuObject(object);
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
	
	public void filetoDB(String filePath)
	{
		toObject(new File(filePath));
		objectToDB.flush();
	}	
}
