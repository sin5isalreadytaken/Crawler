import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//��ClassΪ������PageRank������������޸� edit by hxy
/**
* description: �������д����ʺ��ѷ��ʵ���ַ���ӣ���������PageRank����
* note: PageRank�㷨��Hash��Ľ��
* modificationDate: 2014-12-12
*/ 
public class LinkQueue {
	//�ѷ��ʵ� url ����
	private static Set<RankUrl> visitedUrl = new HashSet<RankUrl>();
	//�ѷ��ʵ� url ����(.html)
	private static Set<RankUrl> visitedHUrl = new HashSet<RankUrl>();
	//�����ʵ� url ����
	private static ConcurrentLinkedQueue<RankUrl> unVisitedUrl = new ConcurrentLinkedQueue<RankUrl>();
	//seed url����
	private static Queue<RankUrl> seedUrls = new LinkedList<RankUrl>();
	//tag ����
	private static Hashtable<String, String>  tagtext= new Hashtable<String, String>();
	//Url�÷ֱ�
	private static Hashtable<RankUrl, Double> urlsScoreTable = new Hashtable<RankUrl, Double>();
	//ǰʮ����
	private static ArrayList<String> topUrlsSort = new ArrayList<String>();
//	/**
//	 * ���RankUrl����
//	 * @return δ��������������(Queue<RankUrl>)
//	 * @param 
//	 * @throws 
//	 */
//	public static Queue<RankUrl> getUnVisitedUrl() {
//		return unVisitedUrl;
//	}
	/**
	 * ���tag��
	 * @return ����tag
	 * @param 
	 * @throws 
	 */
	public static Hashtable<String, String> getTagtext()
	{	
		return tagtext;
	}
	/**
	 * ���tag��
	 * @return ����tag
	 * @param 
	 * @throws 
	 */
    //��ӵ����ʹ���URL������
	@SuppressWarnings("static-access")
	public static void addVisitedUrl(RankUrl url) {
		MyCrawler m=new MyCrawler();
		m.Cr.UIinsertURLs(url.getUrl());
		visitedUrl.add(url);
		//urlsScoreTable.put(url,100.0);
	}
	/**
	 * �����������
	 * @return 
	 * @param url(RankUrl)
	 * @throws 
	 */
	public static void addSeedUrl(RankUrl url) {
		if (url.getUrl() != null)
			seedUrls.add(url);
	}
	/**
	 * δ���ʵ�URL������
	 * @return 
	 * @param 
	 * @throws 
	 */
	public static Object unVisitedUrlDeQueue() {
		return unVisitedUrl.poll();
	}
	/**
	 * ���δ���ʵ�RankUrl���
	 * @return 
	 * @param url(RankUrl),inUrl(RankUrl)
	 * @throws 
	 */

	// ��֤ÿ�� url ֻ������һ��
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
	 * ����Ѿ����ʵ�URL��Ŀ
	 * @return size(int)
	 * @param 
	 * @throws 
	 */
	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}
	/**
	 * �ж�δ���ʵ�URL�������Ƿ�Ϊ��
	 * @return �Ƿ�Ϊ��(boolean)
	 * @param 
	 * @throws 
	 */
	public static boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}
	/**
	 * ʹ��PageRank�㷨����Url����Ҫ������
	 * ÿ��url������ʼ����100
	 * ����ʼurl��ʼ��ÿ�ֵ����У�ÿ��url�����г������õ� ��url��ǰ��ֵ/�������� �ļӷ�
	 * ֱ����������
	 */
	public static void getUrlsScores() {
		// visitedNew�����UrlҪ��ScoreTableʼ�ձ���һ��
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
	        for(RankUrl url : visitedUrlsNew) { //�����µ�Url����
	        	for (RankUrl outUrl : url.getOutUrls()) { //����Url�ĳ���
	        		if (visitedHUrl.contains(outUrl)) { // ���������visitedUrl����
	        			waitUrls.add(outUrl); // ���Url��wait��һ�ε���������ͳһ��ӵ�visitNew
	        			double outScore = urlsScoreTable.get(url) / url.getOutUrlsSize(); //�õ���ÿ�������ķ�������
	        			// �����Table���Ѿ���ӵ����ӣ����¼ӷ�
	        			if (urlsScoreTable.containsKey(outUrl)) {
			        		 double tempScore = urlsScoreTable.get(outUrl);
			        		 tempScore += outScore;
			        		 urlsScoreTable.put(outUrl, tempScore);
			        	}
	        			//����ֱ����ӵ÷�
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
	 * �������top10���ڵ�Url�ͷ���
	 * @return ������(Hashtable)
	 * @param 
	 * @throws 
	 */
	public static Hashtable<String, Double> getTopRankUrls() {
		//�õ�Table�Ŀ���rankUrls
		Hashtable<RankUrl, Double> rankUrls = new Hashtable<RankUrl, Double>();
		Set<RankUrl> keys = urlsScoreTable.keySet(); 
        for(RankUrl key: keys){ 
            rankUrls.put(key, urlsScoreTable.get(key)); 
        } 
        Hashtable<String, Double> topRankUrls = new Hashtable<String, Double>();
		// ������������10���൱�����򣻷�����ǰʮ��
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
