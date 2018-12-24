package com.sharkwang8.phoneassistant.util;

import android.content.Context;
import android.util.Log;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.sharkwang8.phoneassistant.HttpCenter;
import com.sharkwang8.phoneassistant.HttpResult;
import com.sharkwang8.phoneassistant.bean.AdInfo;
import com.sharkwang8.phoneassistant.event.EmptyEvent;
import com.sharkwang8.phoneassistant.util.RxBus.RxBus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AdUtil {
    //广告出现的时间
    public static long SHOW_TIME = 0;
    public static int AD_TYPE = AdUtil.TYPE_FACEBOOK;
    /**
     * 1 开启
     * 0 关闭
     */
    public static int AD_STATUS = 1;
    public static long AD_TIME = 3600000;

    private static final int TYPE_FACEBOOK = 1;
    private static final int TYPE_ADMOB = 2;
    private static final String TAG = AdUtil.class.getSimpleName();

    /**
     * 显示广告
     */
    public static void showAds(Context context, String source) {
        switch (AdUtil.AD_TYPE) {
            case TYPE_FACEBOOK:
                AdUtil.showFacebookAds(context);
                break;
            case TYPE_ADMOB:
                AdUtil.showAdModAds(context);
                break;
            default:
                //判断Fackbook是否安装
                if (AppUtil.isInstalled(context, "com.facebook.katana")) {
                    AdUtil.showFacebookAds(context);
                } else {
                    AdUtil.showAdModAds(context);
                }
                break;
        }
        Log.e(TAG, "Interstitial ad at " + source);
    }

    private static void showFacebookAds(final Context context) {
//        final String PLACEMENT_ID = "302328470589163_302330743922269";
        final String PLACEMENT_ID = "302328470589163_329962474492429";
//        AdSettings.addTestDevice("386dcd1a-4ea0-4757-889c-5c8a5a6271bb");
        final com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(context, PLACEMENT_ID);
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            String TAG = "Facebook";

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                if(interstitialAd != null) {
                    interstitialAd.destroy();
                }
                RxBus.getDefault().post(new EmptyEvent());
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                if(interstitialAd != null) {
                    interstitialAd.destroy();
                }
                RxBus.getDefault().post(new EmptyEvent());
            }

            @Override
            public void onAdLoaded(Ad ad) {
//                Observable.create(new ObservableOnSubscribe<String>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<String> e) throws Exception {
//                        e.onNext(AppUtil.getAid(context));
//                    }
//                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String aid) throws Exception {
//                        String ip = AppUtil.getIPAddress(context);
//                        if (aid == null) {
//                            Log.e(TAG, "get Google Advertising ID failed!");
//                            aid = "";
//                        }
//
//
//                        Map<String, Object> eventValues = new HashMap<>();
//                        eventValues.put(AFInAppEventParameterName.AD_REVENUE_NETWORK_NAME, ip);
//                        eventValues.put(AFInAppEventParameterName.ACHIEVEMENT_ID, aid);
//                        AppsFlyerLib.getInstance().trackEvent(context, AFInAppEventType.AD_CLICK, eventValues);
//                    }
//                });
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");

                AdUtil.onAdClick(context, TAG, PLACEMENT_ID);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

    private static void onAdClick(Context context, String channel, String placementId) {
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.AF_CHANNEL, channel);
        eventValues.put(AFInAppEventParameterName.AD_REVENUE_PLACEMENT_ID, placementId);
        AppsFlyerLib.getInstance().trackEvent(context, AFInAppEventType.AD_CLICK, eventValues);
    }

    private static void showAdModAds(final Context context) {
        //初始化AdMob
        MobileAds.initialize(context);
        final String UNIT_ID = "ca-app-pub-8013994383371748/1342340443";
        //初始化Interstitial Ads
        final com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(context);
        interstitialAd.setAdUnitId(UNIT_ID);
        AdRequest request = new AdRequest.Builder()
//                .addTestDevice("3354EE0DE60D4DE6C845A1C28842FDEA")
                .build();
        interstitialAd.loadAd(request);
        //初始化成功以后直接显示
        interstitialAd.setAdListener(new AdListener() {
            String TAG = "AdMob";

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                interstitialAd.show();
                Log.e(TAG, "Interstitial ad onAdLoaded.");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e(TAG, "Interstitial ad onAdFailedToLoad. errorCode is " + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d(TAG, "Interstitial ad onAdOpened.");
                AdUtil.onAdClick(context, TAG, UNIT_ID);
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.e(TAG, "Interstitial ad onAdLeftApplication.");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.d(TAG, "Interstitial ad onAdClosed.");
            }
        });
    }

    /**
     * 获取最新的广告配置
     */
    public static void getAdType() {
        AdUtil.getAdTypeAndShow(null, null);
    }

    /**
     * 获取最新的广告配置并展示
     */
    public static void getAdTypeAndShow(final Context context, final String source) {
        HttpCenter.getService().getAdType("getad_type", "Phone Assistant").subscribeOn(Schedulers.io())//指定网络请求所在的线程
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//指定的是它之后（下方）执行的操作所在的线程
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (context != null) {
                            AdUtil.showAds(context, source);
                        }
                    }
                })
                .subscribe(new Observer<HttpResult<AdInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(HttpResult<AdInfo> adInfoHttpResult) {
                        if (adInfoHttpResult.getResult() == 0) {
                            if (adInfoHttpResult.getData() != null) {
                                AdInfo adInfo = adInfoHttpResult.getData();
                                AdUtil.AD_TYPE = adInfo.getAd_type();
                                AdUtil.AD_STATUS = adInfo.getAd_status();
                                AdUtil.AD_TIME = adInfo.getAd_time();
                                Log.d(TAG, "Interstitial ad type is [" + adInfo.getAd_name() + "] now!");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Interstitial ad type isn't Changed!");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}