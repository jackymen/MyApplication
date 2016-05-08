package com.android.inputmethod.pinyin;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



public class HandWritingBoardView extends View {
      
	static private final int DELAY_TIME=600;
    private Path    mPath;
    private	Paint 	mPaint;
    static final Object mlock=new Object();
    private static final short STROKEENDMARK=-1;
    private ArrayList<Long> PointData; 
    private ArrayList<String> mCandidateList=new ArrayList<String>();
    private HWRecognizeTimer mHWRecognizeTask=new HWRecognizeTimer();
    private HWRecognizeListener mHWRecognizeListener=null;
    private IHwDecoderService mIHwDecoderService=null;
    private ServiceConnection mServiceConnection=null;
    /*
     * 手写识别范围
     */
    private int mDwRange=0;
    
    
    /* ------------------手写识别范围---------------------- */
    public final static int        ALC_SC_COMMON	=	0x00000001;		/* 绠�綋涓�骇瀛�*/
    public final static int        ALC_SC_RARE		=	0x00000002;		/* 绠�綋浜岀骇瀛�*/
    public final static int        ALC_TC_TO_SC	=	0x00000020;		/* 璇嗗埆绻佷綋瀛楋紝杈撳嚭瀵瑰簲鐨勭畝浣撳瓧锛屽彧閫傜敤浜庣畝浣撶増 */
    public final static int        ALC_SC_RADICAL	=	0x00000080;	  /* 鍋忔梺閮ㄩ锛屽彧閫傜敤浜庣畝浣撶増 */
    public final static int        ALC_NUMERIC		=	0x00000100;		/* 鏁板瓧 0-9 ) */
    public final static int        ALC_UCALPHA		=	0x00000200;		/* 澶у啓瀛楁瘝( A-Z ) */
    public final static int        ALC_LCALPHA		=	0x00000400;		/* 灏忓啓瀛楁瘝 ( a-z ) */
    public final static int        ALC_PUNC_COMMON	=	0x00000800;		/* 甯哥敤鏍囩偣 
        												0021 !     0022 "     201C 鈥�    201D 鈥� 
        												002C ,     003A :     003B ;     003F ?     
                                                        3001 銆�   3002 銆�*/
    public final static int        ALC_PUNC_RARE	=	0x00001000;		/* 鎵╁睍鏍囩偣 
        												0027 '    0028  (    0029 )    2014 鈥�   
        												2026 鈥�    3008 銆�  3009 銆�  300A 銆�  
        												300B 銆�  */
    public final static int       ALC_SYM_COMMON	=	0x00002000;		/* 甯哥敤绗﹀彿 
        												0023 #    0024 $     0025 %      0026 &    
        												002A *    002B +     002D -      002E  .    
        												002F /    003C <     003D =      003E >   
        												0040 @    FFE1 锟�   FFE5 锟�    20AC 锟�   */
    public final static int        ALC_SYM_RARE	=	0x00004000;		/* 鎵╁睍绗﹀彿
        												005B  [   005C \   005D  ]     005E  ^      
                                                        005F  _   0060 `   007B  {     007C  |      
                                                        007D  }   007E ~   */
    public final static int    ALC_GESTURE		=	0x00008000;		/* 鎺у埗鎵嬪娍
        											  Space(0x20), Carriage Return(0x0d), 
                                                      Backspace(0x08), Table 0x0009	*/
    public final static int   ALC_CS_CURSIVE  =  0x00010000;      /* 琛岃崏瀛�*/
    
    
    public final static int   REC_FORMULA  =  0x10000000; /*公式识别，调用公式识别库*/

    /* -----------------甯哥敤缁勫悎----------------------------- */
    public final static int RECOGNIZE_RANGE_CHARSET_CNDEFAULT	=(ALC_SC_COMMON|ALC_SC_RARE);//缂虹渷绠�綋涓枃璇嗗埆
    public final static int RECOGNIZE_RANGE_CHARSET_SYMPUNC	=(ALC_PUNC_COMMON|ALC_PUNC_RARE);//绗﹀彿鏍囩偣
    public final static int RECOGNIZE_RANGE_CHARSET_ENGLISH	=(ALC_UCALPHA|ALC_LCALPHA);//鑻辨枃璇嗗埆


    
    
    public void SetDwRange(int DwRange){
    	mDwRange=DwRange;
    }
    
    public int GetDwRange(){
    	return mDwRange;
    }
    
    /**
     * 
     *description: 笔迹识别任务
     *
     */
    private class HWRecognizeTimer extends Handler implements Runnable {

    	static final int MEG_RECOGNIZE_FINISH=0x01;
    	
    	
    	public HWRecognizeTimer() {
			super();
		}
    	
		@Override
		public void run() {
			
			if(mHWRecognizeListener!=null){
				mHWRecognizeListener.OnStartRecognize();
			}
			
			synchronized (mlock) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int[] PointDataTemp=GetPointData();	
						String[] CandidateList;
						try {
							CandidateList = mIHwDecoderService.Hw_GetHWRecognize(PointDataTemp,mDwRange);
							Reset();
							if(CandidateList==null||CandidateList.length<=0){
								postInvalidate();
								return;
							}
							for(int i=0;i<CandidateList.length;i++){
								mCandidateList.add(CandidateList[i]);
							}
							Message msg=new Message();
							msg.what=MEG_RECOGNIZE_FINISH;
							sendMessage(msg);
							postInvalidate();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();		
			}
	
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch (msg.what) {
			case MEG_RECOGNIZE_FINISH:
				if(mHWRecognizeListener!=null){
					ArrayList<String> CanList= new ArrayList<String>();
					CanList.addAll(mCandidateList);
					mHWRecognizeListener.OnFinlishRecognize(CanList);
				}
				return;

			default:
				break; 
			}
			super.handleMessage(msg);
		}
		
        void postDelayedHWRecognize(long delayMillis) {
        		postDelayed(this, delayMillis);
        }

        void cancelHWRecognize() {
            removeCallbacks(this);
        }
		 	
    }
    
    
    public void SetHWRecognizeListener(HWRecognizeListener HWRecognizeListener){
    	mHWRecognizeListener=HWRecognizeListener;
    }
    
    public HWRecognizeListener GetHWRecognizeListener(){
    	return mHWRecognizeListener;
    }
    
    
    private boolean startHwDecoderService() {
        if (null == mIHwDecoderService) {
            Intent serviceIntent = new Intent(HwDecoderService.RECOGNIZE_ACTION);

            // Bind service
            mServiceConnection=new ServiceConnection() {
				
				@Override
				public void onServiceDisconnected(ComponentName name) {
					// TODO Auto-generated method stub
					mIHwDecoderService=null;
				}
				
				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					// TODO Auto-generated method stub
					mIHwDecoderService=IHwDecoderService.Stub.asInterface(service);
				};
            };	
            if (getContext().bindService(serviceIntent, mServiceConnection,			
                    Context.BIND_AUTO_CREATE)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
    
    
    public HandWritingBoardView(Context c) {
        super(c);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
        mPath = new Path();  
        PointData=new ArrayList<Long>();   
        startHwDecoderService();
    }
    
    public HandWritingBoardView(Context context,AttributeSet attr)  
    {  
        super(context,attr);  
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
        mPath = new Path();
        PointData=new ArrayList<Long>();
        startHwDecoderService();
    }  
    
    public void Reset()
    {
    	mPath.reset();
    	synchronized(PointData){
    	PointData.clear();
    	}
    	mCandidateList.clear();
    }
    
    
    public int[] GetPointData()
    {
    	synchronized(PointData){
    	int i=0;
    	long point;
    	int size=PointData.size();
    	int[] Pd=new int[(size+1)];
    	
    	if(size==0) return null;
    	for(;i<size;i++)
    	{
    		point=PointData.get(i);
    		Pd[i]=(int)(point&0xffffffff);
    	}
		Pd[i]=((short)STROKEENDMARK<<16)|(STROKEENDMARK);   
    	return Pd;
      }
    }
    
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {     
        canvas.drawPath(mPath, mPaint);
    }
    
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private long  PointXY;
    
    private void touch_start(float x, float y) {
       // mPath.reset();
    	mHWRecognizeTask.cancelHWRecognize();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        PointXY=(short)mX|(((short)mY)<<16);
        synchronized(PointData){
        PointData.add(PointXY);
        }
    }
    private Boolean touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
            PointXY=(short)mX|(((short)mY)<<16);
            synchronized(PointData){
            PointData.add(PointXY);
            }
            return true;
        }
        return false;
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        PointXY=(short)mX|(((short)mY)<<16);
        synchronized(PointData){
        PointData.add(PointXY);
        
        PointXY=STROKEENDMARK&0xffff;
        PointData.add(PointXY);
        }
        mHWRecognizeTask.postDelayedHWRecognize(DELAY_TIME);
          
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if(touch_move(x, y)){
                	invalidate();
                }           
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
    
    @Override
    protected void finalize() throws Throwable {
    	// TODO Auto-generated method stub
    	super.finalize();
    	if(mIHwDecoderService!=null){
    		
    		
    		getContext().unbindService(mServiceConnection);
    		mIHwDecoderService=null;
    	}
    }
    
    
    public void destroy(){
    	// TODO Auto-generated method stub
    	if(mIHwDecoderService!=null){
    		
    		
    		getContext().unbindService(mServiceConnection);
    		mIHwDecoderService=null;
    	}
    }
    
    
}
