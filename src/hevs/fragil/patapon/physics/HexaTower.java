package hevs.fragil.patapon.physics;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import hevs.fragil.patapon.drawables.SpriteSheet;

public class HexaTower extends Tower {
	
	public HexaTower(int x, int h) {
		super(x, h);
	}

	@Override
	public void loadFiles() {
		basis1 = new SpriteSheet("data/images/tower_hexa_basis.png", 1, 1, 50, 1, false, PlayMode.NORMAL);
		basis2 = new SpriteSheet("data/images/tower_hexa_basis2.png", 1, 1, 50, 1, false, PlayMode.NORMAL);
		head = new SpriteSheet("data/images/tower_hexa_head.png", 1, 1, 50, 1, false, PlayMode.NORMAL);
	}

}
