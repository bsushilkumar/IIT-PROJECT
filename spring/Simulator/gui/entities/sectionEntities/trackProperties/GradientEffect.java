package gui.entities.sectionEntities.trackProperties;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Entity;
import gui.entities.sectionEntityList.GradientEffectList;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import simulator.util.Debug;

public class GradientEffect extends Entity{

	static GradientEffectList gradientEffectUpArray = new GradientEffectList();
	static GradientEffectList gradientEffectDownArray = new GradientEffectList();

	/**
	 * gradientValue
	 */
	public String gradientValue;

	public int direction;
	/**
	 * accelerationChange
	 */
	public double accelerationChange;
	/**
	 * decelerationChange
	 */
	public double decelerationChange;

	/**
	 * @param a
	 * @param c
	 * @param d
	 */
	public GradientEffect(String a, double c, double d) {
		setGradientValue(a);
		setAccelerationChange(c);
		setDecelerationChange(d);
	}

	public GradientEffect() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param Up_gray
	 * @param Down_gray
	 * @throws IOException
	 */
	@SuppressWarnings({ "static-access" })
	public static void readEffect() throws IOException {

		GradientEffectList upSideGradientEffects = new GradientEffectList();
		GradientEffectList downSideGradientEffects = new GradientEffectList();

		Debug.print("GradientEffect: readEffect: I am in gradient_effect");
		Reader r = new FileReader(FileNames.getGradientEffectsFileName());
		GradientEffect grad_eff;
		StreamTokenizer st = new StreamTokenizer(r);
		st.whitespaceChars(0, 3);
		String upDirection = "up";
		String downDirection = "down";
		try {
			while (st.nextToken() != st.TT_EOF) {
				double acceleration_change, deceleration_change;
				String direction, value;
				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug.print("GradientEffect: readEffect: Error in format of input file : Up_grad_info.dat");
					Debug.print("GradientEffect: readEffect: Error : grad_value expected");
				}
				value = st.sval;
				Debug.print("GradientEffect: readEffect: GradientList value read is "
						+ st.sval);
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug.print("GradientEffect: readEffect: Error in format of input file : gradient_efect.txt.");
					Debug.print("GradientEffect: readEffect: Error : direction expected");
				}

				direction = st.sval;
				Debug.print("GradientEffect: readEffect: DIRECTION IS "
						+ direction);
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("GradientEffect: readEffect: Error in format of input file : Up_grad_info.dat.");
					Debug.print("GradientEffect: readEffect: Error : acceleration_change expected");
				}
				Debug.print("GradientEffect: readEffect: acceleration change read is "
						+ st.nval);
				acceleration_change = st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("GradientEffect: readEffect: Error in format of input file : Up_grad_info.dat.");
					Debug.print("GradientEffect: readEffect: Error : deceleration_change expected");
				}
				Debug.print("GradientEffect: readEffect: value read is "
						+ st.nval);
				Debug.print("GradientEffect: readEffect: deceleration change read is "
						+ st.nval);
				deceleration_change = st.nval;

				// System.out.println(direction + " " + dir1);
				if (direction.equalsIgnoreCase(upDirection) == true) {
					grad_eff = new GradientEffect(value, acceleration_change,
							deceleration_change);
					upSideGradientEffects.add(grad_eff);
					Debug.print("GradientEffect: readEffect: adding Up");
				}
				if (direction.equalsIgnoreCase(downDirection) == true) {
					grad_eff = new GradientEffect(value, acceleration_change,
							deceleration_change);
					downSideGradientEffects.add(grad_eff);
					Debug.print("GradientEffect: readEffect: adding down");
				}
			}
		} catch (IOException e) {
			Debug.print("GradientEffect: readEffect: File Not Found");
		}

		GradientEffect.gradientEffectUpArray = upSideGradientEffects;
		GradientEffect.gradientEffectDownArray = downSideGradientEffects;
	}

	/**
	 * @param gradientValue
	 * @return acceleration
	 */
	public static double returnAcceleration(String gradientValue, int direction) {
		ArrayList<GradientEffect> gradientEffects = getGradientEffectsFromDirection(direction);
		double acc = 0;
		if (gradientEffects == null)
			return acc;

		String temp;
		Debug.print("GradientEffect: returnAcceleration: In return acceleration "
				+ "function of gradientList effects  ");
		for (int i = 0; i < gradientEffects.size(); i++) {
			temp = gradientEffects.get(i).getGradientValue();
			if (gradientValue.equalsIgnoreCase(temp)) {
				acc = (gradientEffects.get(i).getAccelerationChange());
			}
		}
		Debug.print("GradientEffect: returnAcceleration: returning " + acc);
		return acc;
	}

	/**
	 * @param gradientValue
	 * @return deceleration
	 */
	public static double returnDeceleration(String gradientValue, int direction) {
		ArrayList<GradientEffect> gradientEffects = getGradientEffectsFromDirection(direction);
		double dec = 0;
		String temp;
		Debug.print("GradientEffect: returnDeceleration: In return acceleration function"
				+ " of gradientList effect up  ");
		for (int i = 0; i < gradientEffects.size(); i++) {
			temp = gradientEffects.get(i).getGradientValue();
			if (gradientValue.equalsIgnoreCase(temp)) {
				dec = (gradientEffects.get(i).getDecelerationChange());
			}
		}
		Debug.print(" returning " + dec);
		return dec;
	}

	private static ArrayList<GradientEffect> getGradientEffectsFromDirection(
			int direction) {
		if (direction == GlobalVar.UP_DIRECTION)
			return gradientEffectUpArray;
		else if (direction == GlobalVar.DOWN_DIRECTION)
			return gradientEffectDownArray;
		else
			return null;
	}

	/**
	 * @param gradientEffect
	 */
	public void add(GradientEffect gradientEffect) {
		Debug.print("I am in add gradientList EFFECT");
		int direction = gradientEffect.getDirection();
		ArrayList<GradientEffect> gradientEffects = getGradientEffectsFromDirection(direction);
		gradientEffects.add(gradientEffect);
	}

	@Override
	public boolean hasError() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getGradientValue() {
		return gradientValue;
	}

	public void setGradientValue(String gradientValue) {
		this.gradientValue = gradientValue;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public double getAccelerationChange() {
		return accelerationChange;
	}

	public void setAccelerationChange(double accelerationChange) {
		this.accelerationChange = accelerationChange;
	}

	public double getDecelerationChange() {
		return decelerationChange;
	}

	public void setDecelerationChange(double decelerationChange) {
		this.decelerationChange = decelerationChange;
	}

}
