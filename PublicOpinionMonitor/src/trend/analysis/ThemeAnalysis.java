/**
 * 该类用于分析输入文本的主题, 获取文本特征值.
 * 该类用于分析舆情趋势, 也可以得到舆情数据的特征值. 包括:
 * 	1. 某个帖子的舆情信息
 * 	2. 某段时间的舆情信息
 * 	3. 统计某个人的舆情信息
 * 
 * 使用方法:
 * I 创建一个对象, 指定将会输入的带分词数据的个数.
 * I 调用call(), 多线程执行
 * I 循环将需要分词的数据使用addOrignData()方法放入对象中.
 * I 调用Future.isDone()判断是否完成主题分析
 * I 如果完成可获取执行结果:一个ThemeAnalysis.Result对象. 该对象中包括了主题分析结果和特征选择结果.
 * */

package trend.analysis;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import topic.lda.Corpus;
import topic.lda.LdaGibbsSampler;
import topic.lda.LdaUtil;
import topic.participle.CLibrary;
import util.PropertiesKey;
import util.PropertiesReader;

public class ThemeAnalysis implements Callable<ThemeAnalysis.Result>
{	
	//保存传入的原始数据
	private final LinkedBlockingQueue<String> orignDocumentsData = new LinkedBlockingQueue<String>();
	//分词后的关键词集合
	private final LinkedBlockingQueue<String> keywordsQueue = new LinkedBlockingQueue<String>();
	//保存分词结果
	private final BlockingQueue<String> participleResult = new LinkedBlockingQueue<String>();
	//主题数组
	private Map<String, Double>[] themeMaps = null;
	//完成分词信号
	private CountDownLatch doneSignal = null;
	
	/**
	 * @param count 原始数据的行数
	 * */
	public ThemeAnalysis(int count)
	{
		doneSignal = new CountDownLatch(count);
	}
	
	@Override
	public ThemeAnalysis.Result call()
	{
		while (true)
		{
			try
			{
				String orignString = orignDocumentsData.take();
				if ("ORIGNDATA_STOP".equals(orignString))
				{
					break;
				}
				new Thread(new Worker(orignString)).start();
				keywordsQueue.put(orignString);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			doneSignal.await();
			new Thread(new Runnable()
			{
				StringBuffer data = new StringBuffer();
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					String line = null;
					while ((line = keywordsQueue.poll()) != null)
					{
						data.append(line);
					}

					documentKeywords(data.toString());
				}
			}).start();
			themeMaps = getThemes(participleResult);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ThemeAnalysis.Result();
	}
	
	/**
	 * 获取分词结果的线程.
	 * */
	private class Worker implements Runnable
	{
		private final String data;
		public Worker(String string)
		{
			this.data = string;
		}
		
		public void run()
		{
			try
			{
				participleResult.put(documentParticiple(data));
				doneSignal.countDown();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
		
	/**
	 * 添加需要获取主题的原始数据.
	 * 我们接收一个"ORIGNDATA_STOP"字符串来表示不再向其中添加数据.
	 * */
	public void addOrignData(String data)
	{
		if (data != null)
		{
			try
			{
				orignDocumentsData.put(data);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 对字符串分词
	 * */
	private String documentParticiple(String content)
	{		
		return CLibrary.Instance.NLPIR_ParagraphProcess(content, 0);
	}
	
	/**
	 * 获取字符串特征值.
	 * */
	private void documentKeywords(String string)
	{
		int keyWrodsCount = new Integer(PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_KEYCOUNT)).intValue();
		
		String[] keywords = CLibrary.Instance.NLPIR_GetKeyWords(string, keyWrodsCount, false).split("#");
		for (String keyword : keywords)
		{
			keywordsQueue.add(keyword);
		}
	}
	
	/**
	 * 获取文档集documents的主题.
	 * @param orignDocumentsData2 文档集
	 * @return 文档集主题数组
	 * 		 结果以Map[]保存数组每个元素为一个主题, Map中每个Map.Entry为该主题的关键词(key保存)以及关键词对主题的贡献率(values).
	 * */
	private Map<String, Double>[] getThemes(BlockingQueue<String> orignDocumentsData2)
	{
		Corpus corpus = Corpus.load(orignDocumentsData2);
		
		LdaGibbsSampler ldaGibbsSampler = new LdaGibbsSampler(corpus.getDocument(), corpus.getVocabularySize());
		
		ldaGibbsSampler.gibbs(new Integer(PropertiesReader.getSystemParamter(PropertiesKey.SYSPAR_THEMECOUNT)).intValue());
		
		double[][] phi = ldaGibbsSampler.getPhi();
		return LdaUtil.translate(phi, corpus.getVocabulary(), 10);
	}
	
	/**
	 * PublicOpinion类的call()返回值类型.
	 * 返回舆情分析的关键词和主题.
	 * */
	public class Result
	{
		/**
		 * 获取关键词
		 * */
		public BlockingQueue<String> getKeywords()
		{
			return keywordsQueue;
		}
		
		/**
		 * 获取主题
		 * */
		public Map<String, Double>[] getThemes()
		{
			return themeMaps;
		}
	}
}
