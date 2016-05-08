package com.android.inputmethod.pinyin;

import android.content.Context;
import android.content.res.AssetManager;

public class HwDecoder {

	static final String mfilename = "Dic_GB2312_L.bin";
	AssetManager mAss;

	public HwDecoder(Context context) {
		mAss = context.getAssets();
	}

	native static boolean nativeInit_hw_engine(AssetManager ass, String filename);

	native static void nativeEng_Uninit_hw_engine();

	native static String[] nativeEng_GetHWRecognize(int[] PointData,int dwRange);

	static {
			
		System.loadLibrary("hw_eng");

	}

	private boolean Init_hw_engine(AssetManager ass, String filename) {
		return nativeInit_hw_engine(ass, filename);
	}

	private void Eng_Uninit_hw_engine() {
		nativeEng_Uninit_hw_engine();
	}

	public String[] Eng_GetHWRecognize(int[] PointData,int dwRange) {

		if (!Init_hw_engine(mAss, mfilename))
			return null;
		return nativeEng_GetHWRecognize(PointData,dwRange);
	}

}
