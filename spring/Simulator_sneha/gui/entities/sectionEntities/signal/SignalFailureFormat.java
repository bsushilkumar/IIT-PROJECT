package gui.entities.sectionEntities.signal;

/**
 * 
 */
public class SignalFailureFormat {
	/**
	 * blockNo
	 */
	private int blockNo;
	/**
	 * start
	 */
	private double start;
	/**
	 * end
	 */
	private double end;

	/**
 * 
 */
	public SignalFailureFormat() {
	}

	/**
	 * @param blockNumber
	 * @param startTime
	 * @param endTime
	 */
	public SignalFailureFormat(int blockNumber, double startTime, double endTime) {
		setBlockNo(blockNumber);
		setStart(startTime);
		setEnd(endTime);
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public int getBlockNo() {
		return blockNo;
	}

	public void setBlockNo(int blockNo) {
		this.blockNo = blockNo;
	}

}