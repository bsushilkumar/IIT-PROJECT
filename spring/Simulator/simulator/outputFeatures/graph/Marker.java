package simulator.outputFeatures.graph;

/**
 * marker
 */
public class Marker {
	/**
	 * markX
	 */
	double markX;
	/**
	 * markY
	 */
	double markY;
	/**
	 * name
	 */
	String name = "";

	/**
	 * 
	 */
	public Marker() {
	}

	/**
	 * @param x
	 * @param y
	 * @param name
	 */
	public Marker(double x, double y, String name) {
		this.markX = x;
		this.markY = y;
		this.name = name;
	}

	/**
	 * @param x
	 * @param y
	 */
	public Marker(double x, double y) {
		this.markX = x;
		this.markY = y;
	}

	/**
	 * @return markX
	 */
	public double getX() {
		return markX;
	}

	/**
	 * @return markY
	 */
	public double getY() {
		return markY;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

}
