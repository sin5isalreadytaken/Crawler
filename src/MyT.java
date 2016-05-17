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
		type.add("百度");
		type.add("新浪");
		type.add("搜狐");
		type.add("腾讯");
	}
	public static void addType(String s)
	{
		if(type.add(s)){
			JOptionPane.showMessageDialog(null, "成功添加关键词");
			getshowtype();
		}	
		else
			JOptionPane.showMessageDialog(null, s+"关键词已存在");
	}
	public static void deleteType(String s)
	{
		for(String t:type)
		{
			if(t.equals(s))
				{
					type.remove(t);
					JOptionPane.showMessageDialog(null, "成功移除关键词");
					getshowtype();
					return;
				}
		}
		JOptionPane.showMessageDialog(null,s+"不是已选关键词");
	}
}
