import java.util.HashSet;
import java.util.Set;

/**
* description: ��¼ĳ�������еĳ�����������pageRank�㷨
* note: 
* modificationDate: 2014-11-30
*/
public class RankUrl {
    String url;
    int UrlKind;//1:stackoverflow	2: cnblog
	Set<RankUrl> outUrls = new HashSet<RankUrl>();
	/**
	 * ���캯��
	 * @return 
	 * @param url(String)
	 * @throws 
	 */
	public RankUrl(String urlsString) {
		url = urlsString;
	}
	/**
	 * ���캯��
	 * @return 
	 * @param url(String),��վ���kind(int)
	 * @throws 
	 */
	public RankUrl(String urlsString,int kind) {
		url = urlsString;
		//System.out.println("url = " + url);
		UrlKind = kind;
	}
	/**
	 * ��ȡ��ַURL
	 * @return url(String) 
	 * @param 
	 * @throws 
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * �õ���ַ��������
	 * @return RankUrl����:outUrls(Set)
	 * @param 
	 * @throws 
	 */
	public Set<RankUrl> getOutUrls() {
		return outUrls;
	}
	/**
	 * �����ַ����
	 * @return 
	 * @param url(RankUrl)
	 * @throws 
	 */
	public void addOutUrl(RankUrl url) {
		this.outUrls.add(url);
	}
	/**
	 * �õ���ַ�������ϵĴ�С
	 * @return size
	 * @param 
	 * @throws 
	 */
	public int getOutUrlsSize() {
		return outUrls.size();
	}
	public void print() {
		System.out.println("url:"+url);
		for (RankUrl u:outUrls) {
			System.out.println(u.url);
		}
		System.out.println("*********");
	}

}
