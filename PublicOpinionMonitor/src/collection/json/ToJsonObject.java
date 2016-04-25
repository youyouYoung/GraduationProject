/**
 * 
 * 将文本中的内容转化为JsonObject对象的类. 
 * 由于不同的文本转化方式不同所以此处只是定义了一个接口.
 * 
 * ----------------------------------------------------------------
 * 可能需要转化的文本有:
 * 		1.从极速论坛中爬取的文件.
 * ----------------------------------------------------------------
 * 
 * */
package collection.json;

import java.io.File;

import org.json.JSONException;

public interface ToJsonObject
{
	/**
	 * 将字符串转化为JsonObject对象.
	 * @param json 待转化的字符串
	 * @return JsonObject 将字符串转化为一个对象,无法读取返回NULL
	 * */
	public JsonObject toJsonObject(String json) throws JSONException;
	
	/**
	 * 将文件转化为JsonObject对象.
	 * @param jsonFile 待转化的文件
	 * @return JsonObject 转化后的对象,无法读取返回NULL
	 * */
	public JsonObject toJsonObject(File jsonFile);
}
