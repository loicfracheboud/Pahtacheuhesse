package hevs.fragil.patapon.accessories;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ch.hevs.gdx2d.desktop.PortableApplication;
import ch.hevs.gdx2d.lib.GdxGraphics;
import ch.hevs.gdx2d.lib.interfaces.DrawableObject;
import ch.hevs.gdx2d.lib.utils.Logger;
import hevs.fragil.patapon.drawables.Clouds;
import hevs.fragil.patapon.drawables.EditorObject;
import hevs.fragil.patapon.drawables.Mountains;
import hevs.fragil.patapon.drawables.Tree;
import hevs.fragil.patapon.mechanics.Param;

public class MapEditor extends PortableApplication {
	Skin skin;
	Stage stage;
	Vector<TextButton> buttons = new Vector<TextButton>();
	Vector<EditorObject> availableObjects = new Vector<EditorObject>();
	List<EditorObject> mapObjects = new Vector<EditorObject>();
	float stateTime;
	Vector2 cursor = new Vector2(Gdx.input.getX(), Gdx.input.getY());
	boolean dropMode;
	EditorObject temp;
	
	public MapEditor(){
		super(1800,900);
	}
	public static void main(String[] args) {
		new MapEditor();
	}
	@Override
	public void onInit() {
		setTitle("SIGMAPI - 2016 - Broken Studios - Loïc Fracheboud, Loïc Gillioz, S2d - MAP EDITOR");

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);// Make the stage consume events

		// Load the default skin (which can be configured in the JSON file)
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
		
		//add instantiable classes into the list
		availableObjects.add(new Clouds());
		availableObjects.add(new Mountains());
		availableObjects.add(new Tree());
		availableObjects.add(new BasicTowerImage());
		availableObjects.add(new HexaTowerImage());
		
		for (EditorObject c : availableObjects) {
			addButton(c);
		}
	}
	@Override
	public void onKeyDown(int keycode) {
		super.onKeyDown(keycode);
		switch(keycode){
			case Keys.LEFT : stage.getCamera().translate(-10, 0, 0);
		}
	}
	private void addButton(EditorObject c) {
		buttons.add(new TextButton(c.getClass().getSimpleName(), skin));
		buttons.lastElement().setWidth(200);
		buttons.lastElement().setHeight(30);
		buttons.lastElement().setPosition(50, (buttons.size()+1)*50);
		stage.addActor(buttons.lastElement());
		buttons.lastElement().addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				try {
					//create a new object from the class
					temp = c.getClass().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
					
					Logger.log("Error instanciating new object.. Be sure the " + c.getClass().getSimpleName() + " class has a null constructor");
				}
				dropMode = true;
			}
		});
	}
	@Override
	public void onGraphicRender(GdxGraphics g) {
		cursor.set(Gdx.input.getX(), Gdx.input.getY());
		if(dropMode && temp != null){
			//true when last frame did receive a mouse click
			temp.place((int)cursor.x);
			if(Gdx.input.justTouched() && cursor.x > 300){
				mapObjects.add(temp);
				//objects comparison
				Comparator<ZObject> sorter = new ZComparator();
				//objects sort according to the sorter
				Collections.sort(mapObjects, sorter);
				temp = null;
				dropMode = false;
			}
		}
		g.clear(Param.BACKGROUND);
		for (DrawableObject d : mapObjects) {
			d.draw(g);
		}
		if(temp != null)temp.draw(g);
		g.setColor(Color.BLACK);
		g.drawFilledRectangle(150, getWindowHeight()/2, 300, getWindowHeight(), 0);
		g.setColor(Color.WHITE);
		g.drawString(150, getWindowHeight()-50, "Elements to add", Param.smallWhite, 1);
		g.drawString(150, 50, "Mouse : " + cursor, 1);
		g.drawFPS();
		//draw the floor
		g.drawFilledRectangle(1050,15,1500,30,0,Color.BLACK);
		// This is required for having the GUI work properly
		stage.act();
		stage.draw();
		
		g.setColor(Color.WHITE);
		g.drawFilledRectangle(getWindowWidth()/2+150, getWindowHeight()-90, 1500, 180, 0);
		
		stateTime += Gdx.graphics.getDeltaTime();
		g.drawStringCentered(870, "MAP EDITOR", Param.medium);
	}
	@Override
	public void onDispose() {
		super.onDispose();
		stage.dispose();
		skin.dispose();
	}
}
