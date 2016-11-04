/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gui.delete;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Guilherme
 */
public class Delete_file_extension {
    private static final int MAX_DEPTH = 20;
    private static final Pattern PATTERN_EXTENSION = Pattern.compile(".+\\.([^\\.]+)$");

    /**
     * @param extensions file extensions (do not use .)
     */
    public static void main(String[] extensions) {
    	if (extensions.length == 0) {
    		extensions = new String[]{"html","css"};
    	} else {
    		removeDot(extensions);
    	}
    	System.out.print("Extensions: ");
    	for (String ext : extensions) {
    		System.out.print(ext + " ");
    	}
    	System.out.print("\n");
        List<String> willDeleteFileList = findFilesFromHere(extensions);
        confirmDelete(willDeleteFileList);
    }
    
    private static void removeDot(final String[] extensions) {
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].contains(".")) {
                extensions[i] = extensions[i].replaceAll("\\.", "");
            }
        }
    }

    private static List<String> findFilesFromHere(final String[] extensions) {
        final File executionFolder = new File(getExecutionPath());
        if (!executionFolder.exists()) {
        	System.out.println("Folder not found: " + executionFolder.getAbsolutePath());
        	System.exit(1);
        }
        return findRecursively(executionFolder, extensions, 0, new ArrayList<>());
    }

    private static String getExecutionPath() {
//        final String executionPath = System.getProperty("user.dir");
//        if (executionPath == null || executionPath.isEmpty()) {
//            throw new RuntimeException("Empty user.dir");
//        }
    	final String executionPath = "C:\\Users\\Aluno";
        
        System.out.println("Path: " + executionPath);
        return executionPath;
    }

    private static List<String> findRecursively(
            final File folder, 
            final String[] extensions, 
            final int depth,
            final List<String> foundFiles) {
        
    	if (!folder.exists()) {
    		System.out.println("Folder not found: " + folder.getAbsolutePath());
    		return foundFiles;
    	} else if (!folder.isDirectory()) {
    		System.out.println("Not a directory: " + folder.getAbsolutePath());
    		return foundFiles;
    	}
    	
        if (depth > MAX_DEPTH) return foundFiles;
        
        if (folder.listFiles() == null) {
        	System.out.println("Error reading: " + folder.getAbsolutePath());
        	return foundFiles;
        }
        
        for (File file : folder.listFiles()){
            
            if (file.isDirectory()) {
                findRecursively(file, extensions, depth+1, foundFiles);
                
            } else if (file.isFile() && hasExtension(file.getName(), extensions)) {
                foundFiles.add(file.getAbsolutePath());
            }
        }
        return foundFiles;
    }

    private static boolean hasExtension(final String name, final String[] extensions) {
        final Matcher matcher = PATTERN_EXTENSION.matcher(name);
        for (String ext : extensions) {
            if (matcher.matches() && matcher.group(1).equals(ext)) {
                return true;
            }
        }
        return false;
    }

    private static void confirmDelete(List<String> willDeleteFileList) {
        System.out.println(willDeleteFileList.size() + " files found:");
        for (String name : willDeleteFileList) {
            System.out.println("\t"+name);
        }
        System.out.println("Confirm? (y or n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        scanner.close();
        if (!"y".equals(input)) {
            System.exit(0);
            return;
        }
        for (String name : willDeleteFileList) {
        	new File(name).delete();
        }
    }
    
}
