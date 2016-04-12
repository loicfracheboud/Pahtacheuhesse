package hevs.fragil.patapon.drawables;

import hevs.fragil.patapon.others.Param;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.interfaces.DrawableObject;
import com.badlogic.gdx.graphics.Color;

public class BlinkingBorder implements DrawableObject{
	static boolean display = true;
	private int frames = 0;
	public static boolean blinkEnable = false;
	Color frameColor = new Color(Color.WHITE);

	@Override
	public void draw(GdxGraphics g) {
		if(blinkEnable){
			int width = g.getScreenWidth();
			int height = g.getScreenHeight();
			int thickness = 10;
			
			//1:up, 2:right, 3:down, 4:left
			float[] x = {width/2, width-thickness/2, width/2, thickness/2};
			float[] y = {height-thickness/2, height/2, thickness/2, height/2};
			
			//linearly graduates to backColor
			float stepsLeft = Param.FRAME_DEGRADE_STEPS - frames;
			frameColor = frameColor.lerp(Param.BACKGROUND, 1/stepsLeft);
			
			//rotation in degrees = i*90 			
			for(int i = 0; i < x.length; i++){
				g.drawFilledRectangle(x[i], y[i], width, thickness, i*90, frameColor);
			}
			frames++;
			if(frames == Param.FRAME_DEGRADE_STEPS){
				blinkEnable = false;
				frameColor.set(Color.WHITE);
				frames = 0;
			}
		}
	}
}