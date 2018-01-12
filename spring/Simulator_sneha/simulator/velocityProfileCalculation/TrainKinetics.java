package simulator.velocityProfileCalculation;

public class TrainKinetics {
	private double milepost;
	private double velocity;

	public TrainKinetics(double milepost, double velocity) {
		this.milepost = milepost;
		this.velocity = velocity;
	}

	public double getMilepost() {
		return milepost;
	}

	public void setMilepost(double milepost) {
		this.milepost = milepost;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
}
