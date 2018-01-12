package gui.entities.sectionEntityList;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackProperties.Gradient;
import gui.entities.sectionEntities.trackProperties.GradientEffect;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import simulator.input.SimulationInstance;
import simulator.util.Debug;

/**
 * Class for GradientList
 */
public class GradientList extends ArrayList<Gradient> {

	/**
	 * constructor
	 */
	public GradientList() {
	}

	/**
	 * @return read gradientList
	 * @throws IOException
	 */
	public static ArrayList<Gradient> readGradient() throws IOException {

		Debug.print("GradientList: readGradient: I am in read_gradient");

		ArrayList<Gradient> gradientFormatList = new ArrayList<Gradient>();

		try {
			Reader reader = new FileReader(FileNames.getGradientFileName());
			StreamTokenizer st = new StreamTokenizer(reader);
			st.slashSlashComments(true);
			st.slashStarComments(true);

			st.whitespaceChars(0, 3);

			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				double startMileP, endMileP;
				String dir, value;

				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug.print("GradientList: readGradient: Error in format of input file : grad_info.dat");
					Debug.print("GradientList: readGradient: Error : grad_value expected");
				}
				value = st.sval;
				Debug.print("value read" + st.sval);
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug.print("GradientList: readGradient: Error in format of input file : grad_info.dat");
					Debug.print("GradientList: readGradient: Error : direction    expected");
				}
				Debug.print("GradientList: readGradient: value read is "
						+ st.sval);
				dir = st.sval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("GradientList: readGradient: Error in format of input file : grad_info.dat");
					Debug.print("GradientList: readGradient: Error : start mile post expected");
				}
				Debug.print("GradientList: readGradient: value read is "
						+ st.nval);
				startMileP = st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("GradientList: readGradient: Error in format of input file : grad_info.dat");
					Debug.print("GradientList: readGradient: Error : end mile post expected");
				}
				Debug.print("GradientList: readGradient: value read is "
						+ st.nval);
				endMileP = st.nval;
				Gradient gradient = new Gradient(value, dir, startMileP,
						endMileP, null);
				gradientFormatList.add(gradient);
			}
		} catch (IOException e) {
			Debug.print("GradientList: readGradient: *********I am iN gradientList File not Found *******");
		}
		for (int i = 0; i < gradientFormatList.size(); i++) {
			Gradient gradient = gradientFormatList.get(i);
			Debug.print(gradient.getDirection() + "  "
					+ gradient.getRelativeStartMilePost() + "  "
					+ gradient.getRelativeEndMilePost());
		}

		return gradientFormatList;

	}

	/**
	 * @param gradient
	 */
	public boolean add(Gradient gradient, int direction) {
		Debug.print("I am in add gradientList");

		if (direction == GlobalVar.DOWN_DIRECTION) {
			String tem = new String();
			if ((gradient.getDirection().equalsIgnoreCase("UP") == true)) {
				tem = "DOWN";
			}
			if ((gradient.getDirection().equalsIgnoreCase("DOWN") == true)) {
				tem = "UP";
			}
			if ((gradient.getDirection().equalsIgnoreCase("LEVEL") == true)) {
				tem = "LEVEL";
			}

			gradient.setDirection(tem);
		}

		return super.add(gradient);
	}

	/**
	 * @param i
	 * @return the ith element in the list
	 */
	public Gradient get(int i) {
		Gradient gradient = super.get(i);
		return gradient;
	}

	/**
	 * @param block
	 * @param a
	 * @param b
	 * @return {@link Gradient}
	 */
	public Gradient returnGradientFormat(double start, double end, Block block) {
		double st, en;
		Gradient gradient = new Gradient("0", "level", start, end, block);

		for (int i = 0; i < this.size(); i++) {
			st = this.get(i).getRelativeStartMilePost();
			en = this.get(i).getRelativeEndMilePost();
			if (start >= st && en <= end) {
				gradient = this.get(i);

				if (gradient.getDirection() == null) {
					gradient.setDirection("LEVEL");
				}
				return gradient;
			}

		}

		return gradient;
	}

	public static void readGradientAndGenerateTinyBlocks(
			SimulationInstance simulationInstance) {
		simulationInstance.initiateSignalFailureParameters();

		ArrayList<Gradient> gradientFormatList = new ArrayList<Gradient>();

		// Read GradientList
		try {
			gradientFormatList = GradientList.readGradient();
		} catch (Exception e) {
			Debug.print("start: ****************      Could not read GRadient profile of the section ********** ");
		}

		Debug.print("start: GRadient profile array  Size is "
				+ gradientFormatList.size());

		/** Read gradientList effect of the section ****/
		try {
			GradientEffect.readEffect();
		} catch (Exception e) {
			Debug.print("start:  ************* Could not read  gradient_effect       ***********");
		}

		int j = 0;
		// String dir1;
		Debug.print("start: J ABOVE IS   " + j);
		Debug.print("start: GRADIENT SIZE IS " + gradientFormatList.size());

		simulationInstance.createTinyBlockFormatsForBlocks(gradientFormatList);

	}

	public static void buildGradientForBlock(String gradientValuesString,
			String gradientDirectionsString,
			String relativeStartMilePostsString,
			String relativeEndMilePostsString, Block block) {

		StringTokenizer gradientValueStringTokenizer = new StringTokenizer(
				gradientValuesString, GlobalVar.majorStringDelimiter);
		StringTokenizer gradientDirectionStringTokenizer = new StringTokenizer(
				gradientDirectionsString, GlobalVar.majorStringDelimiter);
		StringTokenizer relativeStartMilePostStringTokenizer = new StringTokenizer(
				relativeStartMilePostsString, GlobalVar.majorStringDelimiter);
		StringTokenizer relativeEndMilePostStringTokenizer = new StringTokenizer(
				relativeEndMilePostsString, GlobalVar.majorStringDelimiter);

		while (gradientValueStringTokenizer.hasMoreTokens()) {
			String gradientValue = gradientValueStringTokenizer.nextToken();
			String gradientDirection = gradientDirectionStringTokenizer
					.nextToken();
			String relativeStartMilePostString = relativeStartMilePostStringTokenizer
					.nextToken();
			String relativeEndMilePostString = relativeEndMilePostStringTokenizer
					.nextToken();

			double relativeStartMilePost = Double
					.parseDouble(relativeStartMilePostString);
			double relativeEndMilePost = Double
					.parseDouble(relativeEndMilePostString);
			int directionInt = GlobalVar
					.getDirectionIntFromString(gradientDirection);

			Gradient gradient = new Gradient(gradientValue, gradientDirection,
					relativeStartMilePost, relativeEndMilePost, block);
			block.getGradientList().add(gradient, directionInt);
		}
	}

	public static void readGradients(StreamTokenizer streamtokenizer,
			Block block) throws IOException {
		// System.out.println("readGradients: for block " + blockNo);

		// read gradientList value
		streamtokenizer.nextToken();
		String gradientValuesString = streamtokenizer.sval;

		// read gradientList of direction
		streamtokenizer.nextToken();
		String gradientDirectionsString = streamtokenizer.sval;

		// read relative starting milePosts of gradientList with respect to
		// startMilePost of block
		streamtokenizer.nextToken();
		String relativeStartMilePostsString = streamtokenizer.sval;

		// read relative ending milePosts of gradientList with respect to
		// startMilePost of block
		streamtokenizer.nextToken();
		String relativeEndMilePostsString = streamtokenizer.sval;

		GradientList.buildGradientForBlock(gradientValuesString,
				gradientDirectionsString, relativeStartMilePostsString,
				relativeEndMilePostsString, block);
	}

}
