/*
package file;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


public class FileManage {
	static Set <String>myfile=new HashSet<String>();
	static int error;
	//error1表示文件不存在 error2表示文件已重复
	public static boolean addFile(String s)
	{
		File f1=new File(s);
		if(!f1.exists())
		{
			error=1;
			return false;
		}
		if(!myfile.add(s))
		{
			error=2;
			return false;
		}
		return true;
	}
	public static void main()
	{
		Scan scanner=new Scan();
		Link.fileManage();
		scanner.scan();
	}
}

*/