package com.example.tetris;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class LoginActivity extends Activity{
	
	private ImageButton play;
	private ImageButton help;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		play = (ImageButton) findViewById(R.id.play);
		
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,GameActivity.class);
				startActivity(intent);
			}
		});
		
		help = (ImageButton) findViewById(R.id.help);
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).setTitle("����")
						.setMessage("���ϻ�����ͣ���ٴ��ϻ�������Ϸ�����¿��ٻ�����ʵ��˲���»��������LEFE:�����ƶ����顣" +
								"�����RIGHT:�����ƶ����顣�����ؼ�����ص���ʾ��").setNeutralButton("ȷ��",
										new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										
									}
								}).create();
				dialog.show();
			}
		});
				
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).setTitle("�Ƿ��˳���")
					.setPositiveButton("��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).setNegativeButton("��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).create();
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
