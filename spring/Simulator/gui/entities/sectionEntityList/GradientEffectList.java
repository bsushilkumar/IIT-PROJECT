package gui.entities.sectionEntityList;

import gui.entities.sectionEntities.trackProperties.GradientEffect;

import java.util.ArrayList;

public class GradientEffectList extends ArrayList<GradientEffect> {
	public boolean add(GradientEffect gradientEffect) {
		for (GradientEffect gradientEffect2 : this) {
			if (gradientEffect.getGradientValue().equalsIgnoreCase(
					gradientEffect2.getGradientValue())) {
				
				// already present
				gradientEffect2.setAccelerationChange(gradientEffect
						.getAccelerationChange());
				gradientEffect2.setDecelerationChange(gradientEffect
						.getDecelerationChange());
				return true;
			}
		}

		// not already present
		return super.add(gradientEffect);
	}
}
