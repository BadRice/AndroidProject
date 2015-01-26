package org.opencv.samples.facedetect;

import org.opencv.samples.facedetect.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		/* 设置显示face_detect_surface_view2.xml布局 */
		setContentView(R.layout.face_detect_surface_view1);
		/* findViewById(R.id.button1)取得布局中的dtbutton */
		 Button dtbutton = (Button) findViewById(R.id.dtbutton);
		 /* 监听dtbutton 的事件信息 */
		 dtbutton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* 新建一个Intent 对象 */
				Intent intent = new Intent();
				/* 指定intent 要启动的类 */
				intent.setClass(HomeActivity.this, FdActivity1.class);
				/* 启动一个新的Activity */
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
				intent.setClass(HomeActivity.this, SelectPic.class);
				startActivity(intent);
				HomeActivity.this.finish();
			}
			 
		 });
	}
}
