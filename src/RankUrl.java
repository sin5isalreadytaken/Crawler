import java.util.HashSet;
import java.util.Set;

/**
* description: 记录某链接所有的出链，服务于pageRank算法
* note: 
* modificationDate: 2014-11-30
*/
public class RankUrl {
    String url;
    int UrlKind;//1:stackoverflow	2: cnblog
	Set<RankUrl> outUrls = new HashSet<RankUrl>();
	/**
	 * 构造函数
	 * @return 
	 * @param url(String)
	 * @throws 
	 */
	public RankUrl(String urlsString) {
		url = urlsString;
	}
	/**
	 * 构造函数
	 * @return 
	 * @param url(String),网站类别kind(int)
	 * @throws 
	 */
	public RankUrl(String urlsString,int kind) {
		url = urlsString;
		//System.out.println("url = " + url);
		UrlKind = kind;
	}
	/**
	 * 获取网址URL
	 * @return url(String) 
	 * @param 
	 * @throws 
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 得到网址排名集合
	 * @return RankUrl集合:outUrls(Set)
	 * @param 
	 * @throws 
	 */
	public Set<RankUrl> getOutUrls() {
		return outUrls;
	}
	/**
	 * 添加网址排名
	 * @return 
	 * @param url(RankUrl)
	 * @throws 
	 */
	public void addOutUrl(RankUrl url) {
		this.outUrls.add(url);
	}
	/**
	 * 得到网址排名集合的大小
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
