import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public  class ConUi extends JFrame implements ActionListener, Runnable, ItemListener{

	public String idbname = null;
	public String iusername = null;
	public String ipassword = null;
	public String iip = null;
	JLabel dbname = new JLabel("数据库名称：");
	JLabel usname = new JLabel("用户名：");
	JLabel pword =  new JLabel("密码：");
	JLabel ip = new JLabel("服务器TCP/IP地址:");
	JTextField  getdbname=new JTextField ();
	JTextField  getusname=new JTextField ();
	JTextField  getpword=new JTextField ();
	JTextField  getip=new JTextField();
	JButton confirm = new JButton("确认");
	Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	CraUi tojump;
	
	class ConfirmAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			idbname = getdbname.getText();
			iusername = getusname.getText();
			ipassword = getpword.getText();
			iip = getip.getText();
			if(iip.equals("")||iip.equals(null)){
				JOptionPane.showMessageDialog(null, "Please enter your IP! ");
				return ;
			}
			if(idbname.equals("")||idbname.equals(null)){
				JOptionPane.showMessageDialog(null, "Please enter your DataBase Name!");
				return ;
			}
			if(iusername.equals(null)||iusername.equals("")){
				JOptionPane.showMessageDialog(null, "Please enter your username! ");
				return ;
			}
			if(ipassword.equals(null)||ipassword.equals("")){
				JOptionPane.showMessageDialog(null, "Please enter your password! ");
				return ;
			}
			//System.out.println(iconURL+" "+iusername+" "+ipassword);
			ConnectServer.setip(iip);
			ConnectServer.setdbname(idbname);
			ConnectServer.setusername(iusername);
			ConnectServer.setpassword(ipassword);
			ConnectServer.setconURL();
			setVisible(false);
			tojump.setVisible(true);
		}
	}
	public void settojump(CraUi Cr){
		this.tojump=Cr;
		tojump.setVisible(false);
	}
	public ConUi(){
		super("请连接数据库");
		this.setLayout(null);
		this.setSize(new Dimension(500, 500));
		this.setLocation(450, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		ip.setBounds(20,20,120,30);
		this.getContentPane().add(ip);
		dbname.setBounds(20,70,100,30);
		this.getContentPane().add(dbname);
		usname.setBounds(20,120,100,30);
		this.getContentPane().add(usname);
		pword.setBounds(20,170,100,30);
		this.getContentPane().add(pword);
		
		getip.setBounds(160, 20, 300, 30);
		this.getContentPane().add(getip);
		getdbname.setBounds(160, 70, 300, 30);
		this.getContentPane().add(getdbname);
		getusname.setBounds(160, 120, 300, 30);
		this.getContentPane().add(getusname);
		getpword.setBounds(160, 170, 300, 30);
		this.getContentPane().add(getpword);
		
		
		confirm.setBounds(200,250,80,30);
		this.getContentPane().add(confirm);
		confirm.addActionListener(new ConfirmAction());
		getip.setText("10.2.26.60");
		getdbname.setText("Crawler");
		getusname.setText("crawler");
		getpword.setText("aimashi2015");
	}
	public void setnotvisible(){
		setVisible(false);
	}
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}

