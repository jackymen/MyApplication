package com.android.inputmethod.pinyin;

import android.content.Context;

public class HwFormulaDecoder {


	public HwFormulaDecoder(Context context) {
		
	}

	native static boolean nativeInit_hwFormula_engine();

	native static void nativeEng_Uninit_hwFormula_engine();

	native static String[] nativeEng_GetHWFormulaRecognize(int[] PointData);

	static {
			
		System.loadLibrary("hw_Formula");

	}

	private boolean Init_hwFormula_engine() {
		return nativeInit_hwFormula_engine();
	}

	private void Eng_Uninit_hwFormula_engine() {
		nativeEng_Uninit_hwFormula_engine();
	}

	public String[] Eng_GetHWRecognize(int[] PointData,int dwRange) {

		if (!Init_hwFormula_engine())
			return null;
		return nativeEng_GetHWFormulaRecognize(PointData);
	}

}
