package com.android.inputmethod.pinyin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class HwDecoderService extends Service {

	public static final String RECOGNIZE_ACTION="com.android.inputmethod.pinyin.HwDecoder_Service"; 
	
	private IHwDecoderService.Stub mBinder=new IHwDecoderService.Stub() {
		
		@Override
		public String[] Hw_GetHWRecognize(int[] PointData,int dwRange) throws RemoteException {
			// TODO Auto-generated method stub
			if(PointData==null||PointData.length<=0)
				return null;
			String[] CandidateList;
			if((dwRange&HandWritingBoardView.REC_FORMULA)!=0){
				CandidateList=new HwFormulaDecoder(HwDecoderService.this).Eng_GetHWRecognize(PointData,dwRange);
				if(CandidateList!=null&&CandidateList.length==1)
					CandidateList[0]="$${"+CandidateList[0]+"}$$"; //
			}else{
				CandidateList=new HwDecoder(HwDecoderService.this).Eng_GetHWRecognize(PointData,dwRange);
			}						
			return CandidateList;
		}
	}; 
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

}
