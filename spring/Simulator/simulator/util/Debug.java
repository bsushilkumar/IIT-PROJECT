package simulator.util;

import globalVariables.FileNames;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

/**
 * 
 */
public class Debug {
	public static final int ALL = 0;
	public static final int FINEST = 1;
	public static final int INFO = 2;
	public static final int WARNING = 3;
	public static final int ERROR = 4;
	public static final int FATAL = 5;
	public static int level = ERROR;

	/**
	 * debug
	 */
	public static boolean debug = false;
	/**
	 * level
	 */

	/**
	 * debugTrain
	 */
	public static int debugTrain = -1;
	/**
	 * currTrainNo
	 */
	public static int currTrainNo = -1;

	/**
	 * fileWriter
	 */

	public static FileWriter fileWriter;
	/**
	 * bufferedWriter
	 */
	public static BufferedWriter bufferedWriter;

	/**
     * 
     */
	public Debug() {
		try {
			FileWriter fileWriter = new FileWriter(FileNames.getDebugFileName());
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param filename
	 * @return setOutput
	 */
	public static boolean setOutput(String filename) {
		try {
			System.setOut(new PrintStream(new FileOutputStream(new File(
					filename))));
		} catch (Exception e) {
			System.out.println("Error during initialisation of op file:"
					.concat(String.valueOf(String.valueOf(filename))));
			boolean flag = false;
			return flag;
		}
		return true;
	}

	/**
	 * @param str
	 */
	public static void print(String str) {
		// System.out.println("Debug" + str);
		// if (debug /* && (debugTrain == -1 || currTrainNo == debugTrain) */) {
		//
		// try {
		// fileWriter = new FileWriter("debugFile1.txt");
		// fileWriter.append(str + '\n');
		// fileWriter.flush();
		// fileWriter.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	/**
	 * @param prty
	 * @param str
	 */
	public static void println(int priority, String str) {
		if (priority >= level) {
//			System.out.println(str);
			try {
				FileWriter fileWriter = new FileWriter(
						FileNames.getDebugFileName(), true);
				fileWriter.write(str + "\n");
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * @param value
	 * @param str
	 */
	public static void debug_assert(boolean value, String str) {
		if (!value) {
			System.out.println("Error : ".concat(String.valueOf(String
					.valueOf(str))));
			System.exit(0);
		}
	}

	public static void info(String string) {
		println(INFO, "info: "+string);
	}

	public static void all(String string) {
		println(ALL, string);
	}

	public static void fine(String string) {
		println(FINEST, "finest: "+string);
	}

	public static void warning(String string) {
		println(WARNING, "warning: "+string);
	}

	public static void error(String string) {
		println(ERROR, "error: "+string);
	}

	public static void fatal(String string) {
		println(FATAL, "fatal: "+string);
	}

}
