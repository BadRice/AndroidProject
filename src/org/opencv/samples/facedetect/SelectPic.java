package org.opencv.samples.facedetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.samples.facedetect.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectPic extends Activity {
	private Bitmap bitmap = null;
	private ImageView imgShow = null;
	private static String TAG = "INFO";
	private File mCascadeFile = null;
	private CascadeClassifier mJavaDetector;
	private float mRelativeFaceSize = 0.2f;
	private int mAbsoluteFaceSize = 0;
	private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
	double xCenter = -1;
	double yCenter = -1;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");

				try {
					// load cascade file from application resources
					InputStream is = getResources().openRawResource(
							R.raw.lbpcascade_frontalface);
					File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
					mCascadeFile = new File(cascadeDir,
							"lbpcascade_frontalface.xml");
					FileOutputStream os = new FileOutputStream(mCascadeFile);

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = is.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					is.close();
					os.close();

					// --------------------------------- load left eye
					// classificator -----------------------------------
					InputStream iser = getResources().openRawResource(
							R.raw.haarcascade_lefteye_2splits);
					File cascadeDirER = getDir("cascadeER",
							Context.MODE_PRIVATE);
					File cascadeFileER = new File(cascadeDirER,
							"haarcascade_eye_right.xml");
					FileOutputStream oser = new FileOutputStream(cascadeFileER);

					byte[] bufferER = new byte[4096];
					int bytesReadER;
					while ((bytesReadER = iser.read(bufferER)) != -1) {
						oser.write(bufferER, 0, bytesReadER);
					}
					iser.close();
					oser.close();

					mJavaDetector = new CascadeClassifier(
							mCascadeFile.getAbsolutePath());
					if (mJavaDetector.empty()) {
						Log.e(TAG, "Failed to load cascade classifier");
						mJavaDetector = null;
					} else
						Log.i(TAG, "Loaded cascade classifier from "
								+ mCascadeFile.getAbsolutePath());

					// mJavaDetectorEye = new CascadeClassifier(
					// cascadeFileER.getAbsolutePath());
					// if (mJavaDetectorEye.empty()) {
					// Log.e(TAG, "Failed to load cascade classifier");
					// mJavaDetectorEye = null;
					// } else
					// Log.i(TAG, "Loaded cascade classifier from "
					// + mCascadeFile.getAbsolutePath());
					//
					cascadeDir.delete();

				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
				}

				// mOpenCvCameraView.setCameraIndex(1);
				// mOpenCvCameraView.enableFpsMeter();
				// mOpenCvCameraView.enableView();

			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.select_pic);

		Button backbtn = (Button) findViewById(R.id.backbtn);
		backbtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SelectPic.this, HomeActivity.class);
				startActivity(intent);
				SelectPic.this.finish();
			}

		});

		Button pickBtn = (Button) findViewById(R.id.selectPic);
		pickBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				/* 开启Pictures画面Type设定为image */
				intent.setType("image/*");
				/* 取得相片后返回本画面 */
				startActivityForResult(intent, 1);
			}
		});

		Button detect = (Button) findViewById(R.id.detect);
		detect.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Mat rgbMat = new Mat();
				Mat grayMat = new Mat();

				Utils.bitmapToMat(bitmap, rgbMat);

				Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
				MatOfRect faceDetections = new MatOfRect();

				if (mAbsoluteFaceSize == 0) {
					int height = grayMat.rows();
					if (Math.round(height * mRelativeFaceSize) > 0) {
						mAbsoluteFaceSize = Math.round(height
								* mRelativeFaceSize);
					}
				}

				MatOfRect faces = new MatOfRect();

				if (mJavaDetector != null)
					mJavaDetector.detectMultiScale(grayMat, faces, 1.1, 2,
							2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
							new Size(mAbsoluteFaceSize, mAbsoluteFaceSize),
							new Size());

				Rect[] facesArray = faces.toArray();
				for (int i = 0; i < facesArray.length; i++) {
					Core.rectangle(rgbMat, facesArray[i].tl(),
							facesArray[i].br(), FACE_RECT_COLOR, 3);
					xCenter = (facesArray[i].x + facesArray[i].width + facesArray[i].x) / 2;
					yCenter = (facesArray[i].y + facesArray[i].y + facesArray[i].height) / 2;
					Point center = new Point(xCenter, yCenter);

					Core.circle(rgbMat, center, 10, new Scalar(255, 0, 0, 255),
							3);

					Core.putText(rgbMat, "[" + center.x + "," + center.y + "]",
							new Point(center.x + 20, center.y + 20),
							Core.FONT_HERSHEY_SIMPLEX, 0.7, new Scalar(255,
									255, 255, 255));
					Log.i(String.format("Detected %s faces",
							faceDetections.toArray().length), "");

					// int facenum = 0;
					//
					// for (Rect rect : faceDetections.toArray()) {
					// Core.rectangle(
					// rgbMat,
					// new Point(rect.x, rect.y),
					// new Point(rect.x + rect.width, rect.y + rect.height),
					// new Scalar(0, 255, 0));
					// ++facenum;
					// }
					// Log.i(TAG, "检测到：" + facenum);
					Utils.matToBitmap(rgbMat, bitmap, true);
					imgShow = (ImageView) findViewById(R.id.imgShow);
					imgShow.setImageBitmap(bitmap);
					TextView textView = (TextView) findViewById(R.id.textView);
					// textView.setText("Facecount:" + facenum);
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this,
				mLoaderCallback);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			Log.e("CODE", "ActivityResult resultCode error");
			return;
		}

		if (requestCode == 1) {

			Uri uri = data.getData();

			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;

				// 节约内存
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				/*
				 * inPurgeable如果设置为true，则由此产生的位图将分配其像素，以便系统需要回收内存时可以将它们清除；
				 * inInputShareable与inPurgeable一起使用 ，如果inPurgeable为false那该设置将被忽略
				 * ，如果为true，那么它可以决定位图是否能够共享一个指向数据源的引用，或者是进行一份拷贝；
				 */
				options.inPurgeable = true;
				options.inInputShareable = true;
				// 只返回图片的大小等信息存于options中
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri), null, options);
				// 获得图片的真实宽高
				int height = options.outHeight;
				int width = options.outWidth;
				Log.i("path", "" + height);
				final float STANDARD_HEIGHT = 800f;
				final float STANDARD_WIDTH = 480f;
				// 根据需要设置inSampleSize的值
				int size = (int) ((height / STANDARD_HEIGHT + width
						/ STANDARD_WIDTH) / 2);
				if (size <= 0) {
					size = 1;
				}
				Log.i("path", "" + size);
				options.inSampleSize = size;
				// 重新设置inJustDecodeBounds = false
				options.inJustDecodeBounds = false;
				// 此时图片载入bitmap中
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri), null, options);
				imgShow = (ImageView) findViewById(R.id.imgShow);
				imgShow.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				Log.e("exception", e.toString());
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}