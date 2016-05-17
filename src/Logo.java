import javax.swing.*;
import java.lang.Runnable;
import java.awt.*;
/**
* description: Logoչʾ
* note: 
* modificationDate: 2014-12-4
*/ 
public class Logo extends JWindow implements Runnable {
	String filename;
	/**
	 * ���캯��,����Logo����
	 * @return 
	 * @param 
	 * @throws 
	 */
	public Logo(String name) {
		filename = name;
	}
	/**
	 * ��ʾLogo
	 * @return 
	 * @param 
	 * @throws 
	 */
	public void run()
	{
		ImageIcon ig = new ImageIcon(filename); 
		JButton btn = new JButton(ig); //��ͼƬ��JButton��ʾ
		getContentPane().add(btn);  //����ʾͼƬ��btn�ӵ�JPanel��
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize(); //�����Ļ�Ĵ�С
		setLocation(screenSize.width/2 - 120,screenSize.height/2 - 120);//��Logo������ʾ����Ļ���1/4���ߵ�1/4��
		setSize(ig.getIconWidth(), ig.getIconHeight()); //��Logo���ڴ�С���ͼ��Ĵ�С
		toFront();      //��Logo������ʾΪ��ǰ��Ĵ���
		setVisible(true);  //��ʾ�ô���
	}
	/**
	 * ʹLogo��ʧ
	 * @return 
	 * @param 
	 * @throws 
	 */
	public void setNotVisible()
	{
		setVisible(false); //����ʾ�ô���
	}
}
