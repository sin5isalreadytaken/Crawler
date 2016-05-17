import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//此Class为服务于PageRank排序做了诸多修改 edit by hxy
/**
* description: 管理所有待访问和已访问的网址链接，并服务于PageRank排序
* note: PageRank算法与Hash表的结合
* modificationDate: 2014-12-12
*/ 
public class LinkQueue {
	//已访问的 url 集合
	private static Set<RankUrl> visitedUrl = new HashSet<RankUrl>();
	//已访问的 url 集合(.html)
	private static Set<RankUrl> visitedHUrl = new HashSet<RankUrl>();
	//待访问的 url 集合
	private static ConcurrentLinkedQueue<RankUrl> unVisitedUrl = new ConcurrentLinkedQueue<RankUrl>();
	//seed url集合
	private static Queue<RankUrl> seedUrls = new LinkedList<RankUrl>();
	//tag 集合
	private static Hashtable<String, String>  tagtext= new Hashtable<String, String>();
	//Url得分表
	private static Hashtable<RankUrl, Double> urlsScoreTable = new Hashtable<RankUrl, Double>();
	//前十链接
	private static ArrayList<String> topUrlsSort = new ArrayList<String>();
//	/**
//	 * 获得RankUrl队列
//	 * @return 未访问需排名链接(Queue<RankUrl>)
//	 * @param 
//	 * @throws 
//	 */
//	public static Queue<RankUrl> getUnVisitedUrl() {
//		return unVisitedUrl;
//	}
	/**
	 * 获得tag表
	 * @return 所有tag
	 * @param 
	 * @throws 
	 */
	public static Hashtable<String, String> getTagtext()
	{	
		return tagtext;
	}
	/**
	 * 获得tag表
	 * @return 所有tag
	 * @param 
	 * @throws 
	 */
    //添加到访问过的URL队列中
	@SuppressWarnings("static-access")
	public static void addVisitedUrl(RankUrl url) {
		MyCrawler m=new MyCrawler();
		m.Cr.UIinsertURLs(url.getUrl());
		visitedUrl.add(url);
		//urlsScoreTable.put(url,100.0);
	}
	/**
	 * 添加种子链接
	 * @return 
	 * @param url(RankUrl)
	 * @throws 
	 */
	public static void addSeedUrl(RankUrl url) {
		if (url.getUrl() != null)
			seedUrls.add(url);
	}
	/**
	 * 未访问的URL出队列
	 * @return 
	 * @param 
	 * @throws 
	 */
	public static Object unVisitedUrlDeQueue() {
		return unVisitedUrl.poll();
	}
	/**
	 * 添加未访问的RankUrl入队
	 * @return 
	 * @param url(RankUrl),inUrl(RankUrl)
	 * @throws 
	 */

	// 保证每个 url 只被访问一次
	public static void addUnvisitedUrl(RankUrl url, RankUrl inUrl) {
		
		if (url.getUrl() != null && !url.getUrl().trim().equals("")) {
			boolean isvisitedH = false;
			for (RankUrl vUrl : visitedHUrl) {
				if(vUrl.getUrl().equals(url.getUrl())) {
					isvisitedH = true;
					break;
				}
			}
			if (!isvisitedH)
				visitedHUrl.add(url); //add H
			if (inUrl != null)
				inUrl.addOutUrl(url);
			boolean flag = false;
			for (RankUrl vUrl : visitedUrl) 
				if(vUrl.getUrl().equals(url.getUrl())) {
					flag=true;
					break;
				}
			if(flag)
				return;
			for (RankUrl uUrl : unVisitedUrl)
				if(uUrl.getUrl().equals(url.getUrl())) {
					flag=true;
					break;
				}
			if(flag)
				return;
			unVisitedUrl.add(url);
		}
	}
	/**
	 * 获得已经访问的URL数目
	 * @return size(int)
	 * @param 
	 * @throws 
	 */
	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}
	/**
	 * 判断未访问的URL队列中是否为空
	 * @return 是否为空(boolean)
	 * @param 
	 * @throws 
	 */
	public static boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}
	/**
	 * 使用PageRank算法计算Url的重要性排序
	 * 每个url具有起始分数100
	 * 从起始url开始，每轮迭代中，每个url的所有出链都得到 【url当前分值/出链数】 的加分
	 * 直到搜索结束
	 */
	public static void getUrlsScores() {
		// visitedNew里面的Url要和ScoreTable始终保持一致
		Set<RankUrl> visitedUrlsNew = new HashSet<RankUrl>();
		Set<RankUrl> waitUrls = new HashSet<RankUrl>();
		for (RankUrl url : visitedHUrl) {
			if (url.getOutUrlsSize()!=0) {
				urlsScoreTable.put(url, 100.0);
				visitedUrlsNew.add(url);
				//url.print();
			}
		}
		
		while (true) {			
			//Set<RankUrl> keys = urlsScoreTableCopy.keySet(); 			
	        for(RankUrl url : visitedUrlsNew) { //遍历新的Url集合
	        	for (RankUrl outUrl : url.getOutUrls()) { //遍历Url的出链
	        		if (visitedHUrl.contains(outUrl)) { // 如果出链在visitedUrl里面
	        			waitUrls.add(outUrl); // 添加Url到wait，一次迭代结束后统一添加到visitNew
	        			double outScore = urlsScoreTable.get(url) / url.getOutUrlsSize(); //得到给每个出链的分数增量
	        			// 如果是Table中已经添加的链接，更新加分
	        			if (urlsScoreTable.containsKey(outUrl)) {
			        		 double tempScore = urlsScoreTable.get(outUrl);
			        		 tempScore += outScore;
			        		 urlsScoreTable.put(outUrl, tempScore);
			        	}
	        			//否则直接添加得分
			        	else {
			        		 urlsScoreTable.put(outUrl, 100.0 + outScore);
			        	}
	        		}	            		 
	            }
	        }
	        for (RankUrl url : waitUrls) {
	        	if (!visitedUrlsNew.contains(url))
	        		visitedUrlsNew.add(url);
	        }
	        waitUrls.clear();
	        if (visitedUrlsNew.size() >= visitedHUrl.size())
	        	break;
		}
	}
	/**
	 * 获得排序top10以内的Url和分数
	 * @return 分数表(Hashtable)
	 * @param 
	 * @throws 
	 */
	public static Hashtable<String, Double> getTopRankUrls() {
		//得到Table的拷贝rankUrls
		Hashtable<RankUrl, Double> rankUrls = new Hashtable<RankUrl, Double>();
		Set<RankUrl> keys = urlsScoreTable.keySet(); 
        for(RankUrl key: keys){ 
            rankUrls.put(key, urlsScoreTable.get(key)); 
        } 
        Hashtable<String, Double> topRankUrls = new Hashtable<String, Double>();
		// 总链接数少于10，相当于排序；否则，找前十名
		double maxScore = 0;
		int loopTimes = Math.min(rankUrls.size(), 10);
		for (int i=0; i<loopTimes ; i++) {
			RankUrl topUrl = null;
			Set<RankUrl> keys2 = rankUrls.keySet(); 
	        for(RankUrl url: keys2){ 
	        	if (rankUrls.get(url) > maxScore) {
					maxScore = rankUrls.get(url);
					topUrl = url;			
				}
	        }
	        System.out.println(topUrl.getUrl()+ "***"+ maxScore);
			topRankUrls.put(topUrl.getUrl(), maxScore);
			topUrlsSort.add(topUrl.url);
			maxScore = 0;			
			rankUrls.remove(topUrl);
		}
		return topRankUrls;
	}
	public static ArrayList<String> getTopUrlsSort() {
		return topUrlsSort;
	}
	

}
