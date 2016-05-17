import static org.junit.Assert.*;

import java.awt.event.ActionEvent;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 */

/**
 * @author newbe
 *
 */
public class MyCrawlerTest {
	static MyCrawler myCrawler;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myCrawler = new MyCrawler();
		MyCrawler.main(null);
		Thread.sleep(5000);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	
	/**
	 * Test method for {@link MyCrawler#pdfCrawling(java.lang.String[])}.
	 */
	
	
	/**
	 * Test method for {@link MyCrawler#pdfCrawling(java.lang.String[])}.
	 */
	
	/**
	 * Test method for {@link MyCrawler#pptCrawling(java.lang.String[])}.
	 */
	
	
	
	/**
	 * Test method for {@link MyCrawler#docCrawling(java.lang.String[])}.
	 */
	
	/**
	 * Test method for {@link MyCrawler#STCrawling(java.lang.String[])}.
	 */
	@Test
	public void testSTCrawling() {
		MyCrawler.succeed = 0;
		MyCrawler.visited = 0;
		MyCrawler.Cr.total=1;
		final String [] a = new String[170000];
		for (int i = 0 ; i < 170000 ; i++)
			a[i] = "http://stackoverflow.com/questions?page="+String.valueOf(i+1)+"&sort=votes";
		final int Old = ConnectServer.idNumber();
		MyCrawler.Cr.progress.setMaximum(1);
		MyCrawler.Cr.isStart=true;
		MyCrawler.Cr.startBut.setText("Quiz Pause");
				System.out.println("Look here!It's the time to start!");
				myCrawler.STCrawling(a);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Assert.assertTrue(Old!=ConnectServer.idNumber());
				System.out.println("finish");
	}

	/**
	 * Test method for {@link MyCrawler#CNCrawling(java.lang.String[])}.
	 */
	/*
	@Test
	public void testCNCrawling() {
		myCrawler.succeed = 0;
		myCrawler.visited = 0;
		MyCrawler.Cr.total=1;
		final String [] cnblog = new String[200];
		for (int i = 0 ; i < 200 ; i++)
			cnblog[i] = "http://q.cnblogs.com/list/solved?page=" + String.valueOf(i+1);
		final int Old = ConnectServer.idNumber();
		MyCrawler.Cr.progress.setMaximum(1);
		MyCrawler.Cr.isStart=true;
		MyCrawler.Cr.startBut.setText("Quiz Pause");
				System.out.println("Look here!It's the time to start!");

				myCrawler.STCrawling(cnblog);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Assert.assertTrue(Old!=ConnectServer.idNumber());
				System.out.println("finish");
	}
	*/
	/**
	 * Test method for {@link MyCrawler#DWCrawling(java.lang.String[])}.
	 */
	/*
	@Test
	public void testDWCrawling() {
		MyCrawler.succeed = 0;
		MyCrawler.visited = 0;
		MyCrawler.Cr.total=1;
		final String [] dewen = new String[1000];
		for (int i = 0 ; i < 1000 ; i++)
			dewen[i] = "http://www.dewen.io/questions?page=" + String.valueOf(i+1);
		final int Old = ConnectServer.idNumber();
		MyCrawler.Cr.progress.setMaximum(1);
		MyCrawler.Cr.isStart=true;
		MyCrawler.Cr.startBut.setText("Quiz Pause");
				System.out.println("Look here!It's the time to start!");
				myCrawler.WWCrawling(dewen);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Assert.assertTrue(Old!=ConnectServer.idNumber());
				System.out.println("finish");
	}
	*/
	/**
	 * Test method for {@link MyCrawler#BZCrawling()}.
	 */
	@Test
	public void testBZCrawling() {
		MyCrawler.succeed = 0;
		MyCrawler.visited = 0;
		MyCrawler.Cr.total=1;
		final int Old = ConnectServer.idNumber();
		MyCrawler.Cr.progress.setMaximum(1);
		MyCrawler.Cr.isStart=true;
		MyCrawler.Cr.startBut.setText("Quiz Pause");
				System.out.println("Look here!It's the time to start!");
				myCrawler.BZCrawling();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Assert.assertTrue(Old!=ConnectServer.idNumber());
				System.out.println("finish");
	}

	/**
	 * Test method for {@link MyCrawler#WWCrawling(java.lang.String[])}.
	 */
	@Test
	public void testWWCrawling() {
		MyCrawler.succeed = 0;
		MyCrawler.visited = 0;
		MyCrawler.Cr.total=1;
		final String [] wenwen = new String[1000];
		for (int i = 0 ; i < 1000 ; i++)
			wenwen[i] = "http://wenwen.sogou.com/cate/?cid=0&tp=6&pg="+String.valueOf(i);
		final int Old = ConnectServer.idNumber();
		MyCrawler.Cr.progress.setMaximum(MyCrawler.Cr.total);
		MyCrawler.Cr.isStart=true;
		MyCrawler.Cr.startBut.setText("Quiz Pause");

				System.out.println("Look here!It's the time to start!");
				myCrawler.WWCrawling(wenwen);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Assert.assertTrue(Old!=ConnectServer.idNumber());
				System.out.println("finish");
				
	}

	/**
	 * Test method for {@link MyCrawler#QuizCrawling(java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[])}.
	 */
	@Test
	public void testQuizCrawling() {
		MyCrawler.succeed = 0;
		MyCrawler.visited = 0;
		MyCrawler.Cr.total=5;
		
		final String [] a = new String[170000];
		final String [] cnblog = new String[200];
		final String [] dewen = new String[1000];
		final String [] wenwen = new String[1000];
		final int Old = ConnectServer.idNumber();
		for (int i = 0 ; i < 18000 ; i++)
			a[i] = "http://stackoverflow.com/questions?page="+String.valueOf(i+1)+"&sort=votes";
		for (int i = 0 ; i < 200 ; i++)
			cnblog[i] = "http://q.cnblogs.com/list/solved?page=" + String.valueOf(i+1);
		for (int i = 0 ; i < 1000 ; i++)
			dewen[i] = "http://www.dewen.io/questions?page=" + String.valueOf(i+1);
		for (int i = 0 ; i < 1000 ; i++)
			wenwen[i] = "http://wenwen.sogou.com/cate/?cid=0&tp=6&pg="+String.valueOf(i);
		
		// 1000是否够用？存疑
		MyCrawler.Cr.progress.setMaximum(5);
		MyCrawler.Cr.isStart=true;
		MyCrawler.Cr.startBut.setText("Quiz Pause");
				System.out.println("Look here!It's the time to start!");
				myCrawler.QuizCrawling(a, cnblog, dewen,wenwen);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Assert.assertTrue(Old!=ConnectServer.idNumber());
				System.out.println("finish");
	}

	/**
	 * Test method for {@link MyCrawler#crawling(java.lang.String[])}.
	 */
	@Test
	public void testCrawling() {
		MyCrawler.succeed = 0;
		MyCrawler.visited =0;
		MyCrawler.Cr.total=2;
		final int Old = ConnectServer.idNumber();
		final String [] a=new String[1000];
		a[0] = "http://www.cnblogs.com/newbe";
		MyCrawler.Cr.progress.setMaximum(2);
		MyCrawler.Cr.isStart=true;
		MyCrawler.Cr.startBut.setText("Quiz Pause");
				System.out.println("Look here!It's the time to start!");
				myCrawler.crawling(a);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Assert.assertTrue(Old!=ConnectServer.idNumber());
				System.out.println("finish");
		Object source = myCrawler.Cr.analyzeBut;
		ActionEvent e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Test method for {@link MyCrawler#setKeyword(java.lang.String)}.
	 */
	@Test
	public void testSetKeyword() {
		myCrawler.setKeyword("test");
		Assert.assertEquals(MyCrawler.keyword,"test");
	}
	
	/**
	 * Test method for {@link Analysis#Analysis()}.
	 */
	@Test
	public void testAnalysis() {
		Analysis a=new Analysis();
		a.setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for {@link CraUi#actionPerformed(java.awt.event.ActionEvent)}.
	 */
	@Test
	public void testActionPerformed() {
		Object source = myCrawler.Cr.selectBut;
		ActionEvent e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		source = myCrawler.Cr.startBut;
		e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		myCrawler.Cr.num.setText("0");
		myCrawler.Cr.actionPerformed(e);
		myCrawler.Cr.num.setText("1");
		myCrawler.Cr.actionPerformed(e);
		myCrawler.Cr.isFileInput = true;
		myCrawler.Cr.setTxt("C:\\Users\\newbe\\Desktop\\12.txt");
		myCrawler.Cr.actionPerformed(e);
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		myCrawler.Cr.num.setText("");
		myCrawler.Cr.actionPerformed(e);
		
		source = myCrawler.Cr.openBut;
		e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		source = myCrawler.Cr.txtBut;
		e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		source = myCrawler.Cr.howBut;
		e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * Test method for {@link CraUi#actionPerformed(java.awt.event.ActionEvent)}.
	 */
	@Test
	public void testActionPerformed_pdf() {
		myCrawler.Cr.isFileInput = false;
		myCrawler.Cr.getUrlSeed.setText("http://www.cnblogs.com/conmajia/archive/2012/05/24/quick-csharp.html");
		Object source = myCrawler.Cr.pdfBut;
		ActionEvent e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * Test method for {@link CraUi#actionPerformed(java.awt.event.ActionEvent)}.
	 */
	@Test
	public void testActionPerformed_ppt() {
		myCrawler.Cr.isFileInput = false;
		myCrawler.Cr.getUrlSeed.setText("http://saraswat.org");
		Object source = myCrawler.Cr.pptBut;
		ActionEvent e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * Test method for {@link CraUi#actionPerformed(java.awt.event.ActionEvent)}.
	 */
	@Test
	public void testActionPerformed_doc() {
		myCrawler.Cr.isFileInput = false;
		myCrawler.Cr.getUrlSeed.setText("http://www.seas.harvard.edu/climate/eli/Downloads/Documents");
		Object source = myCrawler.Cr.docBut;
		ActionEvent e = new ActionEvent(source, 0, null);
		myCrawler.Cr.actionPerformed(e);
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Test method for {@link Keyword#getText(java.lang.String)}.
	 */
	@Test
	public void testGetText() {
		Keyword kw = new Keyword("c++");
		String a = kw.getText("http://www.baidu.com");
	}

	/**
	 * Test method for {@link Keyword#accept(java.lang.String)}.
	 */
	@Test
	public void testAccept_false() {
		Keyword kw = new Keyword("c++");
		Assert.assertEquals(false, kw.accept("http://www.baidu.com"));
	}
	@Test
	public void testAccept_true() {
		Keyword kw = new Keyword("新闻");
		Assert.assertEquals(true, kw.accept("http://www.baidu.com"));
	}

}
