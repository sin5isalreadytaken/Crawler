import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.junit.Test;

public class MyTTest {
	static Set<String> type=new HashSet<String>();
	@Test
	public void testGetshowtype() {
		type.add("百度");
		type.add("新浪");
		type.add("搜狐");
		type.add("腾讯");JOptionPane.showMessageDialog(null,"成功添加过滤器规则！");
	}

	@Test
	public void testPrimaryType() {
		type.add("百度");
		type.add("新浪");
		type.add("搜狐");JOptionPane.showMessageDialog(null,"成功添加过滤器规则！");
		type.add("腾讯");
	}

	@Test
	public void testAddType() {
		type.add("百度");
		type.add("新浪");JOptionPane.showMessageDialog(null,"成功添加过滤器规则！");
		type.add("搜狐");
		type.add("腾讯");
	}

	@Test
	public void testDeleteType() {
		type.add("百度");
		type.add("新浪");
		type.add("搜狐");JOptionPane.showMessageDialog(null,"成功添加过滤器规则！");
		type.add("腾讯");
	}

}
