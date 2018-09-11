package com.tarena.tabs.ui;


import com.tarena.tabs.ui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HuadongActivity extends Activity {
	ImageButton imgbutton=null;;
	private Workspace workspace;
	private LinearLayout ll;//线性布局

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.guide);
        
        ExitApplication.getInstance().addActivity(this);//点击key-down键自动退出程序
        
        ll = (LinearLayout) findViewById(R.id.llayout);
    	imgbutton = (ImageButton) findViewById(R.id.img_2);//������ת
    	imgbutton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent=new Intent();
				intent.setClass(HuadongActivity.this,MainActivity.class);
				startActivity(intent);
				
			}	
		});	
        workspace = (Workspace) findViewById(R.id.workspace);
		workspace.setAlloweffect(false);
		workspace
		.setOnScreenChangeListener(new Workspace.OnScreenChangeListener() {
			@Override
			public void onScreenChangeStart(int whichScreen) {
			}

			@Override
			public void onScrrenChangeEnd(int whichScreen) {
				setEnabled(whichScreen);
			}
		});

    }
    
	private void setEnabled(int which) {
		int count = ll.getChildCount();
		for (int i = 0; i < count; i++) {
			((ImageView) ll.getChildAt(i))
					.setImageResource(R.drawable.guide_dot_white);
		}
		ImageView v = (ImageView) ll.getChildAt(which);
		if (null != v) {
			v.setImageResource(R.drawable.guide_dot_blue);
		}
	}
    
}

