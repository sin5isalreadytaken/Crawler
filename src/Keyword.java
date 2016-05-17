import org.htmlparser.beans.StringBean;
/**
* description: ���˷��Ϲ�����������ҳ
* note: �����ٶ��ȶ�
* modificationDate: 2014-11-29
*/ 
public class Keyword {
	public static String keyword = "";
	public Keyword(String _keyword)
	{
		keyword = _keyword;
	}
	/** 
     * �����ṩ��URL����ȡ��URL��Ӧ��ҳ�Ĵ��ı���Ϣ 
     * @return ��Ӧ��ҳ�Ĵ��ı���Ϣ Striing
     * @param ��ַ����url 
     * @throws ParserException 
     */  
    public static String getText(String url){  
        StringBean sb = new StringBean();  
          
        //���ò���Ҫ�õ�ҳ����������������Ϣ  
        sb.setLinks(false);  
        //���ý�����Ͽո�������ո������  
        sb.setReplaceNonBreakingSpaces(true);  
        //���ý�һ���пո���һ����һ�ո�������  
        sb.setCollapse(true);  
        //����Ҫ������URL  
        sb.setURL(url);  
        //���ؽ��������ҳ���ı���Ϣ  
        return sb.getStrings();  
    } 
    
    /** 
     * ����ָ��URL�Ƿ���Ϲ�������
     * @return �Ƿ���Ϲ�������(int)
     * @param ��ַ����url 
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
