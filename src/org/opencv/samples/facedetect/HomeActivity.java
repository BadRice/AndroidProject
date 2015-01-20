package org.opencv.samples.facedetect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		/* ������ʾface_detect_surface_view2.xml���� */
		setContentView(R.layout.face_detect_surface_view1);
		/* findViewById(R.id.button1)ȡ�ò����е�dtbutton */
		 Button dtbutton = (Button) findViewById(R.id.dtbutton);
		 /* ����dtbutton ���¼���Ϣ */
		 dtbutton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* �½�һ��Intent ���� */
				Intent intent = new Intent();
				/* ָ��intent Ҫ�������� */
				intent.setClass(HomeActivity.this, FdActivity1.class);
				/* ����һ���µ�Activity */
				startActivity(intent);
				HomeActivity.this.finish();
			}
		});
		 
		 Button exitButton = (Button) findViewById(R.id.exit);
		 exitButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		 });
		 
		 Button picBtn = (Button) findViewById(R.id.importbutton);
		 picBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, ChosePic.class);
				startActivity(intent);
				HomeActivity.this.finish();
			}
			 
		 });
	}
}
