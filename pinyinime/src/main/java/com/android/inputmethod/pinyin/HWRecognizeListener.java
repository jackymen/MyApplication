package com.android.inputmethod.pinyin;

import java.util.ArrayList;


/**
 *
 * @author jacko
 *
 */
public interface HWRecognizeListener{
	
	public void OnStartRecognize();
	public void OnFinlishRecognize(ArrayList<String> CandidateList);

}
