package file;

import java.io.File;

public class Scan {
	String fileroot="D:\\XueBaResources";
	File root=new File(fileroot);
	public void scan()
	{
		String s;
		File[] files=root.listFiles();
		for(File file:files){
			if(file.isDirectory())
			{
				scan(file);
			}
			else
			{
				if(FileManage.addFile(file.getAbsolutePath()))
				{
					s=file.getAbsolutePath()+"�����ݿ����޼�¼\n���޸�\n";
				}
			}
		}
	}
	public void scan(File f1)
	{
		String s;
		File[] files=f1.listFiles();
		for(File file:files){
			if(file.isDirectory())
			{
				scan(file);
			}
			else
			{
				if(FileManage.addFile(file.getAbsolutePath()))
				{
					s=file.getAbsolutePath()+"�����ݿ����޼�¼\n���޸�\n";
				}
			}
		}
	}
}
