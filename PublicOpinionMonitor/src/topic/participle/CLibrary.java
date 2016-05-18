package topic.participle;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 描述ICTCLAS支撑库可用方法的接口
 * */
public interface CLibrary extends Library
{
	//该接口的实例
	public CLibrary Instance = (CLibrary) Native.loadLibrary(ICTCLASParameter.getICTCLAS(), CLibrary.class);
	
	/**
	 * 初始化函数
	 * @param DataPath Data目录所在的位置
	 * @param encoding 文件的编码格式
	 * @param LicenceCode
	 * */
	public int NLPIR_Init(String DataPath, int encoding, String LicenceCode);

	/**
	 * 对传入文本进行分析处理.
	 * @param Line 传入的文本.
	 * @param POSTTagged 对每组词语进行标记, 0为不显示.
	 * */
	public String NLPIR_ParagraphProcess(String Line, int POSTagged);

	/**
	 * 获取传入字符串的关键字.
	 * @param Line 需要获取关键字的字符串.
	 * @param MaxKayLimit 获取到的关键字的最大数量.
	 * @param WeightOut 是否显示该关键字的权值,true为显示.
	 * @return 关键字,每个关键字之间以#分割
	 * */
	public String NLPIR_GetKeyWords(String Line, int MaxKeyLimit,	boolean WeightOut);

	/** 在完成所有的使用后退出ICTCLAS系统. */
	public void NLPIR_Exit();
}
