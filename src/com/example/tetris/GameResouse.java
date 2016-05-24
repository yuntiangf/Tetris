package com.example.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class GameResouse{
	Resources m_Resources;	//资源类
	Canvas m_Canvas;		//画布
	Bitmap m_Bitmaphc = null;	//缓冲位图
	Bitmap m_Bitmap01 = null;	//图像位图
	Bitmap [] m_Bitmaps = new Bitmap[8];	//精灵位图
	Bitmap score;	//分数、等级、开始位图	
	Bitmap level;	
	Bitmap play;	

	public static int width;
	public static int height; 
	
	public GameResouse(Context context) {
		m_Resources = context.getResources();
		
		DisplayMetrics dm =m_Resources.getDisplayMetrics();
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		for(int i = 0; i<7;i++){
			m_Bitmaps[i] = createImage(m_Resources.getDrawable(R.drawable.cube_960_011+i), width/14, width/14);
		}
		m_Bitmap01 = createImage(m_Resources.getDrawable(R.drawable.bgcatcher), width, height);
		m_Bitmaps[7] = createImage(m_Resources.getDrawable(R.drawable.main11), width, height*5/6);
		
		score = createImage(m_Resources.getDrawable(R.drawable.score), width/4, height/3);
		level = createImage(m_Resources.getDrawable(R.drawable.levelup), width*5/12, height*5/27);
		m_Bitmaphc = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		play = createImage(m_Resources.getDrawable(R.drawable.b7), width, height);
		m_Canvas = new Canvas(m_Bitmaphc);
		bitmapB();
	}
	
	public void bitmapB(){
		Paint m_Paint = new Paint();
		m_Paint.setAntiAlias(true);
//		m_Paint.setAlpha(220);
		m_Canvas.drawBitmap(m_Bitmap01, 0, 0, null);
	}
	
	public static Bitmap createImage(Drawable title, int w,int h){
		System.out.println("asdas: "+w+" " +h );
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas  canvas = new Canvas(bitmap);
		if(w == width && h == height){
			title.setBounds(0, 0, w, h);
		}else{
			title.setBounds(10, 10, w, h);
		}
		
		title.draw(canvas);
		return bitmap;
	}
	
}
