package simulator.velocityProfileCalculation;

import globalVariables.GlobalVar;
import gui.diagramEntities.trainDiagrams.VelocityProfile;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackProperties.TinyBlock;
import gui.entities.sectionEntities.trackProperties.TinyBlockList;
import gui.entities.trainEntities.Train;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import simulator.input.SortTinyBlock;
import simulator.util.Debug;

/**
 * Class VelocityProfileArray
 */
@SuppressWarnings({ "serial" })
public class VelocityProfileArray extends ArrayList<VelocityProfile> {

	/**
	 * runTime
	 */
	private double runTime = 0;

	/**
	 * constructor.
	 */
	public VelocityProfileArray() {
	}

	/**
	 * 1) It calculates velocity of the train for every tinyblock in the middle
	 * of the blocks. Depending upon the direction of the train it can predict
	 * the velocity of the train in a particular region considering the speed
	 * restrictions and newtonian laws of motion. It uses Max_forward and
	 * Max_back arrays of velocities to handle the various speeds of the train
	 * in the tinyBlockRegions depending upon the direction of the train in
	 * forward or backward direction. 2) After calculating the velocities at the
	 * end of the tinyBlocks it calls calVelocity(first1, last1, (accel +
	 * tempTinyBlock.accelerationChange), (deccel +
	 * tempTinyBlock.decelerationChange), tempTinyBlockSpeed,
	 * currentBlockStartMilePost, tempTinyBlock.endMilePost); with proper
	 * parameters to finetune the acceleration and deceleration parts of the
	 * tinyBlockList. calVelocity returns a velocityProfileArray consisting of 3
	 * parts accelerating, constant velocity and decelerating part which is then
	 * added to the main VelocityProfileArray of the constructor.
	 * 
	 * @param simulatorTrain
	 * @param currBlock
	 * @param startVelocity
	 * @param finalVelocity
	 */
	public VelocityProfileArray(Train simulatorTrain, Block currBlock,
			double startVelocity, double finalVelocity) {

		Debug.fine("VelocityProfileArray: Train " + simulatorTrain.getTrainNo()
				+ " " + currBlock.getBlockName() + " startVelocity "
				+ startVelocity + " finalVelocity " + finalVelocity);

		TinyBlockList currentBlockTinyBlockArray = currBlock.getTinyBlockList();
		Collections.sort(currentBlockTinyBlockArray, new SortTinyBlock());

		VelocityProfileArray tempVeloProfileArray = new VelocityProfileArray();

		for (int i = 0; i < currentBlockTinyBlockArray.size(); i++) {
			TinyBlock tempTinyBlock = currentBlockTinyBlockArray.get(i);

			Debug.fine("VelocityProfileArray: tinyBlock is	"
					+ tempTinyBlock.getStartMilePost() + "	"
					+ tempTinyBlock.getEndMilePost() + "	"
					+ tempTinyBlock.getMaxSpeed() + "	"
					+ tempTinyBlock.getAccelerationChange() + "	"
					+ tempTinyBlock.getDecelerationChange() + "   ");
		}

		int currentBlockTinyBlockArraySize = currentBlockTinyBlockArray.size();
		if (currentBlockTinyBlockArraySize == 0) {
			Debug.error("VelocityProfileArray: No tiny blocks error for train "
					+ simulatorTrain.getTrainNo() + " "
					+ currBlock.getBlockName());
			return;
		}

		double max_forward[] = new double[currentBlockTinyBlockArraySize + 1];
		double max_back[] = new double[currentBlockTinyBlockArraySize + 1];
		double acceleration[] = new double[currentBlockTinyBlockArraySize];
		double deceleration[] = new double[currentBlockTinyBlockArraySize];

		if (simulatorTrain.getDirection() == GlobalVar.UP_DIRECTION) {
			max_forward[0] = startVelocity;
			max_back[currentBlockTinyBlockArraySize] = finalVelocity;
		} else {
			max_forward[0] = finalVelocity;
			max_back[currentBlockTinyBlockArraySize] = startVelocity;
		}

		Debug.print("VelocityProfileArray: NEW PROFILE WITH THE NEW FUNCTION ");

		int iteration = 1;

		TinyBlock nextTinyBlock = new TinyBlock();
		TinyBlock previousTinyBlock = new TinyBlock();

		nextTinyBlock = currentBlockTinyBlockArray.get(0);

		double u;// u, v, a, s, d;
		while (iteration < currentBlockTinyBlockArraySize + 1) {
			if (iteration < currentBlockTinyBlockArraySize)
				nextTinyBlock = currentBlockTinyBlockArray.get(iteration);
			else
				nextTinyBlock = null;
			previousTinyBlock = currentBlockTinyBlockArray.get(iteration - 1);

			u = max_forward[iteration - 1];
			setTrainCharacteristicsForTinyBlock(max_forward, acceleration,
					deceleration, iteration, nextTinyBlock, previousTinyBlock,
					simulatorTrain, u, false);
			iteration++;
		}

		Debug.print("VelocityProfileArray: backward calculation");

		iteration = currentBlockTinyBlockArraySize - 1;

		while (iteration >= 0) {
			previousTinyBlock = currentBlockTinyBlockArray.get(iteration);
			if (iteration != 0)
				nextTinyBlock = currentBlockTinyBlockArray.get(iteration - 1);
			else
				nextTinyBlock = null;

			u = max_back[iteration + 1];
			setTrainCharacteristicsForTinyBlock(max_back, acceleration,
					deceleration, iteration, nextTinyBlock, previousTinyBlock,
					simulatorTrain, u, true);
			iteration--;
		}

		Debug.print("VelocityProfileArray: velocity profile calculation   ");

		double tempTinyBlockSpeed;
		double firstVelocity = 0, lastVelocity = 0;

		for (int i = 0; i < currentBlockTinyBlockArraySize; i++) {

			TinyBlock tempTinyBlock = currentBlockTinyBlockArray.get(i);
			tempTinyBlockSpeed = tempTinyBlock.getMaxSpeed();
			if (tempTinyBlockSpeed <= 0)
				tempTinyBlockSpeed = simulatorTrain.getMaximumPossibleSpeed();

			tempTinyBlockSpeed = Math.min(tempTinyBlockSpeed,
					simulatorTrain.getMaximumPossibleSpeed());

			firstVelocity = Math.min(max_forward[i], max_back[i]);

			lastVelocity = Math.min(max_forward[i + 1], max_back[i + 1]);

			tempVeloProfileArray = calVelocity(firstVelocity, lastVelocity,
					acceleration[i], deceleration[i], tempTinyBlockSpeed,
					tempTinyBlock.getStartMilePost(),
					tempTinyBlock.getEndMilePost());

			for (int j = 0; j < tempVeloProfileArray.size(); j++) {
				this.add(tempVeloProfileArray.get(j));
			}

		}

		for (VelocityProfile vp : this) {
			Debug.fine("VelocityProfileArray: s " + vp.getStartMilePost()
					+ " e " + vp.getEndMilePost() + " sv "
					+ vp.getStartVelocity() + " ev " + vp.getEndVelocity()
					+ " t " + vp.getTotalTime() + " a " + vp.getAcceleration());
		}
	}

	private void setTrainCharacteristicsForTinyBlock(double[] max_forward,
			double[] acceleration, double[] deceleration, int iteration,
			TinyBlock nextTinyBlock, TinyBlock previousTinyBlock,
			Train simulatorTrain, double startVelocity,
			boolean calculatingBackwards) {

		double previousTinyMaxSpeed = 0;
		if (previousTinyBlock != null)
			previousTinyMaxSpeed = previousTinyBlock.getMaxSpeed();
		if (previousTinyMaxSpeed <= 0)
			previousTinyMaxSpeed = simulatorTrain.getMaximumPossibleSpeed();

		double nextTinyMaxSpeed = 0;
		if (nextTinyBlock != null)
			nextTinyMaxSpeed = nextTinyBlock.getMaxSpeed();
		if (nextTinyMaxSpeed <= 0)
			nextTinyMaxSpeed = simulatorTrain.getMaximumPossibleSpeed();

		double u = startVelocity, v, a, d, s;

		if (simulatorTrain.getDirection() == GlobalVar.UP_DIRECTION) {
			a = (previousTinyBlock.getAccelerationChange() + simulatorTrain
					.getAccParam());
			d = (previousTinyBlock.getDecelerationChange() + simulatorTrain
					.getDeceParam());
		} else {
			a = (previousTinyBlock.getDecelerationChange() + simulatorTrain
					.getDeceParam());
			d = (previousTinyBlock.getAccelerationChange() + simulatorTrain
					.getAccParam());
		}

		a = Math.max(a, 0);
		d = Math.max(d, 0);
		s = previousTinyBlock.getEndMilePost()
				- previousTinyBlock.getStartMilePost();

		double vsquare;
		if (!calculatingBackwards) {
			vsquare = u * u + 2 * a * s;
		} else {
			vsquare = u * u + 2 * d * s;
		}

		v = Math.sqrt(vsquare);

		v = Math.min(v, nextTinyMaxSpeed);
		v = Math.min(v, previousTinyMaxSpeed);
		v = Math.min(v, simulatorTrain.getMaximumPossibleSpeed());

		max_forward[iteration] = v;

		if (!calculatingBackwards) {
			acceleration[iteration - 1] = a;
			deceleration[iteration - 1] = d;
		}
	}

	/**
	 * overriding method for add.
	 * 
	 * @param obj
	 * @return true if the obj was added.
	 */

	public boolean add(VelocityProfile velocityProfile) {
		setRunTime(getRunTime() + velocityProfile.getTotalTime());
		if (velocityProfile.getTotalTime() == 0.0) {
			return true;
		}
		return super.add(velocityProfile);
	}

	/**
	 * @param k
	 * @param obj
	 * 
	 */
	public void add(int k, VelocityProfile obj) {
		VelocityProfile vProf = obj;
		setRunTime(getRunTime() + vProf.getTotalTime());
		super.add(k, vProf);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#remove(int)
	 */
	public VelocityProfile remove(int i) {
		Debug.print(" In remove function " + i);
		VelocityProfile vProf = super.remove(i);
		setRunTime(getRunTime() - vProf.getTotalTime());
		return vProf;
	}

	/**
	 * @param initVelocity
	 * @param finalVelocity
	 * @param accel
	 * @param deccel
	 * @param maxVelocity
	 * @param startMileP
	 * @param endMileP
	 * @return {@link VelocityProfileArray} calculated after feeding all the
	 *         parameters
	 */
	public static VelocityProfileArray calVelocity(double initVelocity,
			double finalVelocity, double acceleration, double deceleration,
			double maxVelocity, double startMileP, double endMileP) {

		// System.out.println("VelocityProfileArray: calVelocity: startvelo "
		// + initVelocity + " finalVelo " + finalVelocity
		// + " maxvelocity " + maxVelocity + "  acc  " + accel
		// + " deccel " + deccel + " startMilePost " + startMileP
		// + " endMilePost " + endMileP);

		double totalTime = 0;
		VelocityProfileArray velocityProfileArray = new VelocityProfileArray();

		double length = Math.abs(endMileP - startMileP);
		double minMilePost = Math.min(startMileP, endMileP);
		double maxMilePost = Math.max(startMileP, endMileP);

		double startDistance = 0, endDistance = 0, initEndDistance = 0, initStartDistance = 0;

		// cal distance to reach max velocity from initVElocity
		if (maxVelocity >= initVelocity)
			startDistance = (Math.pow(maxVelocity, 2) - Math.pow(initVelocity,
					2)) / (2 * acceleration);

		// cal distance to reach final velocity from max velocity
		if (maxVelocity >= finalVelocity)
			endDistance = (Math.pow(finalVelocity, 2) - Math
					.pow(maxVelocity, 2)) / (2 * (-deceleration));

		initEndDistance = (Math.pow(finalVelocity, 2) - Math.pow(initVelocity,
				2)) / (2 * (-deceleration));

		initStartDistance = (Math.pow(finalVelocity, 2) - Math.pow(
				initVelocity, 2)) / (2 * acceleration);

		// check distance
		if (startDistance + endDistance > length) {

			if (initEndDistance > length) {

				// cal start velo and total time
				double startVelo = Math.sqrt(Math.abs(Math
						.pow(finalVelocity, 2) - 2 * -deceleration * length));
				double startTime = (finalVelocity - startVelo)
						/ (-deceleration);
				velocityProfileArray.add(new VelocityProfile(minMilePost,
						maxMilePost, startVelo, finalVelocity, startTime,
						-deceleration));
			} else if (initStartDistance > length) {

				// cal final velo and total time
				double endVelo = Math.sqrt(Math.pow(initVelocity, 2) + 2
						* acceleration * length);
				double startTime = (endVelo - initVelocity) / (acceleration);
				velocityProfileArray.add(new VelocityProfile(minMilePost,
						maxMilePost, initVelocity, endVelo, startTime,
						acceleration));
			} else {
				// System.out.println("initStartDistance < length");
				startDistance = (Math.pow(finalVelocity, 2)
						- Math.pow(initVelocity, 2) - (2 * (-deceleration) * length))
						/ (2 * (acceleration - (-deceleration)));
				endDistance = length - startDistance;
				double interimVelo = Math.sqrt(Math.pow(initVelocity, 2) + 2
						* acceleration * startDistance);

				// calculate time
				double startTime = (interimVelo - initVelocity)
						/ (acceleration);// mj
				velocityProfileArray.add(new VelocityProfile(minMilePost,
						minMilePost + startDistance, initVelocity, interimVelo,
						startTime, acceleration));

				double endTime = (finalVelocity - interimVelo)
						/ ((-deceleration));// mj
				velocityProfileArray.add(new VelocityProfile(minMilePost
						+ startDistance, maxMilePost, interimVelo,
						finalVelocity, endTime, -deceleration));

				totalTime = startTime + endTime;

			}
		} else {
			double startTime = (maxVelocity - initVelocity) / (acceleration);
			double middleTime = (length - startDistance - endDistance)
					/ maxVelocity;
			double endTime = (finalVelocity - maxVelocity) / ((-deceleration));// mj

			if (startTime > 0)
				velocityProfileArray.add(new VelocityProfile(minMilePost,
						minMilePost + startDistance, initVelocity, maxVelocity,
						startTime, acceleration));

			if (middleTime > 0) {
				velocityProfileArray.add(new VelocityProfile(minMilePost
						+ startDistance, maxMilePost - endDistance,
						maxVelocity, maxVelocity, middleTime, 0));
			}

			if (endTime > 0) {
				velocityProfileArray.add(new VelocityProfile(maxMilePost
						- endDistance, maxMilePost, maxVelocity, finalVelocity,
						endTime, -deceleration));
			}

			totalTime = startTime + endTime + middleTime;
		}

		// velocityProfileArray.print();
		// System.out.println();

		return velocityProfileArray;
		// calculate the total time to reach there

	}

	/**
	 * VelocityProfileArray: VelocityProfileArray returnInterval(double
	 * startDistance, double endDistance) 1) It runs through the
	 * velocityProfiles in the current velocityProfile and keeps on adding time
	 * for each of the velocityProfile. 2) It keeps adding the velocityProfiles
	 * in the current velocityProfile to a new blockVelocityProfileArray so that
	 * it can get the velocity descriptions at the beginning, middle and end of
	 * this particular current Block. If the GlobalVar.overlap is true:
	 * velocityprofilearray1 = velocityprofilearray.returnInterval(
	 * GlobalVar.overlapStartDistance, GlobalVar.overlapEndDistance); return new
	 * RunTimeReturn(velocityprofilearray1.runTime, 0.0D,
	 * velocityprofilearray1);
	 * 
	 * @param startDistance
	 * @param endDistance
	 * @return {@link VelocityProfileArray}
	 */
	public VelocityProfileArray returnInterval(double milepost1,
			double milepost2) {
		double startDistance = Math.min(milepost1, milepost2);
		double endDistance = Math.max(milepost1, milepost2);

		Debug.fine("returnInterval: startDistance " + startDistance
				+ " endDistance " + endDistance);

		VelocityProfileArray blkVeloProfileArray = new VelocityProfileArray();
		double totalTime = 0;

		for (int currProfile = 0; currProfile < size(); currProfile++) {
			VelocityProfile currVeloProfile = get(currProfile);

			Debug.fine("returnInterval: VelocityProfile smp "
					+ currVeloProfile.getStartMilePost() + " emp  "
					+ currVeloProfile.getEndMilePost() + " sv "
					+ currVeloProfile.getStartVelocity() + " ev "
					+ currVeloProfile.getEndVelocity() + " t "
					+ currVeloProfile.getTotalTime() + " a "
					+ currVeloProfile.getAcceleration());
		}

		Debug.fine("returnInterval: VeloProfile should be cut between start "
				+ startDistance + " end " + endDistance);
		for (int currProfile = 0; currProfile < size(); currProfile++) {

			VelocityProfile currVeloProfile = get(currProfile);

			if (currVeloProfile.getStartMilePost() < endDistance) {
				if (currVeloProfile.getStartMilePost() >= startDistance) {
					if (currVeloProfile.getEndMilePost() <= endDistance) {
						totalTime += currVeloProfile.getTotalTime();
						blkVeloProfileArray.add(get(currProfile));
					} else {
						VelocityProfile veloProfile = get(currProfile);
						double endVelo = Math
								.sqrt(Math.pow(veloProfile.getStartVelocity(),
										2)
										+ 2
										* veloProfile.getAcceleration()
										* (endDistance - veloProfile
												.getStartMilePost()));

						double endTime;
						if (veloProfile.getAcceleration() == 0) {
							endTime = (endDistance - veloProfile
									.getStartMilePost()) / endVelo;
						} else {
							endTime = (endVelo - veloProfile.getStartVelocity())
									/ (veloProfile.getAcceleration());
						}

						totalTime += endTime;
						VelocityProfile newVelocityProfile = new VelocityProfile(
								veloProfile.getStartMilePost(), endDistance,
								veloProfile.getStartVelocity(), endVelo,
								endTime, veloProfile.getAcceleration());
						newVelocityProfile.setTrackSegment(veloProfile
								.getTrackSegment());
						blkVeloProfileArray.add(newVelocityProfile);
						// /check this???
					}
				} else {
					if (currVeloProfile.getEndMilePost() >= startDistance) {
						double startVelo = Math
								.sqrt(Math.pow(
										currVeloProfile.getEndVelocity(), 2)
										- 2
										* currVeloProfile.getAcceleration()
										* (currVeloProfile.getEndMilePost() - startDistance));
						double startTime;

						if (currVeloProfile.getAcceleration() == 0) {
							startTime = (currVeloProfile.getEndMilePost() - startDistance)
									/ startVelo;
						} else {
							startTime = (currVeloProfile.getEndVelocity() - startVelo)
									/ (currVeloProfile.getAcceleration());// mj
						}
						totalTime += startTime;
						VelocityProfile newVelocityProfile = new VelocityProfile(
								startDistance,
								currVeloProfile.getEndMilePost(), startVelo,
								currVeloProfile.getEndVelocity(), startTime,
								currVeloProfile.getAcceleration());
						newVelocityProfile.setTrackSegment(currVeloProfile
								.getTrackSegment());
						blkVeloProfileArray.add(newVelocityProfile);

					}
				}
			}
		}

		blkVeloProfileArray.setRunTime(totalTime);
		for (VelocityProfile velocityProfile : blkVeloProfileArray) {
			Debug.fine("returnInterval:  " + velocityProfile.getStartMilePost()
					+ "   " + velocityProfile.getEndMilePost() + "  "
					+ velocityProfile.getStartVelocity() + "  "
					+ velocityProfile.getEndVelocity());
		}
		return blkVeloProfileArray;
	}

	/**
	 * @param veloProfArray
	 */

	public void addAll(VelocityProfileArray veloProfArray) {
		addAll((ArrayList<VelocityProfile>) veloProfArray);
		setRunTime(getRunTime() + veloProfArray.getRunTime());
	}

	/**
	 * @param simulatorTrain
	 * @return the endVelocity of the velocity profile array depending upon the
	 *         direction of the train
	 */
	public double getEndVelocity(Train simulatorTrain) {
		if (this.size() != 0) {
			if (simulatorTrain.getDirection() == GlobalVar.UP_DIRECTION) {

				return (this.get(this.size() - 1)).getEndVelocity();
			} else {

				return (this.get(0)).getStartVelocity();
			}
		} else {
			return 0.0;
		}

	}

	/**
	 * @param simulatorTrain
	 * @return the startVelocity of the velocity profile array depending upon
	 *         the direction of the train
	 */
	public double getStartVelocity(Train simulatorTrain) {
		if (this.size() != 0) {
			if (simulatorTrain.getDirection() == 0) {

				return (this.get(0)).getStartVelocity();
			} else {

				return (this.get(this.size() - 1)).getEndVelocity();
			}
		} else {
			return 0.0;
		}

	}

	public void print(int trainDirection) {
		for (VelocityProfile VelocityProfile : this) {
			VelocityProfile.print(trainDirection);
		}
	}

	public void remove(double startMilePost, double endMilePost) {
		int index = 0;
		while (this.size() > 0 && index < this.size()) {
			VelocityProfile vp = this.get(index);
			if (vp.containsMilePost(startMilePost)
					|| vp.containsMilePost(endMilePost)) {
				this.remove(index);
			} else
				index++;
		}
	}

	public double getRunTime() {
		return runTime;
	}

	public void setRunTime(double runTime) {
		this.runTime = runTime;
	}

	public void sort(Train train) {
		Collections.sort(this, new VelocityProfileSorter(train));
	}

	public void sort() {
		Collections.sort(this, new VelocityProfileSorter());
	}

	public double getStartMilePost(int trainDirection) {
		// assumption the velocityProfileArray is already sorted

		if (this.size() > 0) {
			VelocityProfile firstVelocityProfile;
			if (trainDirection == GlobalVar.UP_DIRECTION) {
				firstVelocityProfile = this.get(0);
			} else {
				firstVelocityProfile = this.get(this.size() - 1);
			}

			return firstVelocityProfile.getStartMilePost(trainDirection);
		}

		return Double.NEGATIVE_INFINITY;
	}

	public double getEndMilePost(int trainDirection) {
		// assumption the velocityProfileArray is already sorted
		if (this.size() > 0) {
			VelocityProfile lastVelocityProfile;
			if (trainDirection == GlobalVar.UP_DIRECTION) {
				lastVelocityProfile = this.get(this.size() - 1);
			} else {
				lastVelocityProfile = this.get(0);
			}

			return lastVelocityProfile.getEndMilePost(trainDirection);
		}

		return Double.NEGATIVE_INFINITY;
	}

	public TrainKinetics getTrainKineticsByTime(double time,
			int trainDirection, Block block) {
		int i, change;
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			i = 0;
			change = 1;
		} else { // trainDirection is down direction
			i = this.size() - 1;
			change = -1;
		}

		TrainKinetics trainKinetics = null;
		double arrivalTime = 0, departureTime;
		for (; 0 <= i && i < this.size(); i = i + change) {
			VelocityProfile velocityProfile = this.get(i);
			departureTime = arrivalTime + velocityProfile.getTotalTime();

			if (arrivalTime <= time && time <= departureTime) {
				double t = time - arrivalTime, u, a = velocityProfile
						.getAcceleration(), S, milepost, s;
				if (trainDirection == GlobalVar.UP_DIRECTION) {
					u = velocityProfile.getStartVelocity();
					S = velocityProfile.getStartMilePost();
					s = u * t + a * t * t / 2.0;
					milepost = S + s;
				} else {
					S = velocityProfile.getEndMilePost();
					u = velocityProfile.getEndVelocity();
					a = -a;
					s = u * t + a * t * t / 2.0;
					milepost = S - s;
				}

				// standard kinematic equations
				double velocity = u + a * t;
				Debug.fine("getTrainKineticsByTime: S " + S + " u " + u + " a "
						+ a + " t " + t + " s " + s + " v " + velocity);
				trainKinetics = new TrainKinetics(milepost, velocity);
				break;
			}

			arrivalTime = departureTime;
		}

		if (trainKinetics == null)
			return null;
		double milepost = trainKinetics.getMilepost(), correctedMilePost;
		double sightingDistance = GlobalVar.sightingDistance;

		if (trainDirection == GlobalVar.UP_DIRECTION) {
			correctedMilePost = Math.max(block.getEndMilePost()
					- sightingDistance, milepost);
			i = 0;
		} else {
			correctedMilePost = Math.min(milepost, block.getStartMilePost()
					+ sightingDistance);
			i = this.size() - 1;
		}

		if (milepost == correctedMilePost) {
			return trainKinetics;
		} else {
			Debug.fine("getTrainKineticsByTime: correcting milepost "
					+ milepost + " to correctedMilepost " + correctedMilePost);
			milepost = correctedMilePost;
		}

		for (; 0 <= i && i < this.size(); i = i + change) {
			VelocityProfile velocityProfile = this.get(i);
			double startMilePost = velocityProfile.getStartMilePost();
			double endMilePost = velocityProfile.getEndMilePost();

			if (startMilePost <= milepost && milepost <= endMilePost) {
				double u = velocityProfile.getStartVelocity();
				double a = velocityProfile.getAcceleration();
				double v;
				if (a == 0) {
					v = u;
				} else {
					double s = milepost - startMilePost;
					double vSquare = u * u + 2 * a * s;
					v = Math.sqrt(vSquare);
				}

				trainKinetics = new TrainKinetics(milepost, v);
				break;
			}
		}

		return trainKinetics;
	}
}

class VelocityProfileSorter implements Comparator<VelocityProfile> {
	Train train = null;

	public VelocityProfileSorter(Train train) {
		this.train = train;
	}

	public VelocityProfileSorter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(VelocityProfile velocityProfile1,
			VelocityProfile velocityProfile2) {
		if (train == null) {
			if (velocityProfile1.getArrivalTime() < velocityProfile2
					.getArrivalTime())
				return -1;
			else if (velocityProfile1.getArrivalTime() > velocityProfile2
					.getArrivalTime())
				return 1;
			else {
				if (velocityProfile1.getDepartureTime() < velocityProfile2
						.getDepartureTime())
					return -1;
				else if (velocityProfile1.getDepartureTime() > velocityProfile2
						.getDepartureTime())
					return 1;
				else
					return 0;
			}
		}

		if (train.getDirection() == GlobalVar.UP_DIRECTION) {

			if (velocityProfile1.getStartMilePost() < velocityProfile2
					.getStartMilePost())
				return -1;
			else if (velocityProfile1.getStartMilePost() > velocityProfile2
					.getStartMilePost())
				return 1;
			else if (velocityProfile1.getEndMilePost() < velocityProfile2
					.getEndMilePost())
				return -1;
			else if (velocityProfile1.getEndMilePost() > velocityProfile2
					.getEndMilePost())
				return 1;
		} else {
			if (velocityProfile1.getStartMilePost() > velocityProfile2
					.getStartMilePost())
				return -1;
			else if (velocityProfile1.getStartMilePost() < velocityProfile2
					.getStartMilePost())
				return 1;
			else if (velocityProfile1.getEndMilePost() > velocityProfile2
					.getEndMilePost())
				return -1;
			else if (velocityProfile1.getEndMilePost() < velocityProfile2
					.getEndMilePost())
				return 1;
		}

		return 0;
	}
}