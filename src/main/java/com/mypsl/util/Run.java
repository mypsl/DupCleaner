package com.mypsl.util;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @author moyi
 * v1.0 �÷�
 *  DupCleaner -d �������� 
 *  DupCleaner + [ɨ��Ŀ¼]
 * v1.1 ����ļ�ʱ����ǰĿ¼��������trash+��ǰʱ��
 * v1.2 ����-u ȥ��ɨ�跽ʽ��ֻ�г��ļ�������ͬ���б�
 */

public class Run {
	public Run() {}
	
	public static void main(String args[])
	{
		DupFinder finder  = new DupFinder();
		
		// -d �����������������ļ��嵥
		if (args.length == 2 && args[0].equals("-d")) {
			FileCleaner fc = new FileCleaner();
			try {
				fc.deadList(args[1]);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			return;
		}else if (args.length == 2 && args[0].equals("-u")){
			// -u ������ɨ��󰴹���ȥ��
			finder.dirPath = args[1];
			finder.uniMode = true;
		}
		else if (args.length == 1){ // ɨ�����ָ��Ŀ¼
			finder.dirPath = args[0];
		} else { // δָ��Ŀ¼ȱʡɨ�赱ǰĿ¼
			File curDir = new File(".");
			try {
				finder.dirPath = curDir.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("---#" + finder.dirPath + "#---");
		finder.loadFilesInfo(finder.dirPath);
		finder.dumpDuplicateFilesSort();
	}	

}