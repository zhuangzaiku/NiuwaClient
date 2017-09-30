package tv.niuwa.live.danmaku;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


import com.smart.androidutils.utils.SharePrefsUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;
import tv.niuwa.live.R;
import tv.niuwa.live.utils.DisplayUtil;

import static master.flame.danmaku.danmaku.model.BaseDanmaku.TYPE_SCROLL_LR;
import static master.flame.danmaku.danmaku.model.BaseDanmaku.TYPE_SCROLL_RL;

/**
 * @author tomato
 * @version create time：2015-2-7 下午1:17:34
 * 
 */

public class DanmaManager {
	
	private String TAG = DanmaManager.class.getSimpleName();
	
	private DanmakuView danmakuSurfaceView;
	private Context context;

	private DanmakuContext danmakuContext;

	private BaseDanmakuParser baseDanmakuParser;
	private static final int nameColor = Color.parseColor("#ffc63d");

	private static final int MSG_DANMA = 0x001;

	private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

		private Drawable mDrawable;

		@Override
		public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
//			if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
//				// FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
//				new Thread() {
//
//					@Override
//					public void run() {
//						String url = "http://www.bilibili.com/favicon.ico";
//						InputStream inputStream = null;
//						Drawable drawable = mDrawable;
//						if(drawable == null) {
//							try {
//								URLConnection urlConnection = new URL(url).openConnection();
//								inputStream = urlConnection.getInputStream();
//								drawable = BitmapDrawable.createFromStream(inputStream, "bitmap");
//								mDrawable = drawable;
//							} catch (MalformedURLException e) {
//								e.printStackTrace();
//							} catch (IOException e) {
//								e.printStackTrace();
//							} finally {
//								IOUtils.closeQuietly(inputStream);
//							}
//						}
//						if (drawable != null) {
//							drawable.setBounds(0, 0, 100, 100);
//							SpannableStringBuilder spannable = createSpannable(drawable);
//							danmaku.text = spannable;
//							if(mDanmakuView != null) {
//								mDanmakuView.invalidateDanmaku(danmaku, false);
//							}
//							return;
//						}
//					}
//				}.start();
//			}
		}

		@Override
		public void releaseResource(BaseDanmaku danmaku) {
			// TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
		}
	};

	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case MSG_DANMA:
					if (msg.obj != null && msg.obj instanceof BaseDanmaku)
						Log.d(TAG, "handler view add danmaku");
						danmakuSurfaceView.addDanmaku((BaseDanmaku) msg.obj);
					break;
			}
		};
	};

	/**
	 * 绘制背景(自定义弹幕样式)
	 */
	private static class BackgroundCacheStuffer extends SpannedCacheStuffer {
		// 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
		final Paint paint = new Paint();

		@Override
		public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
			danmaku.padding = 10;  // 在背景绘制模式下增加padding
			super.measure(danmaku, paint, fromWorkerThread);
		}

		@Override
		public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
			paint.setColor(0x8125309b);
			canvas.drawRect(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2, paint);
		}

		@Override
		public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
			// 禁用描边绘制
		}
	}

	public DanmaManager(Context context, master.flame.danmaku.ui.widget.DanmakuView danmakuView) {
		this.context = context;
		this.danmakuSurfaceView = danmakuView;
		this.danmakuContext = DanmakuContext.create();


		// 设置最大显示行数
		HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
		maxLinesPair.put(TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
//		maxLinesPair.put(TYPE_SCROLL_LR, 5); // 滚动弹幕最大显示5行
		// 设置是否禁止重叠
		HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
		overlappingEnablePair.put(TYPE_SCROLL_RL, true);
//		overlappingEnablePair.put(TYPE_SCROLL_LR, true);
		overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
		overlappingEnablePair.put(BaseDanmaku.TYPE_SPECIAL, true);

		danmakuContext.
				setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).
				setDuplicateMergingEnabled(false).
				setMaximumVisibleSizeInScreen(-1).
				setScrollSpeedFactor(1.2f).
				setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter)
		.setMaximumLines(maxLinesPair);
		// baseDanmakuParser =
		// createParser(context.getResources().openRawResource(R.raw.comments));
		baseDanmakuParser = createParser(null);
		danmakuSurfaceView.setCallback(new DrawHandler.Callback()
		{
			@Override
			public void updateTimer(DanmakuTimer timer) {

			}

			@Override
			public void danmakuShown(BaseDanmaku baseDanmaku) {

			}

			@Override
			public void drawingFinished() {

			}

			@Override
			public void prepared() {
				danmakuSurfaceView.start();
			}
		});
		danmakuSurfaceView.prepare(baseDanmakuParser,danmakuContext);
		danmakuSurfaceView.enableDanmakuDrawingCache(true);
	}


	private BaseDanmakuParser createParser(InputStream stream) {

		if (stream == null) {
			return new BaseDanmakuParser()
			{
				@Override
				protected Danmakus parse() {
					return new Danmakus();
				}
			};
		}
//		ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
//		try {
//			loader.load(stream);
//		} catch (IllegalDataException e) {
//			e.printStackTrace();
//		}
//		BaseDanmakuParser parser = new BiliDanmukuParser();
//		IDataSource<?> dataSource = loader.getDataSource();
//		parser.load(dataSource);
		return new BaseDanmakuParser()
		{
			@Override
			protected Danmakus parse() {
				return new Danmakus();
			}
		};
	}

	public void init() {
		handler.removeCallbacksAndMessages(null);
	}

	public void hideSurface() {
		danmakuSurfaceView.hide();
	}

	public void showSurface() {
		danmakuSurfaceView.show();
	}

	public boolean danmaIsShowing(){
		return danmakuSurfaceView.getVisibility() == View.VISIBLE;
	}
	
	// 弹幕收到的类型
	public enum DanmaType
	{
		GIFT, CHAT, SYS
	}

	public void addChatDanma(String userid, String name, String content) {
		addDanma(userid, name, content, DanmaType.CHAT);
	}

	private void addDanma(String userid, String name, String content, DanmaType type) {
		if (!danmakuSurfaceView.isShown() || TextUtils.isEmpty(content))
			return;
		
		BaseDanmaku danmaku = createDanma(userid, type);
		if(danmaku == null)
			return;
		Log.i(TAG,"gift text color 2 = right??" +
				(danmaku.textColor == context.getResources().getColor(R.color.kk_danma_game_gift_text_color)?"true" :" false"));
		danmaku.text = name + "：" + content;
		sendDamma(danmaku);
	}

	private void sendDamma(BaseDanmaku danmaku){
		if(danmaku == null)
			return;
		Message msg = handler.obtainMessage(MSG_DANMA);
		msg.obj = danmaku;
		handler.sendMessage(msg);
	}
	private int[] mShowType = new int[]{BaseDanmaku.TYPE_SCROLL_RL,BaseDanmaku.TYPE_SCROLL_RL,BaseDanmaku.TYPE_FIX_TOP,BaseDanmaku.TYPE_SPECIAL,BaseDanmaku.TYPE_SPECIAL};
	private int[] mShowColor = new int[]{R.color.kk_danma_color1, R.color.kk_danma_color2, R.color.kk_danma_color3,
			R.color.kk_danma_color4, R.color.kk_danma_color5};
	private Random mRandom = new Random(System.currentTimeMillis());
	
	private BaseDanmaku createDanma(String userid, DanmaType type) {
		int index = mRandom.nextInt(4);
		int showType = mShowType[index];
		BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(showType);
		if (danmaku == null) {
			return null;
		}
		if (type == DanmaType.CHAT) {
			if (userid.equals((String) SharePrefsUtils.get(context, "user", "userId", ""))) {
				danmaku.textColor = context.getResources().getColor(mShowColor[mRandom.nextInt(10000) % 5]);
//				danmaku.textColor = context.getResources().getColor(R.color.kk_danma_my_text_color);
			} else {
				danmaku.textColor = context.getResources().getColor(mShowColor[mRandom.nextInt(10000) % 5]);
			}
		} else if (type == DanmaType.GIFT) {
//			if (userid != GameSetting.getInstance().getUserId()) {
				danmaku.textColor = context.getResources().getColor(R.color.kk_danma_game_gift_text_color);

			Log.i(TAG,"gift text color 1 = right??" +
					(danmaku.textColor == context.getResources().getColor(R.color.kk_danma_game_gift_text_color)?"true" :" false"));
//			} else {
//				danmaku.textColor = context.getResources().getColor(R.color.kk_danma_my_text_color);
//			}
		}
		danmaku.padding = 15;
		danmaku.priority = 1;
		danmaku.setTime(danmakuSurfaceView.getCurrentTime() + 200);
		danmaku.isLive = true;
		danmaku.textSize = DisplayUtil.sp2px(context, 18);
		danmaku.textShadowColor = context.getResources().getColor(R.color.kk_danma_my_text_alpha);
		danmaku.borderColor = 0;
		if(showType == BaseDanmaku.TYPE_FIX_TOP) {
			danmaku.setDuration(new Duration(2500));
			danmaku.padding = 20;
		} else {
			danmaku.setDuration(new Duration(5000));
		}

		return danmaku;
	}

	public void addGiftDanma(String userid, String content) {
		addDanma(userid, null, content, DanmaType.GIFT);
	}

	public void destroy() {
		release();
		handler.removeCallbacksAndMessages(null);
	}

	private void release() {
		if (danmakuSurfaceView != null) {
			danmakuSurfaceView.release();
			danmakuSurfaceView = null;
		}
	}

	public void pause() {
		if (danmakuSurfaceView != null && danmakuSurfaceView.isPrepared()) {
			danmakuSurfaceView.pause();
		}
	}

	public void resume() {
		if (danmakuSurfaceView != null && danmakuSurfaceView.isPrepared() && danmakuSurfaceView.isPaused()) {
			danmakuSurfaceView.resume();
		}
	}

	public void addDanmaKuShowText(String winString, String content, String endContent) {
		Log.v(TAG, "addDanmaKuShowText : "+winString+" , "+content +" , "+endContent);
		BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(TYPE_SCROLL_RL);
		if (danmaku == null || TextUtils.isEmpty(content)) {
			return;
		}
		Log.v(TAG, "addDanmaKuShowText danmaku = " + danmaku);
		SpannableStringBuilder spanFront = new SpannableStringBuilder(content);
		spanFront.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)), 0, spanFront.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		SpannableStringBuilder spanRich = new SpannableStringBuilder(winString);
		spanRich.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.kk_danma_game_gift_text_color)), 0, spanRich.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		SpannableStringBuilder spanEnd = new SpannableStringBuilder(endContent);
		spanEnd.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)), 0, spanEnd.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanFront.append(spanRich).append(spanEnd);
		danmaku.text = spanFront;
		danmaku.padding = 15;
		danmaku.priority = 1;
		danmaku.isLive = true;
		danmaku.setTime(danmakuSurfaceView.getCurrentTime() + 200);
		Log.v(TAG, "addDanmaKuShowText 1 ");
		danmaku.textColor = context.getResources().getColor(R.color.white);
		Log.v(TAG, "addDanmaKuShowText 2 ");
		danmaku.textShadowColor = 0;
		danmaku.textSize = DisplayUtil.sp2px(context, 18f);
		Log.v(TAG, "addDanmaKuShowText 3 ");
		Message msg = handler.obtainMessage(MSG_DANMA);
		msg.obj = danmaku;
		handler.sendMessage(msg);
		Log.v(TAG, "addDanmaKuShowText end ");
	}

}
