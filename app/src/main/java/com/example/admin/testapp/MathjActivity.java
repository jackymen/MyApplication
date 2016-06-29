package com.example.admin.testapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.kexanie.library.MathView;

public class MathjActivity extends AppCompatActivity {
    MathView formula_two;
    @InjectView(R.id.formula_one)
    MathView formulaOne;
    @InjectView(R.id.formula_two)
    MathView formulaTwo;
    private String html = "<HTML><HEAD><style>div{text-align:center;border:1px solid #000000;padding:0px; width:25px;border-radius:50px;}</style><TITLE>HTML文件的上标字与下标字</TITLE></HEAD><BODY>"
            + "<div >\\(99_1^2\\)</div>"
            + "<H1>我的\\(ax^2 + bx + c = 0\\)个人首页</H1>①②③④⑤⑥⑦⑨⑧⑩⑫⑬⑭⑮⑯⑰⑱⑲⑳&#12881;  &#12882;  &#12883;  &#12884;  &#12885;  &#12886;  &#12887;  &#12888;  &#12889;  &#12890; ☆⑳ ①②③④⑤⑥⑦⑧⑨⑩ &#39; &#8448; &#8451; content: &#9350; 在word里面 先输入246a，然后按快捷键alt+X，就可以出现&#9322;\n246A alt+x =&#9322; \n246B alt+x =&#9323; \n246C alt+x =&#9324;\n246D alt+x =&#9325;\n246E alt+x ..数学公式：(X+Y)<SUP>2</SUP> = X<SUP>2</SUP> + 2XY<SUP>b</SUP> + Y<SUP>2</SUP><BR>化学方程式：H<SUB>2</SUB>O = 2H + O &#9733 	&frac34</BODY></HTML>";

    String tex = "\\( x_1^2 + x_2^2 \\)+ \\cdots + x_n^2 This come from string.x<sup>2</sup> You can insert inline formula:" +
            " \\(ax_3^2 + bx + c =0 \\) " +
            "disaplay img<img onClick=\"window.jsdemo.clickOnAndroid()\" align=\"middle\" src=\"http://img11.360buyimg.com/cms/jfs/t2278/175/2502766179/5579/16811fda/56e16a64N72dff231.jpg\" /> or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$费擦发送答复就是";


    Handler mHander = new Handler();

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mathjax);
        ButterKnife.inject(this);
        formula_two = (MathView) findViewById(R.id.formula_two);
        formula_two.setText(tex);
        formula_two.getSettings().setJavaScriptEnabled(true);
        formula_two.addJavascriptInterface(new JsAndroidInterface(), "jsdemo");
    }

    final class JsAndroidInterface {
        public JsAndroidInterface() {
        }

        public void clickOnAndroid() {
            Toast.makeText(getApplicationContext(), "fafds", Toast.LENGTH_SHORT).show();
            mHander.post(new Runnable() {
                public void run() {
                    //  mWebView.loadUrl("javascript:wave()");
                    Toast.makeText(getApplicationContext(), "fafds", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
