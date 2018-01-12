package gui.entities.sectionEntityList;

import globalVariables.GlobalVar;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackProperties.SpeedRestriction;
import gui.entities.trainEntities.Train;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import simulator.util.Debug;

/**
 * 
 */
public class SpeedRestrictionList extends ArrayList<SpeedRestriction> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
	public SpeedRestrictionList() {
	}

	/**
	 * @param sp
	 */
	@Override
	public boolean add(SpeedRestriction sp) {
		if (this.size() == 0) {
			super.add(sp);
			Debug.print("SpeedRestrictionList: add: first speed restriction added");
			return true;
		}

		double currStartMileP = this.get(0).getRelativeStartMilePost();

		if (sp.getRelativeStartMilePost() < currStartMileP) {

			if (sp.getRelativeEndMilePost() > currStartMileP) {
				super.add(
						0,
						new SpeedRestriction(sp.getSpeedLimit(), sp
								.getRelativeStartMilePost(), currStartMileP));
			} else {
				super.add(
						0,
						new SpeedRestriction(sp.getSpeedLimit(), sp
								.getRelativeStartMilePost(), sp
								.getRelativeEndMilePost()));
			}
		}
		for (int i = 0; i < this.size(); i++) {
			// check disatance and spilt
			currStartMileP = this.get(i).getRelativeStartMilePost();
			double currEndMileP = this.get(i).getRelativeEndMilePost();
			if (sp.getRelativeStartMilePost() > currStartMileP) {
				if (sp.getRelativeEndMilePost() < currEndMileP) {
					double velo = this.get(i).getSpeedLimit();
					if (sp.getSpeedLimit() < velo) {
						super.remove(i);
						// restictions r being added inversly they will be
						// shifted down
						super.add(
								i,
								new SpeedRestriction(velo, sp
										.getRelativeEndMilePost(), currEndMileP));
						super.add(
								i,
								new SpeedRestriction(sp.getSpeedLimit(), sp
										.getRelativeStartMilePost(), sp
										.getRelativeEndMilePost()));
						super.add(i, new SpeedRestriction(velo, currStartMileP,
								sp.getRelativeStartMilePost()));
						Debug.print("speed res added in middle split in three");
					}
					break;
				}
				if (sp.getRelativeStartMilePost() < currEndMileP) {
					double velo = this.get(i).getSpeedLimit();
					if (sp.getSpeedLimit() < velo) {
						super.remove(i);
						super.add(i, new SpeedRestriction(velo, currStartMileP,
								sp.getRelativeStartMilePost()));
						super.add(
								i + 1,
								new SpeedRestriction(sp.getSpeedLimit(), sp
										.getRelativeStartMilePost(),
										currEndMileP));
						i = i + 1;
						Debug.print("speed res added at last");
					}
				}
			} else {
				if (sp.getRelativeEndMilePost() > currEndMileP) {
					double velo = this.get(i).getSpeedLimit();
					if (sp.getSpeedLimit() < velo) {
						this.get(i).setSpeedLimit(sp.getSpeedLimit());
						Debug.print("speed res was stricter than ther earlier");
					}
				} else {
					if (sp.getRelativeEndMilePost() > currStartMileP) {
						double velo = this.get(i).getSpeedLimit();
						if (sp.getSpeedLimit() < velo) {
							super.remove(i);
							super.add(
									i,
									new SpeedRestriction(sp.getSpeedLimit(),
											currStartMileP, sp
													.getRelativeEndMilePost()));
							super.add(
									i + 1,
									new SpeedRestriction(velo, sp
											.getRelativeEndMilePost(),
											currEndMileP));
							i = i + 1;
							Debug.print("speed res added at first");
						}
					}
					break;
				}
			}
		}
		double currEndMileP = this.get(this.size() - 1)
				.getRelativeStartMilePost();
		if (sp.getRelativeEndMilePost() > currEndMileP) {
			if (sp.getRelativeStartMilePost() < currEndMileP) {
				super.add(new SpeedRestriction(sp.getSpeedLimit(),
						currEndMileP, sp.getRelativeEndMilePost()));
			} else {
				super.add(new SpeedRestriction(sp.getSpeedLimit(), sp
						.getRelativeStartMilePost(), sp
						.getRelativeEndMilePost()));
			}
		}
		mergeSimilar();
		return true;
	}

	/**
     * 
     */
	public void mergeSimilar() {
		for (int i = 0; i < this.size() - 1; i++) {
			if (this.get(i).getSpeedLimit() == this.get(i + 1).getSpeedLimit()) {
				this.get(i).setRelativeEndMilePost(
						this.get(i + 1).getRelativeEndMilePost());
				super.remove(i + 1);
			}

		}
	}

	/**
	 * @param distance
	 * @return {@link ArrayList} of {@link SpeedRestrictionList}
	 */
	public ArrayList<SpeedRestrictionList> split(double distance) {
		ArrayList<SpeedRestrictionList> returnArray = new ArrayList<SpeedRestrictionList>();
		SpeedRestrictionList speedRestStart, speedRestEnd;
		speedRestStart = new SpeedRestrictionList();
		speedRestEnd = new SpeedRestrictionList();
		double currEndMileP, currStartMileP;
		SpeedRestriction sp;
		for (int i = 0; i < this.size(); i++) {
			sp = this.get(i);
			currStartMileP = sp.getRelativeStartMilePost();
			currEndMileP = sp.getRelativeEndMilePost();
			if (currStartMileP < distance) {
				if (currEndMileP <= distance) {
					speedRestStart.add(sp);
				} else {
					speedRestStart.add(new SpeedRestriction(sp.getSpeedLimit(),
							sp.getRelativeStartMilePost(), distance));
					speedRestEnd.add(new SpeedRestriction(sp.getSpeedLimit(),
							distance, sp.getRelativeEndMilePost()));
				}
			} else {
				speedRestEnd.add(this.get(i));
			}
		}

		returnArray.add(speedRestStart);
		returnArray.add(speedRestEnd);
		return returnArray;
	}

	// gradientList to be oincluded...
	/**
	 * @param i
	 * @return {@link SpeedRestriction}
	 */
	public SpeedRestriction get(int i) {
		SpeedRestriction sp, newSp;
		sp = super.get(i);
		newSp = new SpeedRestriction(sp.getSpeedLimit(),
				sp.getRelativeStartMilePost(), sp.getRelativeEndMilePost());
		// TODO
		// Train currentTrain = GlobalVar.getSimulationInstance().currentTrain;
		Train currentTrain = null;
		if (currentTrain != null) {
			if (i != 0) {
				if (this.get(i).getSpeedLimit() > this.get(i - 1)
						.getSpeedLimit()) {
					if (currentTrain.getDirection() == GlobalVar.UP_DIRECTION) {
						newSp.setRelativeStartMilePost(newSp
								.getRelativeStartMilePost()
								+ currentTrain.getLength());
					} else {
						newSp.setRelativeStartMilePost(newSp
								.getRelativeStartMilePost()
								- currentTrain.getLength());
					}
				}
			}
			if (i < this.size() - 1) {
				if (this.get(i).getSpeedLimit() < this.get(i + 1)
						.getSpeedLimit()) {
					if (currentTrain.getDirection() == GlobalVar.UP_DIRECTION) {
						newSp.setRelativeEndMilePost(newSp
								.getRelativeEndMilePost()
								+ currentTrain.getLength());
					} else {
						newSp.setRelativeEndMilePost(newSp
								.getRelativeEndMilePost()
								- currentTrain.getLength());
					}
				}
			}
		}

		return sp;
	}

	/**
	 * CODE BY CHANDRA
	 * 
	 * @param a
	 * @param b
	 * @return MaxSP
	 * **/
	public double returnMaxsp(double a, double b) {
		double tem = 0;
		Debug.print("CAME to returnmaxsp");
		for (int i = 0; i < this.size(); i++) {
			if ((a >= this.get(i).getRelativeStartMilePost())
					&& (b <= this.get(i).getRelativeEndMilePost())) {
				tem = this.get(i).getSpeedLimit();
				return tem;
			}
		}

		return tem;
	}

	public static void readSpeedRestriction(StreamTokenizer streamtokenizer,
			Block block) throws IOException {

		// System.out.println("SpeedRestrictionList: readSpeedRestriction: block "+block.blockNo);

		streamtokenizer.nextToken();
		String speedLimitsString = streamtokenizer.sval;

		streamtokenizer.nextToken();
		String speedLimitsStartMilePosts = streamtokenizer.sval;

		streamtokenizer.nextToken();
		String speedLimitsEndMilePosts = streamtokenizer.sval;

		StringTokenizer speedLimitsStringtokenizer = new StringTokenizer(
				speedLimitsString, GlobalVar.majorStringDelimiter);
		StringTokenizer relativeStartMilePostsStringtokenizer = new StringTokenizer(
				speedLimitsStartMilePosts, GlobalVar.majorStringDelimiter);
		StringTokenizer relativeEndMilePostsStringTokenizer = new StringTokenizer(
				speedLimitsEndMilePosts, GlobalVar.majorStringDelimiter);

		while (speedLimitsStringtokenizer.hasMoreTokens()) {

			String speedLimitString = speedLimitsStringtokenizer.nextToken();
			String relativeStartMilePostString = relativeStartMilePostsStringtokenizer
					.nextToken();
			String relativeEndMilePostString = relativeEndMilePostsStringTokenizer
					.nextToken();

			double speedLimit = Double.parseDouble(speedLimitString) / 60D;
			double relativeStartMilePost = Double
					.parseDouble(relativeStartMilePostString);
			double relativeEndMilePost = Double
					.parseDouble(relativeEndMilePostString);

			SpeedRestriction speedRestriction = new SpeedRestriction(
					speedLimit, relativeStartMilePost, relativeEndMilePost,
					block);

			block.getSpeedRestrictionList().add(speedRestriction);
		}

	}

}