package gui.entities.sectionEntities;

public abstract class Entity {

	protected int entityType = -1;
	public static final int BLOCK_ENTITY = 0;
	public static final int LOOP_ENTITY = 1;
	public static final int LINK_ENTITY = 2;

	public abstract boolean hasError();

	public boolean isBlock() {
		return getEntityType() == BLOCK_ENTITY;
	}

	public boolean isLoop() {
		return getEntityType() == LOOP_ENTITY;
	}

	public boolean isLink() {
		return getEntityType() == LINK_ENTITY;
	}

	public int getEntityType() {
		return entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}
}
