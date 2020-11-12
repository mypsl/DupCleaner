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

	// 将路径下文件信息载人Map
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
	
	// 去掉FileInfo的implements Comparable<FileInfo>接口，使用函数排序
//    Collections.sort(listA, new Comparator<Person>() {
//        public int compare(Person arg0, Person arg1) {
//            return arg0.getOrder().compareTo(arg1.getOrder());
//        }
//    });	
	
	// 打印同名重复文件清单
	public void dumpDuplicateFilesSort() {
		for (Entry<String, ArrayList<FileInfo>> entry : filesInfo.entrySet()) {
			List<FileInfo> infos = (ArrayList<FileInfo>) entry.getValue();
			if (infos.size() < 2) continue;	// 忽略2条以下同名文件清单，增強處理效率

			List<FileInfo> uniInfos = new ArrayList<>();
			if (uniMode) {
				// 长度不同的同名文件认为不重复，需要去重
				// 使用hashset去重复，set为重复的集合，可以通过new ArrayList(set)转换成list
				HashSet<FileInfo> set = new HashSet<>();
				for (FileInfo info : infos) {
					// HashSet中的add方法会返回一个Boolean值，如果插入的对象已经存在，则插入失败返回false
					// 判断存在的依据是，先用hashCode方法循环比较HashSet中所有对象，若hash值相同则返回true，需要继续使用equals方法比较
					// 若hash值不同则返回false，可直接插入
					// 使用equals方法比较返回true则对象相同，插入失败
					boolean add = set.add(info);
					if (add) {
						uniInfos.add(info);
					}
				}
			} else {
				uniInfos.addAll(infos);
			}
			
			if (uniInfos.size() < 2) continue; // 去重后，忽略2条以下清单
			// 按最后修改时间排序
			Collections.sort(uniInfos);
			// 最后修改文件去掉删除标志
			((FileInfo) uniInfos.get(uniInfos.size() - 1)).setKillFlag(FileInfo.KEEP_FLAG);

			for (int i = 0; i < uniInfos.size(); i++) {
				FileInfo f = (FileInfo) uniInfos.get(i);
				System.out.println(f.getKillFlag() + f.getUrl() + " # " + "文件大小：" + f.getSize() / 1024 + "Kb" + " # "
						+ "文件最后修改时间：" + new Date(f.getModifyTime()));
			}
			
			System.out.println();
		}
	}
	
	// 排序导出重复文件到文件 
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
