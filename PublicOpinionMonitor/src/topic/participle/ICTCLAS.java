package topic.participle;

import java.util.concurrent.Callable;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class ICTCLAS implements Callable<String>
{
	interface CLibrary extends Library
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
		 * */
		public String NLPIR_GetKeyWords(String Line, int MaxKeyLimit,	boolean WeightOut);

		/** 在完成所有的使用后退出ICTCLAS系统. */
		public void NLPIR_Exit();
	}
	
	private final String orignData; //原始数据
	private final int part;			//数据划分结果的最大份额
	
	/**
	 * @param data 带分词的原始数据
	 * @param part 规定返回数据的最大长度.
	 * */
	public ICTCLAS(String data, int part)
	{
		this.orignData = data;
		this.part = part;
	}
	
	@Override
	/**
	 * 对传入的原始数据使用ictclas方式进行分词处理.
	 * @return 分词结果词与词之间使用#分隔, 如果分词失败返回null.
	 * */
	public String call()
	{
		int init_flag = CLibrary.Instance.NLPIR_Init(ICTCLASParameter.getDataLocation(), 1, "0");
		
		if (0 == init_flag)
		{
			System.err.println("初始化失败!");
			return null;
		}
		
		//String sInput = "本帖最后由流光流散于2016-03-25 10:07:04 编辑种子下载地址：http://bitpt.cn/download.php?tid=244543 种子速度： －－－－－－－－－－－－－－－－－－－修改---删除－－－－－－－－－－－－－－－－－－－ 专辑名：All My Demons Greeting Me As A Friend 表演者: Aurora 流派: 民谣 专辑类型: 专辑 介质: CD 发行时间: 2016-03-11 出版者: Glassnote 简介： 环顾欧美乐坛，近年各位年近三、四十的大龄女歌手还乐此不疲的穿着荧光糖果色的连衣裙唱少女心事，反观这个2013年末悄悄走入人们视线的清冽的年轻嗓音却总爱诠释一些充满民族感冰冷的寓意型歌曲。疏离也亲近，给听众美的启发和灵感。 一如名字aurora一样，她充满了虚幻的神秘感。这个不到20岁的萝莉一头银发，带有天使一般的面庞，几分童稚又略带女性韵味。 如同卑尔根那些纯净峡湾的浅蓝瞳色。一张张可以做屏保的专辑封面，时而冷漠凝视，时而在丛林中浅露侧脸。 这次更脱俗的成了一只破茧而出的新蝶。 少年怀才，10岁开始写歌，13岁便开始了演出生涯。 如果说北欧indie风格如同放入耳道的一块寒冰 风格鲜明（如 Bang gang，sofia，Enya etc），那aurora的就相对更大气像香樟味的薄荷糖，怪咖内在的文艺青年；或者形容为冰雪极地里看到极光闪现后那种被美好所蛊惑的神清气爽。 尽管曦妹的粉丝们乐于把她的嗓音和作品称作充满灵性。灵怕是仅有几分，她的风格更像是经过纯化洗涤的旧时旋律。是一种从脊柱深处成长出的民族文化气息，融合着纳维亚半岛的清新和冰凉的民谣基础的ChamberPop，揉进现代轻快的电子旋律或是寡淡隽永的钢琴伴奏，好在统统都特别到让人心驰神往。 反复听 《噩梦如友造访 》这张专辑，最喜欢的是 runaway，black water lilies，winter bird。 以《runaway》作为开场，简单低沉渐进的节奏，如同困于人世空洞之中，反复吟唱加强的“take me home home i belong”让整体氛围趋于悲壮。 当耳朵还没从渐无的音浪中回过神。《conquer》的主旋律鼓点已经在耳边强势的响起，一改之前的迷惘，Ive been looking for the conquer，在废墟中拔身而起的精气神，即使生活只是无用的不停寻找疾驰跋涉，也是因此而更充实。“i feel alive” 《running with the wolves》副歌流畅的吟唱加强，让整首歌充满自由和活力。电子元素用的恰到好处。 《lucky》算是两首音浪轰炸之后的降火之作，虔诚的唱腔，感恩的歌词。毕竟，为人存在就是一种幸福。 《winter bird》鲜明的aurora的歌曲模式，亮眼的是有主转副歌的bridge衔接很悠长足够完美。大珠小珠落玉盘的质感。 《i went too far》里“give me some love”能把渴求唱的独立孤离的美女怕也真是少数了。编曲很流行的讨喜。 《though the eyes of child》说灵性这首当属前几，钢琴敲击的主旋律音符平抚人心，如同茫茫冰原上嗅到阳光的味道。 《warrior》明显的ELectronica+ folk，激愤恢宏。 《murder song》那段风琴实在悦耳，而歌词里叙述的故事却也着实囧怪。而高潮部分的嘶哑演绎更让歌曲富于戏剧化。可能相爱的人之间从未想过会伤害对方，但悲剧也来的毫无征兆。《home》民谣味浓郁，唱法充满层次感。 《under the water》副歌充满惊喜，穿梭于各种吟唱探问之后的情绪释放。 《black water lilies》假音加时而清晰时而虚幻的咬字，让思绪也如水流一般映出了那些藏在心底的故事。 《half the world away》英国百货的圣诞广告曲。很治愈也足够制郁，容易让人想起儿时眼前流过的灯火阑珊，那些爸妈牵着自己的手，嘴里哈出的热气模糊了商店的橱窗的日子。也容易借着歌词和温吞的音乐在适合团聚的日子里，在手机屏幕上映出自己的形单影只。发现就算什么都在改变，不变的是对温暖和被陪伴的渴望。 坦诚的讲，对于aurora，她或许嗓音并不大气特别，独特风格有些故作深沉。在独立歌手里也不足够格调鲜明。但哲理故事化的歌词，清晰独立的唱腔和虚幻神秘的演绎方式。让这道本该简单未脱童稚年纪的极光变得复杂多彩。对待这种与年龄形成反差式的少年新星，我们本该就应该付诸更多欣赏的眼光，而不是学院式的评判，毕竟我们需要的是她以后能更美好的长足发展和更像艺术品的作品。 曲目列表： 01 Runaway (04:09) 02 Conqueror (03:28) 03 Running With The Wolves (03:15) 04 Lucky (04:14) 05 Winter Bird (04:05) 06 I Went Too Far (03:28) 07 Through The Eyes Of A Child (04:35) 08 Warrior (03:44) 09 Murder Song (5, 4, 3, 2, 1) (03:21) 10 Home (03:33) 11 Under The Water (04:25) 12 Black Water Lilies (04:43) 13 Half The World Away (03:19) Deluxe Bonus 14 Murder Song (5, 4, 3, 2, 1) (Acoustic) (03:39) 15 Nature Boy (Acoustic) (03:00) 16 Wisdom Cries (04:08) 17 Running With The Wolves (Pablo Nouvelle Remix) (03:51)";

		String nativeBytes = null;
		//nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(orignData, 0);
		//System.out.println("分词结果为： " + nativeBytes);
		
		nativeBytes = CLibrary.Instance.NLPIR_GetKeyWords(orignData, part, false);
		//System.out.println("关键词为: "+ nativeBytes);
		CLibrary.Instance.NLPIR_Exit();
		
		return nativeBytes;
	}
}
