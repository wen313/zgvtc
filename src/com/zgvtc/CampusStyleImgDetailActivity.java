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
	private ArrayList contents;// 新闻标题

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	private boolean flag = false;//真表示滑动，假表示只是移动

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	private Toast mToast;
	private int width;
	private int height;

	// 从内存缓存中获取图片
	ImageMemoryCache memoryCache;
	// 文件中获取图片
	ImageFileCache fileCache;

	private AsyncImageLoader asyncImageLoader;

	private GestureDetector gd;// 用于捕捉touch的详细手势（gesture）

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
		Log.d(LOG_TAG, "索引："+index+",内容："+contents.toString());
		// 加载图片
		// 内存加载
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
		gd = new GestureDetector(this, new GDetector());// 手势监听
	}

	private void getImage() {
		Map<String,String> map = (Map<String, String>) contents.get(index);
		tv_title.setText(map.get("title"));
		tv_index.setText((index+1)+" / "+contents.size());
		String ImgUrl = map.get("ImageUrl");
		Matrix mt = new Matrix();
		Log.d(LOG_TAG, "高度：" + height + ",宽度：" + width);
		mt.postScale(1.8f, 1.5f);
		mt.postTranslate(0, height / 4);
		iv_imgDetail.setImageMatrix(mt);
		Bitmap result = memoryCache.getBitmapFromCache(ImgUrl);
		iv_imgDetail.setTag(ImgUrl);
		Log.d(LOG_TAG, "ImageUrl地址：" + ImgUrl);
		if (result == null) {
			// 文件缓存中获取
			result = fileCache.getImage(ImgUrl);
			if (result == null) {// 网络获取
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
			// 添加到内存缓存
			memoryCache.addBitmapToCache(ImgUrl, result);
			showToast("图片下载失败...");
		}
		if (result == null) {
			iv_imgDetail.setImageResource(R.drawable.loading_image);// 替换成加载的中的图片

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
				Log.d("Infor", "多点操作");
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				matrix.set(iv_imgDetail.getImageMatrix());
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				Log.d("Infor", "触摸了...");
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN: // 多点触控
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
				if (mode == DRAG) { // 此实现图片的拖动功能...
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				} else if (mode == ZOOM) {// 此实现图片的缩放功能...
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
		gd.onTouchEvent(event);// 首先监听滑动事件
		return false;
	}

	public class GDetector extends SimpleOnGestureListener // GDetector 名字是随便起的
	{
		// MotionEvent e1 按下时的状态,位置
		// MotionEvent e2, //松手时的状态，位置
		// float vx,//x坐标的移动速度，单位: px/秒
		// float y坐标的移动速度
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float vx,
				float vy) {
			Log.d(LOG_TAG, "滑动事件...");
			// 滑动速度足够快至少50点/秒，手指起落点减起点是正值且>200 判断属于向左滑动
			if ((e1.getX() - e2.getX() > 200) && (Math.abs(vx) > 50)) {
				index++;
				
			} else if ((e2.getX() - e1.getX() > 200) && (Math.abs(vx) > 50)) // 同理判断是向右滑动
			{
				index--;
			}
			if(index<0){//第一张
				++index;
				showToast("已经到达第一张");
			}else if(index>=contents.size()){//最后一张
				--index;
				showToast("已经到达最后一张");
			}else{
				flag = true;
				getImage();
			}
			return false;
		}
		
	}

	/**
	 * 显示Toast消息
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
