package com.hanvon.test;

import java.io.File;

import android.content.Context;
import android.os.Environment;




public class FormulaDraw {

	private CommandInfo commands = new CommandInfo();
	
	
	static 
	{
		System.loadLibrary("Slh_FormulaDraw");	
		//System.load(MainActivity.dbPath + "libSlh_FormulaDraw.so");
	}
	
	private static native int DrawFormula(  Object commandInfo );
	private static native String RecognizeFormula( short[] points );
	/*
	 * param points:input trace, separated by (-1,0) in strokes, ended by (-1,-1)
	 * param sdkCommandInfo: recognize and display params object. see call example
	 * param sdkCommandInfoPack: sdkCommandInfo package name
	 * return: int, see ErrorCode defined in sdkCommandInfo class
	 */
	private static native int SdkDrawFormula( short[] points, Object sdkCommandInfo, String sdkCommandInfoPack );
	
	private static native String HWGetResult( int nIdx );
	private Context mContext;
	 
	public FormulaDraw( Context context )
	{
		mContext = context;
	}
	
	public void draw( )
	{
		String[] cmds = {"-d","-f", "/mnt/asec/temp1.tex", "-s", "5", "-e", "/mnt/asec/d.gif" };
		commands.commandlines = cmds;  
		DrawFormula( commands );
	}
	
	private static final Boolean bIsUseSDCard = false;
	public static final String tempFileName = "/temp1.gif";
	public static final String tempTraceFileName = "/temp.";
	
	public String getSDPath()
	{ 
		String sdDir = ""; 
		boolean sdCardExist = Environment.getExternalStorageState() 
		.equals(android.os.Environment.MEDIA_MOUNTED); //
		if (sdCardExist) 
		{ 
			sdDir = Environment.getExternalStorageDirectory().getPath();//鑾峰彇璺熺洰褰�
		} 
		return sdDir; 
	} 

	
	public String getResultFileName()
	{
		if( !bIsUseSDCard )
		{
			Context cont = mContext.getApplicationContext();
			String fileDir = cont.getFilesDir().getPath(); 
			return fileDir;
		}
		else
		{
			
			return getSDPath();
		}
	}
	
	public void draw( String result )
	{
		String str =  getResultFileName();		
		File f = new File( str );
		if( !f.exists() )
		{
			f.mkdir();
		}
		//String[] cmds = {"-d", result, "-s", "5", "-e", getResultFileName() + tempFileName };
		String[] cmds = {"-d", result, "-s", "7", "-e", getResultFileName() + tempFileName };
		commands.commandlines = cmds;  
		DrawFormula( commands );
	}
	public void draw( String result, String fileName )
	{
		String str =  getResultFileName();		
		File f = new File( str );
		if( !f.exists() )
		{
			f.mkdir();
		}
		File destF = new File(getResultFileName() + "/"+ fileName);
		if( destF.exists() )
		{
			destF.delete();
		}
		
		String[] cmds = {"-d", result, "-s", "7", "-e", destF.getAbsolutePath() };
		commands.commandlines = cmds;  
		DrawFormula( commands );
	}
//	public void sdkDraw( short[] points )
//	{
//		SdkCommandInfo info = new SdkCommandInfo();
//		info.picFormat = SdkCommandInfo.PicFormat.Format_Gif;		
//		info.fontScale = SdkCommandInfo.FontLevel.Font_Huge;
//		info.fontColor = SdkCommandInfo.FontColor.Color_Red;
//		info.fBox = SdkCommandInfo.BOX;
//		info.inputExpressfileName ="";//
//		info.picScale = SdkCommandInfo.MAGBOX + "2";
//		info.outputFileName = getResultFileName() + tempFileName;
//		
//		File destF = new File(info.outputFileName);
//		if( destF.exists() )
//		{
//			destF.delete();
//		}
//		
//		SdkDrawFormula( points, info, "com/hanvon/test/SdkCommandInfo" );
//	}
	
	public String GetResult( int nIdx )
	{
		return HWGetResult( nIdx );
	}
	
	public String test( short[] outPoints )
	{
		if( outPoints == null )
		{
			return RecognizeFormula( testPoints );
		}
		else
		{
			return RecognizeFormula( outPoints );
		}
	}
	private short[] testPoints =			
		{
			12,24,12,25,11,25,11,26,12,26,12,27,12,29,12,32,12,36,12,41,11,52,
			10,70,9,80,8,90,7,100,6,109,6,116,5,121,5,126,5,129,6,132,
			6,134,6,133,6,134,6,134,-1,0,8,30,9,29,11,29,13,29,15,30,
			19,31,24,33,28,36,32,40,35,43,37,47,37,51,36,55,36,57,35,59,
			33,64,29,70,23,75,19,78,14,81,9,84,6,85,3,86,1,86,1,86,
			-1,0,76,37,76,39,76,40,75,40,74,41,72,41,70,43,66,46,61,52,
			57,57,54,63,50,70,47,77,46,82,46,87,46,92,47,96,48,100,50,103,
			52,104,54,106,58,108,59,108,59,108,-1,0,95,55,96,55,95,55,96,56,
			96,57,95,58,94,62,93,66,92,71,91,77,89,88,88,93,88,98,88,102,
			87,105,87,108,87,107,87,109,87,110,87,110,-1,0,91,62,92,60,93,59,
			94,58,97,56,98,55,100,54,102,54,104,54,106,55,108,56,109,57,110,59,
			110,61,110,64,109,66,107,69,104,71,101,74,97,77,94,79,92,80,92,81,
			91,81,90,82,91,83,92,83,93,83,96,85,99,87,100,88,102,90,103,92,
			104,94,104,97,104,100,103,102,102,104,101,107,98,111,95,113,92,115,90,117,
			89,117,88,117,88,117,-1,0,144,50,146,50,147,50,148,51,150,51,153,53,
			155,54,159,58,161,61,164,64,165,68,166,73,167,78,166,80,165,82,162,92,
			159,97,155,101,153,104,150,107,148,109,145,112,144,113,144,113,-1,0,189,78,
			192,78,194,77,196,77,198,77,202,76,206,76,210,75,215,75,220,75,224,75,
			228,75,231,75,234,75,235,75,235,75,-1,0,199,89,198,90,198,91,197,91,
			198,91,198,90,199,91,201,91,205,91,210,91,212,91,217,91,221,91,227,91,
			231,91,236,91,240,91,246,91,246,91,-1,0,283,57,284,57,285,57,286,56,
			289,56,293,56,298,55,303,55,310,55,316,55,330,55,337,55,344,55,350,55,
			355,55,359,55,362,55,361,55,362,55,362,55,-1,0,288,59,287,59,288,59,
			289,60,291,60,295,61,298,61,303,62,307,63,313,64,317,65,320,65,322,66,
			323,67,323,68,322,69,320,70,316,73,310,76,304,79,299,81,294,83,289,84,
			285,86,281,87,279,87,278,88,279,88,282,88,285,89,292,89,295,89,302,89,
			309,90,323,90,329,90,335,90,340,90,343,90,342,90,343,90,345,90,346,90,
			347,90,347,90,-1,0,274,111,275,111,276,111,277,111,279,111,281,112,284,112,
			284,113,285,114,285,115,285,118,284,120,282,123,281,125,278,129,276,133,275,136,
			275,138,275,140,275,141,276,143,277,144,279,145,281,146,282,146,285,146,286,146,
			286,146,-1,0,287,100,287,101,287,102,288,103,289,105,289,106,289,106,-1,0,
			297,122,299,122,300,122,301,122,302,122,303,122,305,122,308,122,313,122,316,122,
			320,123,324,123,327,123,330,123,332,123,332,123,-1,0,302,134,303,134,304,134,
			305,135,308,135,310,136,316,136,320,137,322,137,325,137,328,137,329,137,329,137,
			-1,0,349,112,349,113,349,114,348,116,347,120,346,125,346,130,345,135,345,139,
			345,143,345,147,345,149,345,151,345,151,-1,0,331,1,331,2,331,4,330,8,
			328,11,327,14,325,18,325,19,325,21,324,21,325,21,326,19,327,18,330,14,
			332,12,333,10,335,8,335,7,336,7,337,6,337,7,338,8,339,10,340,12,
			341,17,342,19,342,20,343,23,343,25,343,25,-1,0,400,19,401,19,402,19,
			401,19,402,20,403,20,403,22,403,25,402,28,401,34,400,40,397,56,394,64,
			391,72,389,78,389,83,388,87,387,90,387,92,387,93,387,94,387,95,387,95,
			-1,0,405,18,406,17,407,17,407,16,408,16,409,15,410,15,411,15,413,15,
			415,14,417,15,419,15,422,17,423,19,424,21,425,24,425,27,424,28,423,31,
			422,34,419,38,417,41,412,44,410,46,408,46,407,47,405,48,405,48,-1,0,
			458,26,457,27,456,27,455,27,454,27,453,28,452,29,449,31,446,36,443,42,
			441,47,440,52,439,56,439,58,438,62,438,66,439,67,440,70,441,72,443,75,
			445,76,446,77,449,77,449,77,-1,0,485,33,486,32,487,32,488,32,487,32,
			488,32,488,33,487,34,486,37,485,42,484,44,483,48,482,54,481,58,479,63,
			477,70,476,75,474,79,474,82,474,83,473,86,473,88,473,89,473,90,473,91,
			474,90,473,88,473,88,-1,0,479,48,480,47,481,45,482,43,484,42,488,38,
			494,34,498,33,501,32,504,32,506,32,506,33,507,33,508,34,508,35,507,36,
			507,38,504,41,502,44,499,46,497,48,495,51,494,51,494,52,494,53,494,54,
			495,54,497,56,499,58,501,60,502,61,504,64,505,66,505,68,505,70,505,72,
			504,74,503,76,500,79,497,81,493,83,492,84,490,84,490,85,489,85,489,85,
			-1,0,546,27,547,27,548,27,547,27,548,27,548,29,547,32,546,36,544,41,
			542,49,539,59,537,65,535,71,533,76,532,80,531,83,530,86,530,87,531,87,
			531,87,-1,0,582,33,583,33,584,33,584,35,581,38,578,43,573,51,568,59,
			565,64,562,69,560,72,557,74,558,76,558,77,558,76,558,76,-1,0,578,37,
			579,36,580,35,581,35,581,34,582,34,583,34,584,35,585,37,586,40,587,43,
			588,50,589,55,591,59,592,64,593,67,595,71,596,73,598,75,599,77,600,78,
			601,78,599,78,599,78,-1,0,566,63,567,63,568,62,570,62,573,61,576,60,
			582,60,583,59,587,59,591,59,593,58,593,58,-1,0,619,57,619,58,620,58,
			621,59,622,61,622,62,622,64,621,68,620,70,619,73,618,76,617,79,617,81,
			617,83,618,84,618,86,620,87,621,87,623,88,626,88,626,87,628,86,628,86,
			-1,0,630,47,631,47,631,48,631,49,631,50,631,52,631,52,-1,0,661,31,
			662,31,663,31,663,32,663,33,664,34,665,37,665,40,665,45,665,49,664,52,
			663,59,661,70,659,74,656,78,653,81,651,84,648,87,644,88,644,88,-1,0,
			704,21,705,21,705,22,705,25,704,29,703,34,702,40,699,56,697,64,695,72,
			693,79,691,85,689,90,688,93,688,96,689,98,689,97,689,98,689,97,689,97,
			-1,0,716,15,718,14,719,14,720,14,721,13,721,14,723,14,725,15,727,16,
			728,18,730,23,730,27,729,29,728,34,726,38,723,44,719,49,716,52,713,54,
			711,55,709,56,708,56,708,56,-1,0,778,17,778,18,777,18,776,18,775,19,
			772,20,768,23,764,26,761,30,758,35,756,40,754,46,753,52,753,57,754,61,
			754,65,755,68,756,67,759,71,761,72,766,73,766,73,-1,0,821,22,820,23,
			819,24,816,27,811,33,804,41,799,48,795,54,792,59,789,64,787,68,785,71,
			784,73,785,74,786,74,786,73,786,73,-1,0,820,27,821,27,822,27,823,26,
			823,27,824,27,825,27,826,29,826,31,827,35,827,37,828,41,829,45,829,50,
			830,54,831,58,832,61,832,64,833,67,834,69,834,70,834,70,-1,0,800,60,
			801,60,802,59,804,59,806,58,812,57,817,55,822,54,827,53,831,52,836,51,
			841,49,841,49,-1,0,857,51,858,52,859,53,859,54,860,55,860,56,859,59,
			858,61,855,65,855,67,853,71,852,74,853,77,853,78,854,79,855,80,858,80,
			861,80,862,80,863,79,863,79,-1,0,873,37,873,38,874,39,874,40,874,43,
			874,44,874,44,-1,0,925,16,926,16,927,16,927,17,928,18,929,22,929,24,
			929,28,929,33,927,46,926,53,923,60,921,66,917,72,913,77,909,81,904,84,
			900,87,893,90,893,90,-1,0,-1,-1,
		};
}
