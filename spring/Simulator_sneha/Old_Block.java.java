/**
	 * Consider the block occupancies and determine if the block is free during
	 * the interval specified by the startTime and endTime. If it is not free,
	 * return the ending time of the interval in which "startTime" lies. If the
	 * block is free, then check if the direction of the block, if specified by
	 * BlockdDirectionInInterval file does not change during the same interval.
	 * If it does change then return the ending time of the blockInterval in
	 * which startTime lies. If the direction of the block does not change
	 * during this interval then return -1.
	 * 
	 * @param startTime
	 * @param endTime
	 * @return time when the block is free.
	 */

/*	public double whenFree(double startTime, double endTime) {
		int i = isFree(startTime, endTime);
		if (i != -1) {
			return getOccupy().get(i).getEndTime();
		}

		// Devendra - to check if the block direction is compatible with train's
		// direction
		// during the interval
		if (this.getBlockDirectionInInterval() != null) {
			if (this.getBlockDirectionInInterval().blockIntervalArray != null) {
				BlockInterval startTimeBlockInterval = this
						.getBlockDirectionInInterval()
						.getBlockIntervalFromTime(startTime);
				BlockInterval endTimeBlockInterval = this
						.getBlockDirectionInInterval()
						.getBlockIntervalFromTime(endTime);
				if (startTimeBlockInterval != endTimeBlockInterval){
					return startTimeBlockInterval.getEndTime();
				}
			}
		}
		return -1D;
	}
*/