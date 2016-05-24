package com.example.tetris;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class GameView extends View {
	
	public final static int of_Width = 26;		//��Ϸ���еĳ���
	public final static int of_Height = 10;		//��Ϸ���еĿ��
	int gradeCourt = 0;		//����
	int level = 1;			//�ȼ�
	int line = 0;			//��ȥ������
	private int [][] m_screen = new int[of_Width][of_Height];		//��Ļ����
	private int [][] m_color = new int[of_Width][of_Height];		//��ɫ����
	private int [] log_color = new int[of_Height];			//����ʱ��¼��ɫֵ
	private int k = 0,a =0, state = 0, statenext =0;		//��������������
	private int of_x = 3, of_y = 0;		//����λ��
	private int of_x_x = 0; 			//��Ӱ���Ϸ��ľ���
	private int add_dis_y = 0; 			//�𶯾���
	public int log_X = 0;				//����λͼ��־
	public int timeTask = 1000;			//�ȼ�������ز���
	public boolean buttonLeft = false;	//�����任��ɫֵ
	public boolean buttonRight = false;
	public boolean buttonRotate = false;
	public boolean buttonDown = false;
	public boolean buttonFastDown = false;
	ImageButton m_ButtonLeft = null;		//ͼ��ť
	ImageButton m_ButtonLeft_a = null;
	ImageButton m_ButtonRight = null;
	ImageButton m_ButtonRight_a = null;
	ImageButton m_ButtonRotate = null;
	ImageButton m_ButtonRotate_a = null;
	ImageButton m_ButtonDown = null;
	ImageButton m_ButtonDown_a = null;
	ImageButton m_ButtonFastDown = null;
	ImageButton m_ButtonFastDown_a = null;
	Canvas m_Canvas = null;
	GameResouse m_GameResource;
	Random m_Random = null;
	int w = 0;
	int h = 0;
	
	public GameView(Context context) {
		super(context);
		m_GameResource = new GameResouse(context);		//��ʼ��λͼ��Դ����
		w = m_GameResource.width;
		h = m_GameResource.height;
		
		inItButton(context);				//��ʼ��ͼ��
		clean();							//�����Ϸ��ʼ��������ֵ
		m_Random = new Random();			//�����������
		state = Math.abs(m_Random.nextInt()%7);		//��ǰ������ɫֵ
		statenext = Math.abs(m_Random.nextInt()%7);	//�¸�������ɫֵ
		k = Math.abs(m_Random.nextInt()%28);		//��ǰ����
		a = Math.abs(m_Random.nextInt()%28);		//�¸�����
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(0 != statefang.state[k][i][j]){
					m_screen[i][j+3] = statefang.state[k][i][j];
					m_color[i][j+3] = state;
				}
			}
		}
		of_y = 3;
		fastDown(1);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	//��ʼ��ͼ��ť
	public void inItButton(Context context){
		m_ButtonLeft = new ImageButton(context, R.drawable.control_left1, 0, h*4/5);
		m_ButtonLeft_a = new ImageButton(context, R.drawable.control_left2, 0, h*4/5);
		m_ButtonRight = new ImageButton(context, R.drawable.control_right1, w/4, h*4/5);
		m_ButtonRight_a = new ImageButton(context, R.drawable.control_right2, w/4, h*4/5);
		m_ButtonRotate = new ImageButton(context, R.drawable.control_rotate1, w/2, h*4/5);
		m_ButtonRotate_a = new ImageButton(context, R.drawable.control_rotate2, w/2, h*4/5);
		m_ButtonDown = new ImageButton(context, R.drawable.control_down1, w*3/4, h*4/5);
		m_ButtonDown_a = new ImageButton(context, R.drawable.control_down2, w*3/4, h*4/5);
		m_ButtonFastDown = new ImageButton(context, R.drawable.control_drop1, w*3/4, h*7/10);
		m_ButtonFastDown_a = new ImageButton(context, R.drawable.control_drop2, w*3/4, h*7/10);
	}
	
	public void clean(){
		for (int i = 0; i < of_Width; i++) {
			for (int j = 0; j < of_Height; j++) {
				m_screen[i][j] = 0;
				m_color[i][j] = -1;
			}
		}
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint m_paint =new Paint();
		m_paint.setAntiAlias(true);
		m_paint.setColor(Color.RED);
		m_paint.setAlpha(100);
		m_paint.setTextSize(w/20);
		this.setKeepScreenOn(true);
		canvas.drawBitmap(m_GameResource.m_Bitmaphc, 0, 0, null);
		canvas.drawBitmap(m_GameResource.m_Bitmaps[7], 0, add_dis_y, null);
		canvas.drawBitmap(m_GameResource.score, w*3/4, h*5/16+add_dis_y, null);
		
		PaintTm(canvas, m_paint);		//�����·���͸������
		PaintNext(canvas);				//������һ����������
		PaintNow(canvas);				//������������ķ���
		PaintButton(canvas, m_paint);	//���Ʒ��򰴼�
		canvas.drawText(" "+level, w*6/7, h*2/5, m_paint);
		canvas.drawText(" "+gradeCourt, w*6/7, h/2, m_paint);
		canvas.drawText(" "+line, w*6/7, h*3/5, m_paint);
		add_dis_y = 0;
		m_Canvas = canvas;
	}
	
	//�����·���͸������
	public void PaintTm(Canvas canvas,Paint paint){
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(statefang.state[k][i][j] != 0 ){
					canvas.drawBitmap(m_GameResource.m_Bitmaps[state], (of_y+j+1)*w/18, 
							(of_x_x-4+i)*w/18+11+add_dis_y, paint);
				}
			}
		}
	}
	
	//�����¸�����
	public void PaintNext(Canvas canvas){
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(statefang.state[a][i][j] != 0){
					canvas.drawBitmap(m_GameResource.m_Bitmaps[statenext], j*15+w*3/4, 
							h/8+i*20+add_dis_y, null);
				}
			}
		}
	}
	
	//�������ڵķ���
	public void PaintNow(Canvas canvas){
		for (int i = 0; i < of_Width; i++) {
			for (int j = 0; j < of_Height; j++) {
				if(m_screen[i][j] != 0 && i> 3){
					canvas.drawBitmap(m_GameResource.m_Bitmaps[m_color[i][j]], (j+1)*w/18,
							(i-4)*w/18+11+add_dis_y, null);
				}
			}
		}
	}
	
	//���ư���������λͼ
	public void PaintButton(Canvas canvas, Paint m_Paint){
		if(timeTask == 800 || timeTask == 500 || timeTask == 200){
			if(log_X == 0){
				canvas.drawBitmap(m_GameResource.level, w/2,h/2, m_Paint);
				log_X = 1;
			}
		}
		if(buttonLeft){
			m_ButtonLeft_a.DrawImageButton(canvas, m_Paint);
		}
		else{
			m_ButtonLeft.DrawImageButton(canvas, m_Paint);
		}
		buttonLeft = false;
		
		if(buttonRight){
			m_ButtonRight_a.DrawImageButton(canvas, m_Paint);
		}
		else{
			m_ButtonRight.DrawImageButton(canvas, m_Paint);
		}
		buttonRight = false;
		
		if(buttonRotate){
			m_ButtonRotate_a.DrawImageButton(canvas, m_Paint);
		}
		else{
			m_ButtonRotate.DrawImageButton(canvas, m_Paint);
		}
		buttonRotate = false;
		
		if(buttonDown){
			m_ButtonDown_a.DrawImageButton(canvas, m_Paint);
		}
		else{
			m_ButtonDown.DrawImageButton(canvas, m_Paint);
		}
		buttonDown = false;
		
		if(buttonFastDown){
			m_ButtonFastDown_a.DrawImageButton(canvas, m_Paint);
		}
		else{
			m_ButtonFastDown.DrawImageButton(canvas, m_Paint);
		}
		buttonFastDown = false;
		
	}
	
	public void colorstate(int x,int y,int stateColor){
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(statefang.state[k][i][j] != 0){
					m_color[x+i][y+j] = stateColor;
				}
			}
		}
	}
	
	//�԰�������Ӧ
	public void idKeyDown(int key,KeyEvent keyevent){
		if(key ==KeyEvent.KEYCODE_DPAD_LEFT && moveLeftOn()){
			Left();
			fastDown(1);
			buttonLeft = false;
		}
		if(key ==KeyEvent.KEYCODE_DPAD_RIGHT && moveRightOn()){
			Right();
			fastDown(1);
			buttonRight = false;
		}
		if(key ==KeyEvent.KEYCODE_DPAD_UP && rotateOnCurrent()){
			Rotate();
			fastDown(1);
			buttonRotate = false;
		}
		if(key ==KeyEvent.KEYCODE_DPAD_DOWN && moveDown()){
			Down();
			fastDown(1);
			buttonDown = false;
		}
		if(key ==KeyEvent.KEYCODE_DPAD_CENTER && moveDown()){
			FastDown();
			fastDown(1);
			buttonFastDown = false;
		}
		
	}
	
	
	
	//������ת�ж�
	public boolean avaliableForTile(int [][] tile ,int x, int y){
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(tile[i][j] != 0){
					if(!isSpace(x+i, y+j)){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	//�жϷ����ܷ���ת���ܷ���true, ����������ת
	public boolean rotateOnCurrent(){
		int tempX = 0, tempY = 0;
		int tempShape;
		int [][] tempTile = new int[4][4];
		tempShape = k;
		if(tempShape % 4 > 0){
			tempShape--;
		}else{
			tempShape += 3;
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				tempTile[i][j] = statefang.state[tempShape][i][j];
			}
		}
		
		tempX = of_x;
		tempY = of_y;
		boolean canTurn =false;
		cleanState(of_x,of_y);
		if(avaliableForTile(tempTile, tempX, tempY)){
			canTurn = true;
		}else if(avaliableForTile(tempTile, tempX-1, tempY)){
			canTurn = true;
			tempX--;
		}else if(avaliableForTile(tempTile, tempX-2, tempY)){
			canTurn = true;
			tempX -= 2;
		}else if(avaliableForTile(tempTile, tempX+1, tempY)){
			canTurn = true;
			tempX++;
		}else if(avaliableForTile(tempTile, tempX+2, tempY)){
			canTurn = true;
			tempX += 2;
		}
		
		if(canTurn){
			k = tempShape;
			of_x = tempX;
			of_y = tempY;
			return true;
		}
		//ִ����ת֮ǰ��Ļ�������ɫ����ص���ǰֵ
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (statefang.state[k][i][j] !=0) {
					m_screen[of_x+i][of_y+j] = 1;
					m_color[of_x+i][of_y+j] = state;
				}
			}
		}
		return false;
	}
	
	//�������״̬
	public void cleanState(int x, int y) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (statefang.state[k][i][j] != 0) {
					m_screen[x+i][y+j] = 0;
					m_color[x+i][y+j] = -1;
				}
			}
		}
	}

	//�жϷ����Ƿ��ڱ߽����Լ���ʼֵ�Ƿ�Ϊ0
	public boolean isSpace(int x,int y){
		if(x<0 || x >=of_Width){
			return false;
		}
		if(y<0 || y >= of_Height){
			return false;
		}
		if(m_screen[x][y] == 0){
			return true;
		}
		return false;
	}
	
	//�жϷ����ܷ��������
	public boolean moveDown(){
		int n = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(statefang.state[k][i][j] != 0){
					n = i;
					while(n < 3 && statefang.state[k][n+1][j] != 0){
						n++;
					}
					if(!isSpace(of_x+n+1,of_y+j)){
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	//�������
	public int cleanFullLine(){
		int num = 0, n = 0, linenum = 0;
		num = brigeLine();
		for(int i = of_x+3; i > of_x-1; i--) {
			System.out.println("aaa: "+fullLine(i));
			if(fullLine(i)){
				n =i;
				for (int r = 0; r < of_Height; r++) {
					log_color[r] = m_color[n][r];
				}
				
				linenum++;
				for(int j = n; j >= num ; j--) {
					for (int k = 0; k < of_Height; k++) {
						if(j == num){
							m_screen[j][k] = 0;
							m_color[j][k] = -1;
						}else{
							m_screen[j][k] = m_screen[j-1][k];
							m_color[j][k] = m_color[j-1][k];
						}
					}
				}
				//��������ȥ���ļ���״̬
				for(int t = 0; t < of_Height; t++) {
					m_Canvas.drawBitmap(m_GameResource.m_Bitmaps[log_color[t]], 24+t*w/18, (n-4)*w/18, null);
				}
				
				i = n+1;
			}
		}
		
		return linenum;
	}
	
	//�ҳ�����û�з��飬��������ֵ�����򷵻�0
	public int brigeLine(){
		for(int i = of_x+3; i > 0; i--) {
			int j =0;
			while(j < of_Height && m_screen[i][j] == 0){
				j++;
			}
			if(j == of_Height){
				 return i+1;
			}
		}
		return 0;
	}
	
	//�жϸ����Ƿ�������
	public boolean fullLine(int line){
		int j = 0;
		while(j < of_Height && m_screen[line][j] == 1){
			j++;
		}
		if(j == of_Height){
			return true;
		}
		return false;
	}
	
	//�ƶ�ʱ���������Ļ�������ɫ����У��
	public void move(){
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (statefang.state[k][i][j] != 0) {
					m_screen[i+of_x][j+of_y] = 1;
					m_color[i+of_x][j+of_y] = state;
				}
			}
		}
	}
	
	//�ܷ�����
	public boolean moveRightOn(){
		buttonRight = true;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(statefang.state[k][i][j] !=0){
					while(j<3 && statefang.state[k][i][j+1] != 0){
						j++;
					}
					if(!isSpace(of_x+i, of_y+j+1)){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	//�ܷ�����
	public boolean moveLeftOn(){
		buttonLeft = true;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (statefang.state[k][i][j] !=0) {
					if(!isSpace(of_x+i, of_y+j-1)){
						return false;
					}else{
						break;
					}
				}
			}
		}
		return true;
	}
	
	//�ܷ�������䣬�˺���������״̬��һ���Ƿ�����Ӱ��ֵ��һ������������ֵ
	public int fastDown(int log){
		int a[] = {100,100,100,100};
		int n = 0, s = 0, m = 0;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (statefang.state[k][i][j] != 0) {
					m =i;
					while(m < 3 && statefang.state[k][m+1][j] != 0){
						m++;
					}
					if(isSpace(of_x+m+1, of_y+j)){
						n = 1;
						while(isSpace(of_x+m+(++n), of_y+j));
						a[s++] = n-1;
					}
				}
			}
		}
		switch(log){
		case 1:
			of_x_x = of_x+min(min(a[0],a[1]),min(a[2],a[3]));
			return of_x_x;
		case 2:
			s = of_x;
			of_x += min(min(a[0],a[1]),min(a[2],a[3]));
			break;
		}
		
		return s;
	}
	
	public int min(int a,int b){
		if(a >= b){
			return b;
		}else{
			return a;
		}
	}
	
	//�Ʒ�
	public void Count(int grade){
		line +=grade;
		switch(grade){
		case 1:
			gradeCourt += 100;
			break;
		case 2:
			gradeCourt += 300;
			break;
		case 3:
			gradeCourt += 500;
			break;
		case 4:
			gradeCourt += 800;
			break;
		}
	}
	
	//��Ϸ����
	public boolean GameOver(){
		for (int i = 0; i < of_Height; i++) {
			if (m_screen[4][i] != 0) {
				return true;
			}
		}
		return false;
	}
	
	//���ƺ���
	public void Right(){
		cleanState(of_x, of_y);
		of_y++;
		move();
	}
	
	//���ƺ���
	public void Left(){
		cleanState(of_x, of_y);
		of_y--;
		move();
	}
	
	//���亯��
	public void Down(){
		cleanState(of_x, of_y);
		of_x++;
		move();
	}
	
	//�������亯��
	public void FastDown(){
		add_dis_y = 5;
		cleanState(fastDown(2), of_y);
		move();
	}
	
	//��ת����
	public void Rotate(){
		cleanState(of_x, of_y);
		move();
	}
	
	//�������䵽�ײ�������Ļ�������һϵ�еĴ���
	public void Stop(){
		of_x_x = 0;
		colorstate(of_x, of_y, state);		//������ɫֵ
		Count(cleanFullLine());				//���мƷ�
		
	}
	
	public void newState(){
		k = a;		//��������ֵ
		state = statenext;		//�л���ɫֵ
		a = Math.abs(m_Random.nextInt()%28);			//���²�������ֵ
		statenext = Math.abs(m_Random.nextInt()%7);		//���²�����ɫֵ	
		of_y = 3;			//��������λ��
		of_x = 3;	
	}
	
}
