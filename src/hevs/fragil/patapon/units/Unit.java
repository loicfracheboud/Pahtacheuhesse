package hevs.fragil.patapon.units;
import com.badlogic.gdx.math.Vector2;

import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.interfaces.DrawableObject;
import hevs.fragil.patapon.drawables.SpriteSheet;
import hevs.fragil.patapon.mechanics.Param;
import hevs.fragil.patapon.physics.BodyPolygon;

public abstract class Unit implements DrawableObject{
	static int nUnits;
	
	//Skills
	protected Skills skills;
	protected float timeCounter;
	protected int level = 1;
	protected Species species = Species.TAPI;
	protected Expression expression = Expression.DEFAULT;
	protected int collisionGroup;
	
	private BodyPolygon hitBox;
	
	//Drawables
	private static SpriteSheet legs;
	private int frameIndex;
	private SpriteSheet body, eye;
	
	Unit(int lvl, Species species, int attack, int defense, int life, int distance, int range, int cooldown, int collisionGroup){
		this.species = species;
		this.level = lvl;
		this.skills = new Skills(life+level*5, attack, range, cooldown/(lvl+1));
		this.collisionGroup = collisionGroup;
		nUnits++;
	}	
	public void setPosition(int newPos, double totalTime){
		if(hitBox != null)
			hitBox.moveToLinear(newPos, totalTime);
		else
			hitBox = new BodyPolygon(new Vector2(newPos, Param.FLOOR_DEPTH), collisionGroup);
	}
	protected int getPosition(){
		return (int)hitBox.getBodyWorldCenter().x;
	}
	protected void setLife(int life){
		this.skills.setLife(life);
	}
	public String toString(){
		return ", Level : "+ level + ", Life : " + skills.getLife();
	}
	public void attack(float dt){
		timeCounter += dt;
		if(timeCounter > skills.getCooldown()){
			attack();
			timeCounter = 0f;
		}
	}
	public abstract void attack();
	public abstract void draw(GdxGraphics g, float time);
	protected void drawLegs(float stateTime){
		frameIndex = legs.drawKeyFrames(stateTime, getPosition()-32);
	}
	protected void drawBody(float stateTime){
		body.drawWalkAnimation(frameIndex, (4*(species.ordinal()))+(level), getPosition()-32, 40);
	}
	protected void drawEye(){
		//TODO get unit state to change the eye expression
		eye.drawWalkAnimation(frameIndex, 1, getPosition()-32, 52);
	}
	/**
	 * This is only to load files in the PortableApplication onInit method
	 */
	public static void setLegsSprite(String url, int cols, int rows){
		legs = new SpriteSheet(url, cols , rows, 0.2f);
	}
	/**
	 * This is only to load files in the PortableApplication onInit method
	 */
	public void setBodySprite(String url, int cols, int rows) {
		body = new SpriteSheet(url, cols , rows, 0.2f);		
	}
	/**
	 * This is only to load files in the PortableApplication onInit method
	 */
	public void setEyeSprite(String url, int cols, int rows) {
		eye = new SpriteSheet(url, cols , rows, 0.2f);		
	}
	public void setDelay(int delay) {
		skills.setCooldown(delay);
	}
	public int getDelay() {
		return skills.getCooldown();
	}
	
}
