/**
 * 获取使用ICTCLAS所需的参数
 * */
package topic.participle;

import util.PropertiesKey;
import util.PropertiesReader;

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
		path.append(PropertiesReader.getQuoteInfo(PropertiesKey.QUOTE_ICTPATH));
		path.append(selectICTCLAS());
		
		return path.toString();
	}
	
	/**
	 * 获取ICTCLAS核心词库位置.
	 * */
	public static String getDataLocation()
	{
		String dataLocaion = PropertiesReader.getQuoteInfo(PropertiesKey.QUOTE_ICTDATAPATH);
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
	
}
