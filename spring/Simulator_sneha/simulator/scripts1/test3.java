package simulator.scripts1;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;

/**
 * 
 * @author peeyush
 * 
 */
public class test3 {

	public static void readAll() throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("DNfinal.txt"));
		String line = "";
		String out = "";
		while ((line = br.readLine()) != null) {
			Boolean include = true;
			String temp = "";
			// out += "\n";
			StringTokenizer st = new StringTokenizer(line, ",");
			if (!st.hasMoreTokens()) {
				continue;
			}
			String trainum = st.nextToken();
			if (trainum.equals("NF"))
				continue;
			temp += trainum;
			temp += " up";
			temp += " 0.258";
			temp += " 1.332 1.188 3 75 all ";

			temp += st.nextToken() + " ";
			while (st.hasMoreTokens()) {
				String station = st.nextToken();
				if (!st.hasMoreTokens()) {
					break;
				}
				String platform = st.nextToken();

				String platformName = "";
				String stationName = "";
				int num = 0;
				int flag = 0;

				StreamTokenizer loopStreamTokenizer = new StreamTokenizer(
						new FileReader("loop.txt"));

				loopStreamTokenizer.slashSlashComments(true);
				loopStreamTokenizer.slashStarComments(true);

				while (loopStreamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
					// System.out
					// .println("Counting the number of times the loop is executed");

					num = (int) loopStreamTokenizer.nval;
					// loopStreamTokenizer.nextToken();

					loopStreamTokenizer.nextToken();

					loopStreamTokenizer.nextToken();
					String loopType = loopStreamTokenizer.sval;

					// System.out.println("loop type is " + loopType);

					// ignore station name
					loopStreamTokenizer.nextToken();
					stationName = loopStreamTokenizer.sval;
					// platform name
					loopStreamTokenizer.nextToken();
					platformName = loopStreamTokenizer.sval;
					if (stationName.equalsIgnoreCase(station.trim())
							&& platformName.equals(platform.trim())) {
						flag = 1;
						break;
					}
					// System.out.println(stationName);
					// start milePost
					loopStreamTokenizer.nextToken();
					double startMilepost = loopStreamTokenizer.nval;

					// end milePost
					loopStreamTokenizer.nextToken();
					double endMilepost = loopStreamTokenizer.nval;

					loopStreamTokenizer.nextToken();
					double maximumPossibleSpeed = loopStreamTokenizer.nval;

					for (int i = 0; i < 21; i++) {
						loopStreamTokenizer.nextToken();
					}

				}
				if (flag == 1) {/* System.out.println("yes") */
					;
					temp += num + " ";
				} else {
					temp += "- ";
					include = false;
				}
				temp += st.nextToken().replaceAll(":", "").trim() + "00 "
						+ st.nextToken().replaceAll(":", "").trim() + "00 ";
				// System.out.println(out);

			}
			if (include) {
				out += temp + "\n\n";
			}
			// System.out.println(out);
		}
		FileWriter fstream = new FileWriter("scheduledDN.txt");
		BufferedWriter out2 = new BufferedWriter(fstream);
		out2.flush();
		out2.write(out);
		out2.close();

	}

	public static void main(String args[]) throws Exception {
		readAll();
	}
}
