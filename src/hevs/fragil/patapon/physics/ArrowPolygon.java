package hevs.fragil.patapon.physics;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon;
import ch.hevs.gdx2d.lib.physics.AbstractPhysicsObject;
import hevs.fragil.patapon.mechanics.Game;

public class ArrowPolygon extends PhysicsPolygon {
	int group;
	
	static Vector2 dimensions =  new Vector2(3,80);
	static int nArrows;
	static int startAngle = 60;
	static float[] v1 = {-5, 60, -4, 70, 0, 80, 4, 70, 5, 60, 0, 0};
	
	public ArrowPolygon(Vector2 position, int startAngle, int collisionGroup) {
		super("arrow"+nArrows, position, getArrowVertices(startAngle),  8f, 0f, 1f, true);
		this.getBody().setBullet(true);
		this.group = collisionGroup;
		nArrows++;
	}
	public ArrowPolygon(Vector2 position, int startAngle) {
		this(position, startAngle, -1);
	}
	
	@Override
	public void collision(AbstractPhysicsObject theOtherObject, float energy) {
		//Create a joint to stick the arrow
		Game.createWeldJoint(new StickyInfo(this.getBody(), theOtherObject.getBody(),getSpike()));
		//TODO change collisiongroup to match the victim group
	}
	public Vector2 getSpike() {
		Vector2 temp = getBodyWorldCenter();
		double angle = getBodyAngle() + Math.PI/3;
		temp.add((float)(Math.cos(angle)*28), (float)(Math.sin(angle)*28));
		return temp;
	}
	private static Vector2[] getArrowVertices(int angle){
		Polygon poly = new Polygon(v1);
		poly.setOrigin(0, 40);
		poly.rotate(startAngle - 90);
		return verticesToVector2(poly.getTransformedVertices());
	}
	/**
	 * Convert vertices to Vector2's
	 * @param vertices array to convert
	 * @return Vector2[] converted array
	 */
	private static Vector2[] verticesToVector2(float[] vertices){
		if(vertices.length % 2 == 0){
			Vector2[] temp = new Vector2[vertices.length/2];
			int j = 0;
			for(int i = 0 ; i < vertices.length/2 ; i++){
				temp[i] = new Vector2(vertices[j], vertices[++j]);
				j++;
			}
			return temp;
		}
		else{
			return null;
		}
	}
}