package com.slava.file;

import java.io.File;

public class CompareFiles {
	String path1 = null;
	String path2 = null;
	
	public void run() {
		File root1 = new File(path1);
		File root2 = new File(path2);
		compareFolders(root1, root1.getName(), root2, root2.getName());
	}

	public void compareFolders(File root1, String path1, File root2, String path2) {
		if(root2.getName().equals("bin")) return;
		if(root2.getName().equals(".metadata")) return;
		if(!root2.isDirectory()) {
			System.out.println(path1 + " is new.");
			return;
		}
		File[] fs1 = root1.listFiles();
		if(fs1 == null || fs1.length == 0) return;
		for (int i = 0; i < fs1.length; i++) {
			File f1 = fs1[i];
			File f2 = new File(root2, f1.getName());
			if(!f2.exists()) {
				System.out.println(path1 + "/" + f1.getName() + " is new.");
				continue;
			}
			if(f1.isDirectory()) {
				compareFolders(f1, path1 + "/" + f1.getName(), f2, path2 + "/" + f1.getName());
			} else {
				if(f1.lastModified() > f2.lastModified()) {
					System.out.println(path1 + "/" + f1.getName() + " is modified.");
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CompareFiles p = new CompareFiles();
		p.path2 = "C:/Olia/eclipse/workspace";
		p.path1 = "G:/workspace";
		p.run();
		

	}

}
