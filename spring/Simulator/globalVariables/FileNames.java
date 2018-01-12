package globalVariables;

import input.SectionInputDialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

public class FileNames {
	private static String testCaseDirectory = "testCases/churchgateVirar";

	private static String stationFileName = "station.txt";
	private static String blockFileName = "block.txt";
	private static String loopFileName = "loop.txt";
	private static String linkFileName = "link.txt";
	private static String gradientFileName = "gradientList.txt";
	private static String gradientEffectsFileName = "gradientEffects.txt";
	private static String scheduledTrainsFileName = "scheduled.txt";
	private static String unscheduledTrainsFileName = "unscheduled.txt";
	private static String parametersFileName = "param.dat";
	private static String blockDirectionFileName = "BlockDirectionInInterval.txt";
	private static String filePathFileName = "FilePath";

	private static String railwayLineDiagramFileName = "railwayLineDiagram.txt";
	private static String signalDiagramFileName = "signalDiagram.txt";
	private static String stationDiagramFileName = "stationDiagram.txt";
	private static String loopDiagramFileName = "loopDiagram.txt";
	private static String blockDiagramFileName = "blockDiagram.txt";
	private static String linkDiagramFileName = "linkDiagram.txt";
	private static String gradientDiagramFileName = "gradientDiagram.txt";
	private static String gradientEffectDiagramFileName = "gradientEffectDiagram.txt";
	private static String speedRestrictionDiagramFileName = "speedRestrictionDiagram.txt";

	private static String trainDiagramFileName = "trainDiagram.txt";

	private static String signalFailureFileName;

	private static String passengerDelayFileName = "passengerDelay.txt";

	private static String debugFileName = "debug.txt";

	// private static String
	public static void setStationFileName(String stationFileName) {
		FileNames.stationFileName = stationFileName;
	}

	public static String getStationFileName() {
		String fileName = getTestCaseDirectory() + "/" + stationFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setBlockFileName(String blockFileName) {
		FileNames.blockFileName = blockFileName;
	}

	public static String getBlockFileName() {
		String fileName = getTestCaseDirectory() + "/" + blockFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setLoopFileName(String loopFileName) {
		FileNames.loopFileName = loopFileName;
	}

	public static String getLoopFileName() {
		String fileName = getTestCaseDirectory() + "/" + loopFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setGradientFileName(String gradientFileName) {
		FileNames.gradientFileName = gradientFileName;
	}

	public static String getGradientFileName() {
		String fileName = getTestCaseDirectory() + "/" + gradientFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setGradientEffectsFileName(String gradientEffectsFileName) {
		FileNames.gradientEffectsFileName = gradientEffectsFileName;
	}

	public static String getGradientEffectsFileName() {
		String fileName = getTestCaseDirectory() + "/"
				+ gradientEffectsFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setScheduledTrainsFileName(String scheduledTrainsFileName) {
		FileNames.scheduledTrainsFileName = scheduledTrainsFileName;
	}

	public static String getScheduledTrainsFileName() {
		String fileName = getTestCaseDirectory() + "/"
				+ scheduledTrainsFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setUnscheduledTrainsFileName(
			String unscheduledTrainsFileName) {
		FileNames.unscheduledTrainsFileName = unscheduledTrainsFileName;
	}

	public static String getUnscheduledTrainsFileName() {
		String fileName = getTestCaseDirectory() + "/"
				+ unscheduledTrainsFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setParametersFileName(String parametersFileName) {
		FileNames.parametersFileName = parametersFileName;
	}

	public static String getParametersFileName() {
		String fileName = getTestCaseDirectory() + "/" + parametersFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setBlockDirectionFileName(String blockDirectionFileName) {
		FileNames.blockDirectionFileName = blockDirectionFileName;
	}

	public static String getBlockDirectionFileName() {
		String fileName = getTestCaseDirectory() + "/" + blockDirectionFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getRailwayLineDiagramFile() {
		String fileName = getTestCaseDirectory() + "/"
				+ railwayLineDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getSignalDiagramFile() {
		String fileName = getTestCaseDirectory() + "/" + signalDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getStationDiagramFile() {
		String fileName = getTestCaseDirectory() + "/" + stationDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getStationFile() {
		String fileName = getTestCaseDirectory() + "/" + stationFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setTestCaseDirectory(String testCaseDirectory) {
		FileNames.testCaseDirectory = testCaseDirectory;
	}

	public static String getTestCaseDirectory() {
		return testCaseDirectory;
	}

	public static void setLoopDiagramFileName(String loopDiagramFileName) {
		FileNames.loopDiagramFileName = loopDiagramFileName;
	}

	public static String getLoopDiagramFileName() {
		String fileName = testCaseDirectory + "/" + loopDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getBlockDiagramFileName() {
		String fileName = testCaseDirectory + "/" + blockDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getLinkDiagramFileName() {
		String fileName = testCaseDirectory + "/" + linkDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getGradientDiagramFileName() {
		String fileName = testCaseDirectory + "/" + gradientDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getGradientEffectDiagramFileName() {
		String fileName = testCaseDirectory + "/"
				+ gradientEffectDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getSpeedRestrictionDiagramFileName() {
		String fileName = testCaseDirectory + "/"
				+ speedRestrictionDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getTrainDiagramFileName() {
		String fileName = testCaseDirectory + "/" + trainDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getLinkFileName() {
		String fileName = testCaseDirectory + "/" + linkFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getSignalDiagramFileName() {
		String fileName = testCaseDirectory + "/" + signalDiagramFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static void setSignalFailureFileName(String text) {
		signalFailureFileName = text;
	}

	public static void setPassengerDelayFileName(String text) {
		passengerDelayFileName = text;
	}

	public static String getPassengerDelayFileName() {
		String fileName = testCaseDirectory + "/" + passengerDelayFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getSignalFailureFileName() {
		String fileName = testCaseDirectory + "/" + signalFailureFileName;
		System.out.println(fileName);
		return fileName;
	}

	public static String getDebugFileName() {
		String fileName = testCaseDirectory + "/" + debugFileName;
		return fileName;
	}

	public static int chooseDirectory(String testCaseDirectoryPath,
			String action) {
		try {
			String currentDirectoryPath;
			if (testCaseDirectoryPath.isEmpty())
				currentDirectoryPath = (new File("")).getCanonicalPath();
			else
				currentDirectoryPath = testCaseDirectoryPath;

			JFileChooser chooser = new JFileChooser(currentDirectoryPath);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();

			int response = JFileChooser.CANCEL_OPTION;
			if (action.equalsIgnoreCase("new")
					|| action.equalsIgnoreCase("open"))
				response = chooser.showOpenDialog(sectioninputdialog);
			else if (action.equalsIgnoreCase("save")
					|| action.equalsIgnoreCase("save as"))
				response = chooser.showSaveDialog(sectioninputdialog);

			if (response == JFileChooser.APPROVE_OPTION) {
				// user clicked Save
				String testcaseDirectoryPath = chooser.getSelectedFile()
						.getCanonicalPath();
				FileNames.setTestCaseDirectory(testcaseDirectoryPath);
			} else {
				// user cancelled...
				System.out.println("I cancelled choosing the directory");
			}

			return response;
		} catch (Exception ex) {
			return -1;
		}

	}

	public static void writeFilePath() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				FileNames.getFileName()));
		bw.write(stationFileName);
		bw.write("\n");
		bw.write(blockFileName);
		bw.write("\n");
		bw.write(loopFileName);
		bw.write("\n");
		bw.write(parametersFileName);
		bw.write("\n");
		bw.write(gradientFileName);
		bw.write("\n");
		bw.write(gradientEffectsFileName);
		bw.write("\n");
		bw.write(scheduledTrainsFileName);
		bw.write("\n");
		bw.write(unscheduledTrainsFileName);
		bw.write("\n");
		//shashank
		bw.write(blockDirectionFileName);
		bw.write("\n");


	}
	
	public static String getFileName() {
		String fileName = getTestCaseDirectory() + "/" + filePathFileName;
		System.out.println(fileName);
		return fileName;
	}
}
