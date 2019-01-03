package me.goodandevil.skyblock.utils.world.block;

public enum BlockDegreesType {

	ROTATE_90(90F), ROTATE_180(180F), ROTATE_270(270F), ROTATE_360(360F);

	private float angle;

	BlockDegreesType(float angle) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}
}
