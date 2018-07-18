package com.mypsl.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class FileCleaner {

	// �ڵ�ǰ·���´���trash+��ǰʱ��Ŀ¼
	public String mkTrashDir() {
		String fs = System.getProperties().getProperty("file.separator");
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String t = new StringBuilder(".").append(fs).append("trash").append(fs).append(df.format(new Date())).append(fs).toString();
		File trash = new File(t);
		if (!trash.exists()) {
			if (!trash.mkdirs()) {
				System.out.println(t + " mkdir faild");
				return "";
			}				
		}
				
		return t;
	}
	
	// ��·�����ļ��ƶ���trashPath
	public void trashFile(String trashPath, String filePath)  throws SecurityException {

		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println(filePath + " is not exists!");
			return ;
		}

		if (file.isDirectory()) {
			System.out.println(filePath + " is a directory!");
			return ;
		}

		File afile = new File(filePath);
		String t = new StringBuilder(trashPath).append(afile.getName()).append(".bak.").append(System.currentTimeMillis()).toString();
		if (afile.renameTo(new File(t))) {
			System.out.println(filePath + "... is moved to trash successful!");
		} else {
			System.out.println(filePath + "... is failed to move!");
		}
	}
	
	// ��·�����ļ�ɾ��
	public void cleanFile(String filePath) throws SecurityException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println(filePath + " is not exists!");
			return ;
		}

		if (file.isDirectory()) {
			System.out.println(filePath + " is a directory!");
			return ;
		}

		if (file.delete()) {
			System.out.println(filePath + "... is killed!");
		} else {
			System.out.println(filePath + "... is failed to delete!");
		}
	}
	
	// ��fileName�ļ������嵥��*�ļ�ɾ�� 
	public void deadList(String fileName) 
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		String line;
		String trashPath = mkTrashDir();	
		System.out.println("Trash Path:"+trashPath);
		
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.length() == 0)
				continue;

			if (line.startsWith("*")) {
				StringTokenizer st = new StringTokenizer(line, "#");
				String url = st.nextToken();
//				cleanFile(url.substring(1));
				trashFile(trashPath, url.substring(1));
			}
		}
	}
}
