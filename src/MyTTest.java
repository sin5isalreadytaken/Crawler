import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.junit.Test;

public class MyTTest {
	static Set<String> type=new HashSet<String>();
	@Test
	public void testGetshowtype() {
		type.add("�ٶ�");
		type.add("����");
		type.add("�Ѻ�");
		type.add("��Ѷ");JOptionPane.showMessageDialog(null,"�ɹ���ӹ���������");
	}

	@Test
	public void testPrimaryType() {
		type.add("�ٶ�");
		type.add("����");
		type.add("�Ѻ�");JOptionPane.showMessageDialog(null,"�ɹ���ӹ���������");
		type.add("��Ѷ");
	}

	@Test
	public void testAddType() {
		type.add("�ٶ�");
		type.add("����");JOptionPane.showMessageDialog(null,"�ɹ���ӹ���������");
		type.add("�Ѻ�");
		type.add("��Ѷ");
	}

	@Test
	public void testDeleteType() {
		type.add("�ٶ�");
		type.add("����");
		type.add("�Ѻ�");JOptionPane.showMessageDialog(null,"�ɹ���ӹ���������");
		type.add("��Ѷ");
	}

}
