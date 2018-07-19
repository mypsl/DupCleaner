package com.mypsl.util;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @author moyi
 * v1.0 用法
 *  DupCleaner -d 清理名单 
 *  DupCleaner + [扫描目录]
 * v1.1 清除文件时到当前目录下垃圾箱trash+当前时间
 * v1.2 增加-u 去重扫描方式，只列出文件长度相同的列表
 */

public class Run {
	public Run() {}
	
	public static void main(String args[])
	{
		DupFinder finder  = new DupFinder();
		
		// -d 参数，按规则清理文件清单
		if (args.length == 2 && args[0].equals("-d")) {
			FileCleaner fc = new FileCleaner();
			try {
				fc.deadList(args[1]);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			return;
		}else if (args.length == 2 && args[0].equals("-u")){
			// -u 参数，扫描后按规则去重
			finder.dirPath = args[1];
			finder.uniMode = true;
		}
		else if (args.length == 1){ // 扫描参数指定目录
			finder.dirPath = args[0];
		} else { // 未指定目录缺省扫描当前目录
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