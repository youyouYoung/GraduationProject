/**
 * 调用中科院的ICTCLAS工具实现分词
 * */
package topic.participle;

import java.io.UnsupportedEncodingException;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class ICTCLAS
{
	
	public interface CLibrary extends Library
	{
		//绑定分词工具
		CLibrary instance = (CLibrary) Native.loadLibrary(ChoseICTCLAS.getICTCLAS(), CLibrary.class);//
		
		/**
		 * 
		 * 初始化函数
		 * @param sDataPath
		 * @param encoding
		 * @param sLicenceCode
		 * 
		 * */
		public int NLPIR_Init(String sDataPath, int encoding, String sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,	boolean bWeightOut);

		public void NLPIR_Exit();
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		//System.out.println(ChoseICTCLAS.getICTCLAS());
		
		int init_flag = ICTCLAS.CLibrary.instance.NLPIR_Init("", 1, "0");
		
		if (0 == init_flag)
		{
			System.err.println("初始化失败!");
			return;
		}
		
		String sInput = "据悉，质检总局已将最新有关情况再次通报美方，要求美方加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";

		String nativeBytes = null;
		nativeBytes = CLibrary.instance.NLPIR_ParagraphProcess(sInput, 3);
		System.out.println("分词结果为： " + nativeBytes);
		CLibrary.instance.NLPIR_Exit();
		
	}
}
