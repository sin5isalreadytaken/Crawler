import org.htmlparser.beans.StringBean;
/**
* description: 过滤符合过滤条件的网页
* note: 过滤速度稳定
* modificationDate: 2014-11-29
*/ 
public class Keyword {
	public static String keyword = "";
	public Keyword(String _keyword)
	{
		keyword = _keyword;
	}
	/** 
     * 根据提供的URL，获取此URL对应网页的纯文本信息 
     * @return 对应网页的纯文本信息 Striing
     * @param 网址链接url 
     * @throws ParserException 
     */  
    public static String getText(String url){  
        StringBean sb = new StringBean();  
          
        //设置不需要得到页面所包含的链接信息  
        sb.setLinks(false);  
        //设置将不间断空格由正规空格所替代  
        sb.setReplaceNonBreakingSpaces(true);  
        //设置将一序列空格由一个单一空格所代替  
        sb.setCollapse(true);  
        //传入要解析的URL  
        sb.setURL(url);  
        //返回解析后的网页纯文本信息  
        return sb.getStrings();  
    } 
    
    /** 
     * 给出指定URL是否符合过滤条件
     * @return 是否符合过滤条件(int)
     * @param 网址链接url 
     * @throws  
     */
	public static boolean accept(String url)//	boolean usual
	{
		if(keyword.equals("")){
			return true;
		}
		if(url.indexOf(keyword) < 0 && getText(url).indexOf(keyword) < 0)
			return false;
		return true;
	}
}
