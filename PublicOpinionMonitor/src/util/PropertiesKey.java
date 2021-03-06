/**
 * 所有property文件中的key.
 * */
package util;

public class PropertiesKey
{
	//in database.properties
	public static final String DB_DBSERVER = "db_server";
	public static final String DB_DBNAME = "db_name";
	public static final String DB_DBUSER = "db_user";
	public static final String DB_DBPASSWD = "db_pwd";
	
	//in jsonKey.properties
	public static final String JSON_JISUURL = "jisu_url";
	public static final String JSON_JISUTITLE = "jisu_title";
	public static final String JSON_JISUAUTHORS = "jisu_authors";
	public static final String JSON_JISUPOSTS = "jisu_posts";
	
	//in quote.properties
	public static final String QUOTE_ICTPATH = "topic_ictclas_path";
	public static final String QUOTE_ICTNAME = "topic_ictclas_name";
	public static final String QUOTE_ICTDATAPATH = "topic_ictclas_data_path";
	public static final String QUOTE_LISTLOCATION = "stoplist_location";
	
	//in systemParamter.properties
	public static final String SYSPAR_CRAWLERTHREAD = "crawler_threadcount";
	public static final String SYSPAR_KEYCOUNT = "ict_keywordscount";
	public static final String SYSPAR_THEMECOUNT = "lda_themecount";
	public static final String SYSPAR_WEEKNUMBER = "analysis_weeknumber";
	public static final String SYSPAR_DATALIMIT = "data_limit";
	public static final String SYSPAR_CONTRIBUTION = "min_contributionrate";
	public static final String SYSPAR_THEMEPAGE = "webpage_detail";
	public static final String SYSPAR_WEEKPAGE = "webpage_week";
	public static final String SYSPAR_LISTPAGE = "webpage_list";
	public static final String SYSPAR_WEBPAGELOCATION = "webpage_location";
}
