import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;


public class MyT {
	static Set<String> type=new HashSet<String>();
	private static String showtype = "";
	public static String getshowtype(){
			showtype = "";
			for(String tmp :type){
				showtype = showtype +tmp +"\n";
			}
			return showtype;
		}
	public static void primaryType()
	{
		type.add("�ٶ�");
		type.add("����");
		type.add("�Ѻ�");
		type.add("��Ѷ");
	}
	public static void addType(String s)
	{
		if(type.add(s)){
			JOptionPane.showMessageDialog(null, "�ɹ���ӹؼ���");
			getshowtype();
		}	
		else
			JOptionPane.showMessageDialog(null, s+"�ؼ����Ѵ���");
	}
	public static void deleteType(String s)
	{
		for(String t:type)
		{
			if(t.equals(s))
				{
					type.remove(t);
					JOptionPane.showMessageDialog(null, "�ɹ��Ƴ��ؼ���");
					getshowtype();
					return;
				}
		}
		JOptionPane.showMessageDialog(null,s+"������ѡ�ؼ���");
	}
}
