import javax.swing.*;
import java.lang.Runnable;
import java.awt.*;
/**
* description: Logo展示
* note: 
* modificationDate: 2014-12-4
*/ 
public class Logo extends JWindow implements Runnable {
	String filename;
	/**
	 * 构造函数,设置Logo参数
	 * @return 
	 * @param 
	 * @throws 
	 */
	public Logo(String name) {
		filename = name;
	}
	/**
	 * 显示Logo
	 * @return 
	 * @param 
	 * @throws 
	 */
	public void run()
	{
		ImageIcon ig = new ImageIcon(filename); 
		JButton btn = new JButton(ig); //将图片给JButton显示
		getContentPane().add(btn);  //将显示图片的btn加到JPanel里
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize(); //获得屏幕的大小
		setLocation(screenSize.width/2 - 120,screenSize.height/2 - 120);//将Logo窗口显示在屏幕宽的1/4，高的1/4处
		setSize(ig.getIconWidth(), ig.getIconHeight()); //将Logo窗口大小设成图像的大小
		toFront();      //将Logo窗口显示为最前面的窗口
		setVisible(true);  //显示该窗口
	}
	/**
	 * 使Logo消失
	 * @return 
	 * @param 
	 * @throws 
	 */
	public void setNotVisible()
	{
		setVisible(false); //不显示该窗口
	}
}
