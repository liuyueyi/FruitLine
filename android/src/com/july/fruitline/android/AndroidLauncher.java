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
						channelId);// ����й�����ʾ����ʾ�����Զ�������һ�����
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
			msg.what = tag; // ˽�о�̬�����ͱ����������������ж���ֵ
			handler.sendMessage(msg);
		}
	};

	static MainGame game;
	RelativeLayout.LayoutParams adParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// ��������������api֮ǰ���ô�api
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
						// �з������͵ĺ��������֮�����
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

		// Umeng �Զ�������
		UmengUpdateAgent.update(this);
	}

	private void initAd() {
		// �ṩ��Ӧ�ص��ӿڣ����Բ����ã�����setListner����д��load����ǰ
		MyMediaManager.setListner(new MyMDListner() {
			@Override
			public void onMDShow() {
				System.out.println("�����ʾ");// ������Խ���ֹͣ��Ϸ
			}

			@Override
			public void onMDClose() {
				System.out.println("���ر�");
			}

			@Override
			public void onInstanll(int id) {
				System.out.println("��氲װid��" + id);
			}

			@Override
			public void onMDLoadSuccess() {
				System.out.println("�����سɹ�");
				// ����ʵ�ֹ��������������ʾ��棬�����÷����Բ鿴�����ĵ��ġ���������ʱ������
				// ��������ο������ĵ�����˵����Context context
			}

			@Override
			public void onMDExitInFinish() {
				System.out.println("�ڲ��˳����˳���ť�ص�");
			}

			@Override
			public void onMDExitOutFinish() {
				System.out.println("�ⲿ�˳����˳���ť�ص�");
			}
		});

		// ������
		// ��������ο������ĵ�����˵����Context context,String cooId,String channelId
		MyMediaManager.load(AndroidLauncher.this, cooID, channelId);

		AppConnect.getInstance(this);

	}

	private void initUmeng() {
		UMGameAgent.setDebugMode(true);// �����������ʱ��־
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
