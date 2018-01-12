package simulator.scripts1;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class test6 {
	public static void main(String args[]) {
		int fileNames[] = { 20, 21, 22, 23 };
		String baseDirectory = "testCases/churchgateVirar/GPS/";
		String extension = ".txt";

		for (int fileName : fileNames) {
			try {

				BufferedReader br = new BufferedReader(new FileReader(
						baseDirectory + fileName + extension));
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						baseDirectory + fileName + "out" + extension));
				String line;
				br.readLine();
				boolean firstEntry = true;
				int firstTimeValue = 0;

				while ((line = br.readLine()) != null) {
					String tokens[] = line.split(",");
					String timeString = tokens[0];
					String timeStampString = timeString.substring(
							timeString.indexOf("T") + 1,
							timeString.indexOf("Z"));
					String timeStampTokens[] = timeStampString.split(":");
					int hour = Integer.parseInt(timeStampTokens[0]);
					int minute = Integer.parseInt(timeStampTokens[1]);
					int second = Integer.parseInt(timeStampTokens[2]);

					int totalTime = hour * 3600 + minute * 60 + second;

					if (firstEntry) {
						firstEntry = false;
						firstTimeValue = totalTime;
					}

					totalTime = totalTime - firstTimeValue;

					String speedString = tokens[6];
					double speed = Double.parseDouble(speedString);

					System.out.println("h " + hour + " m " + minute + " s "
							+ second + " t " + totalTime + " speed " + speed);

					bw.write(totalTime + "\t" + speed + "\n");
				}

				br.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
