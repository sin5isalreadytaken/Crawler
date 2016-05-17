import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

/**
* description: ͨ������ȡ���ۼ�����ȡ���ۺ�����ȡ���ʴ�ҳ��ȡ���ܵ����
* note: ����������ȡ�ٶ�
* modificationDate: 2014-12-19
*/ 

public class MyCrawler {
	public static int count=0;//��ȡ�߳���
	public static int ST = 0;//stackoverflow
	public static int CN = 0;//q.cnblogs
	public static int DW = 0;//dwen
	public static int BZ = 0;//zhidao.baidu
	public static int WW = 0;//wenwen
	public static CraUi Cr;//UI����
	public static int number=0;//�Ѿ�������ҳ����
	public static int id;//��ǰ��ҳid
	public static String keyword = "";//�ؼ���
	static ArrayList<String> filterStrings = new ArrayList<String>();
	private static String showfilterStrings = "";
	public static String getshowfilter()
	{
			showfilterStrings = "";
			for(String tmp :filterStrings){
				showfilterStrings = showfilterStrings +tmp +"\n";
			}
			return showfilterStrings;
	}
	public static void primaryfilter(){
		filterStrings.add("http:");
		filterStrings.add("https:");
	}
	public static void addfilter(String s){
		for(String tmp:filterStrings){
			if(tmp.equals(s)){
				JOptionPane.showMessageDialog(null, s+"�����������Ѵ���");
				return;
			}
		}
		filterStrings.add(s);
		JOptionPane.showMessageDialog(null,"�ɹ���ӹ���������");
		getshowfilter();
		
	}
	public static void deletefilter(String s){
		for(String tmp:filterStrings){
			if(tmp.equals(s)){
				filterStrings.remove(tmp);
				JOptionPane.showMessageDialog(null,"�ɹ�ɾ������������");
				getshowfilter();
				return ;
			}
		}
		JOptionPane.showMessageDialog(null,s+"�����Ѵ��ڵĹ���������");
	}

	
	public static int visited = 0;//�Ѿ�����
	public static int succeed = 0;//��ȡ�ɹ�
	public static int failed = 0;//��ȡʧ��
	public static int unpassed = 0;//���˸���
	public static long time;//�Ѿ�����ʱ��
	public static long startTime;//��ȡ��ʼʱ��
	//public static HistogramJPanel state = new HistogramJPanel();
	public static int max = 0;//�����ȡ��
	public Lock lock=new Lock();
	public static ExecutorService pool = Executors.newFixedThreadPool(50);
	
	/**
	 * ʹ�����ӳ�ʼ�� URL����
	 * @return
	 * @param seeds(String[])
	 * @throws 
	 */
	private void initCrawlerWithSeeds(String[] seeds)// edit by hxy
	{
		startTime = System.currentTimeMillis();
		for(int i=0;i<seeds.length;i++) {
			RankUrl url = new RankUrl(seeds[i],1);
			LinkQueue.addUnvisitedUrl(url, null);
			LinkQueue.addSeedUrl(url);
		}
			
		Keyword.keyword = keyword;
	}	
	/**
	 * ��ʼץȡstackoverflowҳ�����
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void STCrawling(String[] STseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			File file = new File(System.getProperty("user.dir")+"\\Point.txt");
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				System.out.println(line);
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			int first = 0;
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (ST == 0)
				{
					try {
						if (first != 0)	Thread.sleep(30000);
						first = 1;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (i1 < STseeds.length)
					{
						ST = 1;
						System.out.println(STseeds[i1++]);
						RankUrl visitUrl= new RankUrl(STseeds[i1++], 1);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
			}
			//System.out.println(String.valueOf(i1));
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ��ʼץȡq.cnblogsҳ�����
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void CNCrawling(String[] CNseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (CN == 0)
				{
					if (i2 < CNseeds.length)
					{
						CN = 1;
						RankUrl visitUrl= new RankUrl(CNseeds[i2++], 2);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ��ʼץȡdwenҳ�����
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void DWCrawling(String[] DWseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (DW == 0)
				{
					if (i3 < DWseeds.length)
					{
						DW = 1;
						RankUrl visitUrl= new RankUrl(DWseeds[i3++], 3);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ��ʼץȡzhidao.baiduҳ�����
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void BZCrawling()
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (BZ == 0)
				{
					BZ = 1;
					String BZseed = "http://zhidao.baidu.com/question/"+String.valueOf(i4++)+"/";
					RankUrl visitUrl= new RankUrl(BZseed, 4);
					DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
					//downLoader.start();
					pool.execute(downLoader);
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ��ʼץȡwenwenҳ�����
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void WWCrawling(String[] WWseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (WW == 0)
				{
					if (i5 < WWseeds.length)
					{
						WW = 1;
						RankUrl visitUrl= new RankUrl(WWseeds[i5++], 5);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ConnectServer.dbClose();
	}
	/**
	 * ��ʼץȡ�ʴ�ҳ�����
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void QuizCrawling(String[] STseeds, String[] CNseeds, String[] DWseeds, String[] WWseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();\
			int first = 0;
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (ST == 0)
				{
					try {
						if (first != 0)	Thread.sleep(30000);
						first = 1;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (i1 < STseeds.length)
					{
						ST = 1;
						RankUrl visitUrl= new RankUrl(STseeds[i1++], 1);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
				if (CN == 0)
				{
					if (i2 < CNseeds.length)
					{
						CN = 1;
						RankUrl visitUrl= new RankUrl(CNseeds[i2++], 2);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
				if (DW == 0)
				{
					if (i3 < DWseeds.length)
					{
						DW = 1;
						RankUrl visitUrl= new RankUrl(DWseeds[i3++], 3);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
				if (BZ == 0)
				{
					BZ = 1;
					String BZseed = "http://zhidao.baidu.com/question/"+String.valueOf(i4++)+"/";
					RankUrl visitUrl= new RankUrl(BZseed, 4);
					DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
					//downLoader.start();
					pool.execute(downLoader);
				}
				if (WW == 0)
				{
					if (i5 < WWseeds.length)
					{
						WW = 1;
						RankUrl visitUrl= new RankUrl(WWseeds[i5++], 5);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//ConnectServer.dbClose();
	}
	/**
	 * ��ʼץȡ����
	 * @return
	 * @param seeds
	 * @throws  
	 */
	public void crawling(String[] seeds)
	{   //�������������ȡ��http://www.lietu.com��ͷ������
		//state.pack();//�Ժ��ʵĴ�С��ʾ
        //state.setVisible(true);
		LinkFilter filter = new LinkFilter(){
		
			public boolean accept(String url) {
				int flag=0;
				for(int i = 0; i < filterStrings.size(); i++){
					if(url.startsWith(filterStrings.get(i))){
						flag=1;
						break;
					}					
				}				
				//	�ж��Ƿ�����Ϊɶ�����ж�β�ˣ� ����
				if(url.indexOf("jpg")!=-1||url.indexOf("gif")!=-1||url.indexOf("png")!=-1)
					flag=0;
				if(flag==0)
					return false;
				else
					return true;			
			}
		};
		//��ʼ�� URL ����
		initCrawlerWithSeeds(seeds);
		//ѭ����������ץȡ�����Ӳ�����ץȡ����ҳ������1000
		number=0;
		//�ܹ���Ҫ��ȡ����ҳ��
		max=Cr.total;
		while(succeed<max)
		{
			//	break������continue�������Ѳ����Ļ��޷�ֹͣ
			//	count>3ԭ�򣿴���
			//	������û��δ���ʵ�URL��ֹͣѭ��
			time = System.currentTimeMillis();
			if (Cr.startBut.getText().equals("Continue"))	continue;
			if(LinkQueue.unVisitedUrlsEmpty()||count>10)
			{
				//System.out.println("d");
				continue;
			}
			System.out.println(LinkQueue.getVisitedUrlNum());
			RankUrl visitUrl=(RankUrl)LinkQueue.unVisitedUrlDeQueue();
			DownLoadFile downLoader=new DownLoadFile(visitUrl,filter,lock);
			//	������ҳ
			//downLoader.start();
			pool.execute(downLoader);	
			//	��������ҳ��++
			number++;
			System.out.println("number:"+number);
		}
	}
	/**
	 * main�������
	 * @return
	 * @param args
	 * @throws InterruptedException,ClassNotFoundException,InstantiationException,IllegalAccessException,UnsupportedLookAndFeelException,
	 */
	public static void main(String[]args)
	{
		//final Logo lg =new Logo(System.getProperty("user.dir").replace('\\', '/') + "\\image\\icon1.jpg");
		final ConUi conui = new ConUi();
		//lg.run();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println(System.getProperty("user.dir"));
		try {  
	           javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");  
	       } catch (ClassNotFoundException e) {  
	           e.printStackTrace();  
	       } catch (InstantiationException e) {  
	           e.printStackTrace();  
	       } catch (IllegalAccessException e) {  
	           e.printStackTrace();  
	       } catch (javax.swing.UnsupportedLookAndFeelException e) {
	           e.printStackTrace();  
	       }
	       javax.swing.SwingUtilities.invokeLater(new Runnable() {  
	           public void run() {  
	        	   	//	��UI������߳�
	                Cr = new CraUi();     
	                Cr.setVisible(false);
	                conui.settojump(Cr);
	                Thread t1=new Thread(Cr);
	                t1.start(); 
	           }  
	       }); 
		id=ConnectServer.idNumber()+1;		
		//pool.shutdown();
	}
	/**
	 * ���ùؼ��ַ���
	 * @return
	 * @param keyword
	 * @throws 
	 */
	public void setKeyword(String _keyword) {
		// TODO Auto-generated method stub
		keyword = new String(_keyword);
	}
}


