package com.example.tetris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity implements OnGestureListener, OnTouchListener{

	GestureDetector m_GestureDetector;//��������
	
	public GameActivity(){
		m_GestureDetector = new GestureDetector(this);
	}
	
	private static final int ii = 100;		//UI���̷߳�����Ϣƥ�����
	private static final int kk = 101;
	private int dialog_xx = 0;
	private boolean Thread_Log = true;		//�����̱߳�־
	private float log = 0;
	private int m_time_a, i=0;				//����ʱ������������д�������ز���
	private int log_num = 0;
	private int log_log = 0;
	private int w,h;		//��Ļ�Ŀ��
	Vibrator vibrate;		//��������
	long [] pattern = {0,10,10,30};	//����ָ����ģʽ������
	GameView m_GameView = null;
	
	//����Handler������ʵ����ͼ�ĸ���
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case GameActivity.ii:
				m_GameView.invalidate();
				break;
			case GameActivity.kk:
				Thread_Log = false;
				dialog_a();
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		m_GameView = new GameView(this);
		w = m_GameView.w;
		h = m_GameView.h;
		init();
		new Thread(new GameThread()).start();
	}
	
	//��ʼ��
	private void init() {
		setContentView(m_GameView);
		vibrate = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		m_GameView.setOnTouchListener(this);
		m_GameView.setClickable(true);
		m_GestureDetector.setIsLongpressEnabled(true);	//������Ӧ����
	}

	class GameThread implements Runnable{
		
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				while(Thread_Log){						//��Ϸ�Ƿ���ͣ��־
					Message message = new Message();	//��Ϣ����
					if(dialog_xx == 0){	
						message.what = GameActivity.ii;	//��ֵ��Ϣ
					}else{
						message.what = GameActivity.kk;
					}
					GameActivity.this.handler.sendMessage(message);		//������Ϣ
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					
					m_time_a = ++i*100;
					if(m_time_a == (10-2*m_GameView.level)*100){
						onPause();		//ִ�����䴦����
						i = 0;
					}
				}
			}
		}
	}
    
	public void onPause(){
		super.onPause();
		
		if(Thread_Log){		//��ͣ��־
			if(m_GameView.moveDown()){		//�ܹ�����
				m_GameView.Down();
				log_num = 0;
			}else{
				m_GameView.Stop();
				if(m_GameView.GameOver()){	//��Ϸ����
					dialog_xx = 1;
				}
				if(dialog_xx == 0){
					m_GameView.newState();
				}
				
				if(m_GameView.gradeCourt >= 3000 && m_GameView.timeTask == 1000){
					m_GameView.timeTask = 800;
					m_GameView.level +=1;
				}
				if(m_GameView.gradeCourt >= 8000 && m_GameView.timeTask == 800){
					m_GameView.timeTask = 600;
					m_GameView.level +=1;
					m_GameView.log_X = 0;
				}
				if(m_GameView.gradeCourt >= 15000 && m_GameView.timeTask == 600){
					m_GameView.timeTask = 400;
					m_GameView.level +=1;
					m_GameView.log_X = 0;
				}
				if(m_GameView.gradeCourt >= 30000 && m_GameView.timeTask == 400){
					m_GameView.timeTask = 200;
					m_GameView.level +=1;
					m_GameView.log_X = 0;
				}
				
				m_GameView.fastDown(1);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		m_GameView.idKeyDown(keyCode, event);
		
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			Thread_Log = false;
			dialog();
			return true;
		}
		
		return true;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return m_GestureDetector.onTouchEvent(event);
	}

	private void dialog() {
		// TODO Auto-generated method stub
		AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).setMessage("ȷ��Ҫ�˳���")
				.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Thread_Log = true;
					}
				}).create();
		dialog.show();
	}

	private void dialog_a() {
		// TODO Auto-generated method stub
		AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).setMessage("��Ϸ������")
				.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						m_GameView = null;
						Intent intent = new Intent(GameActivity.this,LoginActivity.class);
						startActivity(intent);
					}
				}).create();
		dialog.show();
	}
	

	//���������¼�
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		int x = (int) e.getX();			//��ȡ���µ�����
		int y = (int) e.getY();
		
		if(m_GameView.m_ButtonLeft.isClick(x, y)){		//�ж������Ƿ���ͼ������
			m_GameView.buttonLeft = true;
			vibrate.vibrate(pattern, -1);				//����°�������Ӧ
			
			if(m_GameView.moveLeftOn() && Thread_Log){
				m_GameView.Left();
				m_GameView.fastDown(1);					//�����ʱ���·��������̾��룬һ�����͸������
			}
		}
		if(m_GameView.m_ButtonRight.isClick(x, y)){		
			m_GameView.buttonRight = true;
			vibrate.vibrate(pattern, -1);				
			
			if(m_GameView.moveRightOn() && Thread_Log){
				m_GameView.Right();
				m_GameView.fastDown(1);					
			}
		}
		if(m_GameView.m_ButtonRotate.isClick(x, y)){	
			m_GameView.buttonRotate = true;
			vibrate.vibrate(pattern, -1);				
			
			if(m_GameView.rotateOnCurrent() && Thread_Log){
				m_GameView.Rotate();
				m_GameView.fastDown(1);					
			}
		}
		if(m_GameView.m_ButtonDown.isClick(x, y)){		
			m_GameView.buttonDown = true;
			vibrate.vibrate(pattern, -1);				
			
			if(m_GameView.moveDown() && Thread_Log){
				m_GameView.Down();
				m_GameView.fastDown(1);					
			}
		}
		if(m_GameView.m_ButtonFastDown.isClick(x, y)){		
			m_GameView.buttonFastDown = true;
			vibrate.vibrate(pattern, -1);				
			
			if(m_GameView.moveDown() && Thread_Log){
				m_GameView.FastDown();					
			}
		}
		
		return true;
	}
	
	//�����Ļ
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		if(e.getY() < h*3/4){
			if(e.getX() < w*3/4 || e.getY() < h*5/8){
				if(m_GameView.rotateOnCurrent()){
					m_GameView.Rotate();
					m_GameView.fastDown(1);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		if(e1.getAction() == MotionEvent.ACTION_DOWN){
			log = e1.getX();
		}
		float e1x = e1.getX();
		float e2x = e2.getX();
		float x1 = e2x-e1x;
		float e1y = e1.getY();
		float e2y = e2.getY();
		float y1 = e2y-e1y;
		float absX = Math.abs(x1);
		float absY = Math.abs(y1);
		if(absX > absY){
			if(x1 > 80f && m_GameView.moveRightOn()){
				m_GameView.Right();
				m_GameView.fastDown(1);
			}
			if(x1 < -80f && m_GameView.moveLeftOn()){
				m_GameView.Left();
				m_GameView.fastDown(1);
			}
			log = e2x;
		}
		
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		int Min_Distance = 100;
		int Min_Velocity = 200;
		float e1y = e1.getY();
		float e2y = e2.getY();
		float y = e2y - e1y;
		if(y>Min_Distance && Math.abs(velocityY) > Min_Velocity && m_GameView.moveDown() && Thread_Log){
			log_num = 1;
			m_GameView.FastDown();
		}
		if(y < 0 && Math.abs(velocityY) > Min_Velocity){
			if(log_log == 0){
				Thread_Log = false;
				log_log =1;
			}else{
				Thread_Log = true;
				log_log = 0;
			} 
		}
		
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
