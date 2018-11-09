package com.sharkwang8.phoneassistant.util;

import android.content.Context;
import android.util.Log;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AdUtil {
    private static final String TAG = AdUtil.class.getSimpleName();
    //广告出现的时间
    public static long SHOW_TIME = System.currentTimeMillis();
    public static void showFacebookAds(final Context context){
//        AdSettings.addTestDevice("386dcd1a-4ea0-4757-889c-5c8a5a6271bb");
        final InterstitialAd interstitialAd = new InterstitialAd(context, "302328470589163_302330743922269");
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext(AppUtil.getAid(context));
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String aid) throws Exception {
                        String ip = AppUtil.getIPAddress(context);
                        if(aid == null) {
                            Log.e(TAG, "get Google Advertising ID failed!");
                            aid = "";
                        }


                        Map<String,Object> eventValues = new HashMap<>();
                        eventValues.put(AFInAppEventParameterName.AD_REVENUE_NETWORK_NAME, ip);
                        eventValues.put(AFInAppEventParameterName.ACHIEVEMENT_ID, aid);
                        AppsFlyerLib.getInstance().trackEvent(context, AFInAppEventType.PURCHASE, eventValues);
                        Log.d(TAG, "Interstitial ad ip is " + ip);
                        Log.d(TAG, "Interstitial ad aid is " + aid);
                    }
                });
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
                SHOW_TIME = System.currentTimeMillis();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
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
}
