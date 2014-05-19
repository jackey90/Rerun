package com.ea.rerun.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileAnalyser {
	private final File file;

	public FileAnalyser(String fileName) {
		file = new File(fileName);
	}

	public FileAnalyser(File file) {
		this.file = file;
	}

	public String readLine(int lineNo) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				if (line == lineNo) {
					return tempString;
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return null;
	}

}
