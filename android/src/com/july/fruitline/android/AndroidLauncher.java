package com.july.fruitline.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import cn.waps.AppConnect;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.july.fruitline.BushEvent;
import com.july.fruitline.MainGame;
import com.july.fruitline.util.Assets;
import com.july.fruitline.util.Constants;
import com.mobgi.android.MobgiAd;
import com.pkag.m.MyMDListner;
import com.pkag.m.MyMediaManager;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.update.UmengUpdateAgent;

public class AndroidLauncher extends AndroidApplication {
	static RelativeLayout layout;
	static Context mContext;
	static String cooID = "d2ce29c396364069b9a5416dbbf788bc";
	static String channelId = "k-360";

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constants.EXIT:
				MyMediaManager.showExitInDialog((Activity) mContext, cooID,
						channelId);// 如果有广告就显示，显示完后会自动加载下一条广告
				break;
			case Constants.CHAPING:
				// MyMediaManager.show((Activity) mContext);
				MobgiAd.showCachedPromotionAd((Activity) mContext,
						"MC42MzAwNDgwMCAxNDE5Mjk_REJEQjhB",
						new MobgiAd.PromotionAdEventListener() {
							@Override
							public void onAdReady() {
							}

							@Override
							public void onAdPresent() {
							}

							@Override
							public void onAdFailed() {
							}

							@Override
							public void onAdDismiss() {
							}

							@Override
							public void onAdActionCallback(String arg0) {
							}
						});
				break;
			case Constants.KAIPING:
				// MyMediaManager.show((Activity) mContext);
				MobgiAd.activatePromotionAd((Activity) mContext,
						"MC42MzAwNDgwMCAxNDE5Mjk_REJEQjhB",
						new MobgiAd.PromotionAdEventListener() {

							@Override
							public void onAdReady() {
							}

							@Override
							public void onAdPresent() {
							}

							@Override
							public void onAdFailed() {
							}

							@Override
							public void onAdDismiss() {
							}

							@Override
							public void onAdActionCallback(String arg0) {
							}
						});

				break;
			case Constants.ADWALL:
				AppConnect.getInstance(mContext).showOffers(mContext);
				// MobgiAd.showListAdView((AndroidLauncher) mContext,
				// "MC4xMTI3NDYwMCAxNDE5Mjk_REJEQjhB",
				// new AdListEventListener() {
				//
				// @Override
				// public void onAdDismiss() {
				//
				// }
				// });
				break;
			case Constants.START_LEVEL:
				UMGameAgent.startLevel("lv_"
						+ Assets.getInstance().currentLevel);
				break;
			case Constants.FAIL_LEVEL:
				UMGameAgent
						.failLevel("lv_" + Assets.getInstance().currentLevel);
				break;
			case Constants.FINISH_LEVEL:
				UMGameAgent.finishLevel("lv_"
						+ Assets.getInstance().currentLevel);
				break;
			}
		}
	};

	BushEvent event = new BushEvent() {
		@Override
		public void notify(Object o, int tag) {
			Message msg = handler.obtainMessage();
			msg.what = tag; // 私有静态的整型变量，开发者请自行定义值
			handler.sendMessage(msg);
		}
	};

	static MainGame game;
	RelativeLayout.LayoutParams adParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 必须在所有其他api之前调用此api
		super.onCreate(savedInstanceState);
		initUmeng();
		mContext = this;
		initAd();

		layout = new RelativeLayout(this);
		adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		game = new MainGame(event);
		View gameView = initializeForView(game, config);

		layout.addView(gameView);
		MobgiAd.initialize(this, "DBDB8AFE62E4B756A465",
				new MobgiAd.InitializeListener() {

					@Override
					public void onInitializeFinish() {
						// 有返回类型的函数在这个之后调用
						View banner = MobgiAd.generateBannerView(
								(AndroidLauncher) mContext,
								"MC45MDg1NjcwMCAxNDE5Mjk_REJEQjhB",
								new MobgiAd.BannerAdEventListener() {

									@Override
									public void onAdReady() {

									}

									@Override
									public void onAdFailed() {

									}

									@Override
									public void onAdActionCallback(String arg0) {

									}
								});
						layout.addView(banner, adParams);
					}
				});

		setContentView(layout);

		// Umeng 自动检测更新
		UmengUpdateAgent.update(this);
	}

	private void initAd() {
		// 提供相应回调接口，可以不调用，建议setListner方法写在load方法前
		MyMediaManager.setListner(new MyMDListner() {
			@Override
			public void onMDShow() {
				System.out.println("广告显示");// 这里可以进行停止游戏
			}

			@Override
			public void onMDClose() {
				System.out.println("广告关闭");
			}

			@Override
			public void onInstanll(int id) {
				System.out.println("广告安装id：" + id);
			}

			@Override
			public void onMDLoadSuccess() {
				System.out.println("广告加载成功");
				// 这里实现广告加载完成立即显示广告，更多用法可以查看开发文档的“方法调用时机”；
				// 具体参数参考开发文档参数说明：Context context
			}

			@Override
			public void onMDExitInFinish() {
				System.out.println("内部退出框退出按钮回调");
			}

			@Override
			public void onMDExitOutFinish() {
				System.out.println("外部退出框退出按钮回调");
			}
		});

		// 请求广告
		// 具体参数参考开发文档参数说明：Context context,String cooId,String channelId
		MyMediaManager.load(AndroidLauncher.this, cooID, channelId);

		AppConnect.getInstance(this);

	}

	private void initUmeng() {
		UMGameAgent.setDebugMode(true);// 设置输出运行时日志
		UMGameAgent.init(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMGameAgent.onResume(this);

		MobgiAd.setCurrentActivity(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		UMGameAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppConnect.getInstance(this).close();
		MobgiAd.destory();
	}
}
