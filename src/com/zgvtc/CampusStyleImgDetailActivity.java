package com.zgvtc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zgvtc.bean.NewsTitlePicBean;
import com.zgvtc.nets.AsyncImageLoader;
import com.zgvtc.nets.AsyncImageLoader.ImageCallback;
import com.zgvtc.nets.ImageFileCache;
import com.zgvtc.nets.ImageMemoryCache;

public class CampusStyleImgDetailActivity extends Activity implements
		OnTouchListener {
	private final String LOG_TAG = "CampusStyleImgDetailActivity";
	private ImageView iv_return;
	private TextView tv_title;
	private ImageView iv_imgDetail;
	private TextView tv_index;
	private int index;
	private ArrayList contents;// ���ű���

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	private boolean flag = false;//���ʾ�������ٱ�ʾֻ���ƶ�

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	private Toast mToast;
	private int width;
	private int height;

	// ���ڴ滺���л�ȡͼƬ
	ImageMemoryCache memoryCache;
	// �ļ��л�ȡͼƬ
	ImageFileCache fileCache;

	private AsyncImageLoader asyncImageLoader;

	private GestureDetector gd;// ���ڲ�׽touch����ϸ���ƣ�gesture��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.campustyle_img_detail);
		Intent it = getIntent();
		Bundle bundle=it.getExtras();
		contents = bundle.getParcelableArrayList("contents");
		index = it.getIntExtra("index", 0);
		Log.d(LOG_TAG, "������"+index+",���ݣ�"+contents.toString());
		// ����ͼƬ
		// �ڴ����
		memoryCache = new ImageMemoryCache(this);
		fileCache = new ImageFileCache();
		asyncImageLoader = new AsyncImageLoader();
		WindowManager wm = this.getWindowManager();

		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		setUpView();
	}

	private void setUpView() {
		iv_return = (ImageView) findViewById(R.id.iv_return);
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_imgDetail = (ImageView) findViewById(R.id.iv_imgDetail);
		tv_index = (TextView) findViewById(R.id.tv_index);
		getImage();
		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CampusStyleImgDetailActivity.this.finish();
			}
		});
		gd = new GestureDetector(this, new GDetector());// ���Ƽ���
	}

	private void getImage() {
		Map<String,String> map = (Map<String, String>) contents.get(index);
		tv_title.setText(map.get("title"));
		tv_index.setText((index+1)+" / "+contents.size());
		String ImgUrl = map.get("ImageUrl");
		Matrix mt = new Matrix();
		Log.d(LOG_TAG, "�߶ȣ�" + height + ",��ȣ�" + width);
		mt.postScale(1.8f, 1.5f);
		mt.postTranslate(0, height / 4);
		iv_imgDetail.setImageMatrix(mt);
		Bitmap result = memoryCache.getBitmapFromCache(ImgUrl);
		iv_imgDetail.setTag(ImgUrl);
		Log.d(LOG_TAG, "ImageUrl��ַ��" + ImgUrl);
		if (result == null) {
			// �ļ������л�ȡ
			result = fileCache.getImage(ImgUrl);
			if (result == null) {// �����ȡ
				result = asyncImageLoader.loadDrawable(ImgUrl,
						new ImageCallback() {
							public void imageLoaded(Bitmap imageBitmap,
									String imageUrl) {
								memoryCache.addBitmapToCache(imageUrl,
										imageBitmap);
								fileCache.saveBitmap(imageBitmap, imageUrl);
							}
						});
			}
		} else {
			// ��ӵ��ڴ滺��
			memoryCache.addBitmapToCache(ImgUrl, result);
			showToast("ͼƬ����ʧ��...");
		}
		if (result == null) {
			iv_imgDetail.setImageResource(R.drawable.loading_image);// �滻�ɼ��ص��е�ͼƬ

		} else {
			iv_imgDetail.setImageBitmap(result);
			iv_imgDetail.setOnTouchListener(this);
			iv_imgDetail.setLongClickable(true);
		}
		flag = false;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		if(!flag){
			if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP)
				Log.d("Infor", "������");
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				matrix.set(iv_imgDetail.getImageMatrix());
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				Log.d("Infor", "������...");
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN: // ��㴥��
				oldDist = this.spacing(event);
				if (oldDist > 10f) {
					Log.d("Infor", "oldDist" + oldDist);
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) { // ��ʵ��ͼƬ���϶�����...
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				} else if (mode == ZOOM) {// ��ʵ��ͼƬ�����Ź���...
					float newDist = spacing(event);
					if (newDist > 10) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}
			iv_imgDetail.setImageMatrix(matrix);
		}
		gd.onTouchEvent(event);// ���ȼ��������¼�
		return false;
	}

	public class GDetector extends SimpleOnGestureListener // GDetector ������������
	{
		// MotionEvent e1 ����ʱ��״̬,λ��
		// MotionEvent e2, //����ʱ��״̬��λ��
		// float vx,//x������ƶ��ٶȣ���λ: px/��
		// float y������ƶ��ٶ�
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float vx,
				float vy) {
			Log.d(LOG_TAG, "�����¼�...");
			// �����ٶ��㹻������50��/�룬��ָ�������������ֵ��>200 �ж��������󻬶�
			if ((e1.getX() - e2.getX() > 200) && (Math.abs(vx) > 50)) {
				index++;
				
			} else if ((e2.getX() - e1.getX() > 200) && (Math.abs(vx) > 50)) // ͬ���ж������һ���
			{
				index--;
			}
			if(index<0){//��һ��
				++index;
				showToast("�Ѿ������һ��");
			}else if(index>=contents.size()){//���һ��
				--index;
				showToast("�Ѿ��������һ��");
			}else{
				flag = true;
				getImage();
			}
			return false;
		}
		
	}

	/**
	 * ��ʾToast��Ϣ
	 * 
	 * @param msg
	 */
	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

}
