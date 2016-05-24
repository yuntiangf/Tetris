package com.example.tetris;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class ImageButton {
	GameResouse m_GameResouse;
	//按钮图片
	private Bitmap m_BitButton = null;
	
	//图片绘制的XY坐标
	private int mPosX = 0;
	private int mPosY = 0;
	//绘制图片的宽高
	private int mWidth = 0;
	private int mHeight = 0;
	//屏幕的高宽
	int m_w = 0;
	int m_h = 0;
	
	

	public ImageButton(Context context,int frameBitmapID,int x, int y){
//		m_GameResouse = new GameResouse(context);
//		m_w = m_GameResouse.width;
//		m_h = m_GameResouse.height;
		m_BitButton = ReadBitmap(context, frameBitmapID);
		mPosX = x;
		mPosY = y;
		mWidth = m_BitButton.getWidth();
		mHeight = m_BitButton.getHeight();
		
	}
	
	//绘制图片按钮
	public void DrawImageButton(Canvas canvas,Paint paint){
		
//		System.out.println("ddd: "+m_w+"  "+m_h );
//		Matrix matrix = new Matrix();
//		float sx = (m_w/4)/mWidth;
//		float sy = (m_w/4)/mHeight;
//		matrix.setScale(sx, sy);
//		System.out.println("cccc:  "+sx*mWidth+"  "+sy*mHeight);
//		canvas.drawBitmap(m_BitButton, matrix, paint);
		canvas.drawBitmap(m_BitButton, mPosX, mPosY, paint);
	}
	
	//判断是否点中图片按钮
	public boolean isClick(int x,int y){
		boolean flag = false;
		if(x >= mPosX && x <= mPosX+mWidth && y >= mPosY && y <= mPosY+mHeight){
			flag = true;
		}
		return flag;
	}
	
	//读取图片
	public Bitmap ReadBitmap(Context context,int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		//获取图片资源
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
		}
}
