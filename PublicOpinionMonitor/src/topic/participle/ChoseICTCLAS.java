/**
 * 根据调用者的
 * */
package topic.participle;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import com.sun.jna.Platform;

public class ChoseICTCLAS
{
	public static String getICTCLAS()
	{
		//当前文件到项目根目录的相对路径
		String toBasePath = System.getProperties().get("user.dir").toString();
		//配置文件的位置
		String propertiesFile = toBasePath + "/properties/quote.properties";
		//System.out.println(propertiesFile);
		//ICTCLAS文件位置
		StringBuffer path = new StringBuffer(toBasePath);
		
		/**
		 * 
		 * 从配置文件中查询ICTCLAS在lib目录中的位置,将其加入path中.
		 * 该值对应的key为:
		 * 		topic_ictclas_path
		 * 
		 * */
		Properties properties = new Properties();
		try
		{
			Reader in = new FileReader(propertiesFile);
			properties.load(in);
			String ictclasPath = properties.getProperty("topic_ictclas_path");
			//System.out.println(ictclasPath);
			path.append(ictclasPath);
			in.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			System.err.println("Can't find the properties file on: "+propertiesFile);
			e.printStackTrace();
			System.exit(0);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.err.println("Error to close InputStream.");
			e.printStackTrace();
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
		//标记当前系统类型
		boolean flag = Platform.isWindows();
		path.append(flag ? "/win" : "/linux");
		path.append(Platform.is64Bit() ? "64" : "32");
		path.append(flag ? "/NLPIR.dll" : "/libNLPIR.so");
		
		return path.toString();
	}

}
