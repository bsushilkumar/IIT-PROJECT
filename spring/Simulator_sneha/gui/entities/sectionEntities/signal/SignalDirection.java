package gui.entities.sectionEntities.signal;

import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.trainEntities.Train;

public class SignalDirection {
	public Train train;
	public Block block;
	public int signal;
	public double arrivalTime;
	public Link previousLink = null;

	public SignalDirection(Train train, Block block, Link previousLink, int signal, double arrivalTime) {
		this.train = train;
		this.block = block;
		this.signal = signal;
		this.arrivalTime = arrivalTime;
		this.previousLink = previousLink;
	}

}
