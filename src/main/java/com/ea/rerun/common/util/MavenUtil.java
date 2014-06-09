package com.ea.rerun.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MavenUtil {

	public static void run(String command) {
		// Properties props=System.getProperties();
		// String osName = props.getProperty("os.name");
		// String osArch = props.getProperty("os.arch");
		// String osVersion = props.getProperty("os.version");
		// System.out.println(osName);
		// System.out.println(osArch);
		// System.out.println(osVersion);

		Process process = null;
		String osName = System.getProperty("os.name");// current os name
		boolean isWindows = true;
		boolean isLinux = false;
		if (osName.contains("windows")) {
			isWindows = true;
			isLinux = false;
		} else if (osName.contains("linux")) {
			isWindows = false;
			isLinux = true;
		}
		ProcessBuilder pb = null;
		try {

			if (isWindows) {
				pb = new ProcessBuilder("cmd", "/c", command);
			} else if (isLinux) {
				pb = new ProcessBuilder(command);
			}

			// pb.redirectErrorStream();
			process = pb.start();
			//System.out.println("Run command :" + command);
			InputStreamConsumer isc = new InputStreamConsumer(
					process.getInputStream());
			isc.start();
			int exitCode = process.waitFor();
			isc.join();
			// System.out.println("Process terminated with " + exitCode);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	public static void run(List<String> commands) {
		if (commands != null) {
			for (String cmd : commands) {
				run(cmd);
			}
		}
	}

	public static class InputStreamConsumer extends Thread {

		private InputStream is;

		public InputStreamConsumer(InputStream is) {
			this.is = is;
		}

		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			try {
				String line = null;
				int index = 0;
				while ((line = br.readLine()) != null) {
					if (line != null) {
						index++;
						// System.out.println(line);
						if (index % 10 == 0) {
							System.out.print(".");
						}
					}
				}
				System.out.println();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
