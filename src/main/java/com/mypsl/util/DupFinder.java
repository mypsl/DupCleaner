package com.mypsl.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DupFinder
{
	public boolean uniMode = false;
	public String dirPath;
	public Map<String,ArrayList<FileInfo>> filesInfo = new HashMap<String,ArrayList<FileInfo>>();
	public DupFinder()	{
	}

	// ��·�����ļ���Ϣ����Map
	public void loadFilesInfo(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory()) {
			System.out.println(dirPath + "is not a directory either file !");
			return ;
		}
	
		File files[] = dir.listFiles();
		if (files == null) {
			System.out.println("cannot open directory:"+ dirPath + " is empty or Permission denied!");
			return;
		}
		for (int i=0; i < files.length; i++) {
			File tempFile = files[i];
			if (tempFile.isDirectory()) {
				loadFilesInfo(tempFile.getAbsolutePath());
				continue;
			}
			
			String fileName = tempFile.getName();
			if (!fileName.isEmpty())	{
				if (!filesInfo.containsKey(fileName)) {
					filesInfo.put(fileName, new ArrayList<FileInfo>());
				}
				filesInfo.get(fileName).add(new FileInfo(tempFile.getAbsolutePath(),tempFile.length(), tempFile.lastModified()));
			}
		}
	}
	
	// ȥ��FileInfo��implements Comparable<FileInfo>�ӿڣ�ʹ�ú�������
//    Collections.sort(listA, new Comparator<Person>() {
//        public int compare(Person arg0, Person arg1) {
//            return arg0.getOrder().compareTo(arg1.getOrder());
//        }
//    });	
	
	// ��ӡͬ���ظ��ļ��嵥
	public void dumpDuplicateFilesSort() {
		for (Entry<String, ArrayList<FileInfo>> entry : filesInfo.entrySet()) {
			List<FileInfo> infos = (ArrayList<FileInfo>) entry.getValue();
			if (infos.size() < 2) continue;	// ����2������ͬ���ļ��嵥������̎��Ч��

			List<FileInfo> uniInfos = new ArrayList<>();
			if (uniMode) {
				// ���Ȳ�ͬ��ͬ���ļ���Ϊ���ظ�����Ҫȥ��
				// ʹ��hashsetȥ�ظ���setΪ�ظ��ļ��ϣ�����ͨ��new ArrayList(set)ת����list
				HashSet<FileInfo> set = new HashSet<>();
				for (FileInfo info : infos) {
					// HashSet�е�add�����᷵��һ��Booleanֵ���������Ķ����Ѿ����ڣ������ʧ�ܷ���false
					// �жϴ��ڵ������ǣ�����hashCode����ѭ���Ƚ�HashSet�����ж�����hashֵ��ͬ�򷵻�true����Ҫ����ʹ��equals�����Ƚ�
					// ��hashֵ��ͬ�򷵻�false����ֱ�Ӳ���
					// ʹ��equals�����ȽϷ���true�������ͬ������ʧ��
					boolean add = set.add(info);
					if (add) {
						uniInfos.add(info);
					}
				}
			} else {
				uniInfos.addAll(infos);
			}
			
			if (uniInfos.size() < 2) continue; // ȥ�غ󣬺���2�������嵥
			// ������޸�ʱ������
			Collections.sort(uniInfos);
			// ����޸��ļ�ȥ��ɾ����־
			((FileInfo) uniInfos.get(uniInfos.size() - 1)).setKillFlag(FileInfo.KEEP_FLAG);

			for (int i = 0; i < uniInfos.size(); i++) {
				FileInfo f = (FileInfo) uniInfos.get(i);
				System.out.println(f.getKillFlag() + f.getUrl() + " # " + "�ļ���С��" + f.getSize() / 1024 + "Kb" + " # "
						+ "�ļ�����޸�ʱ�䣺" + new Date(f.getModifyTime()));
			}
			
			System.out.println();
		}
	}
	
	// ���򵼳��ظ��ļ����ļ� 
	public void outPutDuplicateFilesSort(String url)	throws Exception {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(url) , "UTF-8"));
		
		for (Entry<String, ArrayList<FileInfo>> en  : filesInfo.entrySet()) {  
			 List infos = (ArrayList<FileInfo>)en.getValue();
			 if (infos.size() > 1 ) {
				 Collections.sort(infos);
				 for (int i = 0 ; i < infos.size(); i++) {
					 FileInfo f = (FileInfo) infos.get(i);

					 writer.println(f.getUrl());
				 }
				 writer.println();
			 }
		} 
		
		writer.close();
	}	

}
