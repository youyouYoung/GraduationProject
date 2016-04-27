/**
 * 获取使用ICTCLAS所需的参数
 * */
package topic.participle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.sun.jna.Platform;

public class ICTCLASParameter
{
	/**
	 * 获取当前系统可以使用的ICTCLAS支撑库
	 * */
	public static String getICTCLAS()
	{
		//指向ICTCLAS的绝对路径
		StringBuffer path = new StringBuffer(System.getProperties().getProperty("user.dir"));
		path.append(getPropertiesValue("topic_ictclas_path"));
		path.append(selectICTCLAS());
		
		return path.toString();
	}
	
	/**
	 * 获取ICTCLAS核心词库位置.
	 * */
	public static String getDataLocation()
	{
		String dataLocaion = getPropertiesValue("topic_ictclas_data_path");
		return dataLocaion == null ? "" : dataLocaion;
	}
	
	/**
	 * 
	 * 判断系统使用的ICTCLAS版本,可能的版本有:
	 * win32/NLPIR.dll
	 * win64/NLPIR.dll
	 * linux32/libNLPIR.so
	 * linux64/libNLPIR.so
	 * 
	 * */
	private static String selectICTCLAS()
	{
		StringBuffer ictclasType = new StringBuffer();
		boolean flag = Platform.isWindows();
		
		ictclasType.append(flag ? "/win" : "/linux");
		ictclasType.append(Platform.is64Bit() ? "64" : "32");
		ictclasType.append(flag ? "/NLPIR.dll" : "/libNLPIR.so");
		
		return ictclasType.toString();
	}
	
	/**
	 * 获取quote.properties配置文件中的内容.
	 * @param key 配置文件中的键
	 * */
	private static String getPropertiesValue(String key)
	{
		String result = null;
		final String propertiesFilePath =  "/properties/quote.properties";
		final String basePath = System.getProperties().getProperty("user.dir");
		
		try
		{
			FileReader reader = new FileReader(new File(basePath + propertiesFilePath));
			Properties properties = new Properties();
			
			properties.load(reader);
			result = properties.getProperty(key);
			
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			System.err.println("Can't find properties file.");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.err.println("Can't read properties file.");
			e.printStackTrace();
		}
		
		return result;
	}

}
