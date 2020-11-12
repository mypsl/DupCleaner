package com.mypsl.util;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
	
	public static void main2(String args[]) throws IOException {
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
			finder.setDirPath(args[1]);
			finder.setUniMode(true);
		}
		else if (args.length == 1){ // 扫描参数指定目录
			finder.setDirPath(args[0]);
		} else { // 未指定目录缺省扫描当前目录
			File curDir = new File(".");
			try {
				finder.setDirPath(curDir.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("---#" + finder.getDirPath() + "#---");
		finder.scanDirPath();
		finder.dumpDuplicateFilesSort();
	}

	private static void toSetupOutFile(String [] arr) {
		if (isScanning) {
			System.out.println("now is scanning, Operation stopped");
			return;
		}

		if (1 < arr.length) {
			scanFilePath = arr[1];
			System.out.println("output file setup succeeded!");
		} else {
			System.out.println("out file path:" + scanFilePath);
		}
	}

	private static void toScan() {
		if (isScanning) {
			System.out.println("now is scanning, Operation stopped");
			return;
		}
		// to scan
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				isScanning = true;
				System.out.println("scanning...");
				try {
//					TimeUnit.SECONDS.sleep(10);
					DupFinder finder = new DupFinder(true, scanFilePath);
					finder.scanDirPath();
					if (dumpFilePath.isEmpty()) {
						System.out.println("dumpDuplicateFilesSort...");
						finder.dumpDuplicateFilesSort();
					} else {
						finder.outPutDuplicateFilesSort(dumpFilePath);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				isScanning = false;
			}
		});
		thread.start();
	}

	private static void showUsage() {
		System.out.println("out[o] [filename]  : Specify the destination path for the scan result file to be output");
		System.out.println("scan[s] [pathname] : scan current path if [pathname] is not be defined.");
	}

	private static volatile boolean isScanning = false;
	private static String dumpFilePath = "";
	private static String scanFilePath = "";
	public static void main(String[] args) {
		System.out.println("===============================");
		System.out.println("usage:");
		showUsage();
		System.out.println("===============================");

		Scanner scanner = new Scanner(System.in);
		while (true) {
			String input = scanner.nextLine();
//			String的split方法支持正则表达式；
//			正则表达式\s表示匹配任何空白字符，+表示匹配一次或多次。
			String [] arr = input.split("\\s+");
			switch (arr[0]) {
				case "s":
					toScan();
					continue;
				case "o":
					toSetupOutFile(arr);
					continue;
				case "q":
					System.out.println("quit!");
					return;

				default:
					System.out.println("invalid input!");
			}


		}
	}


}