package com.example.goncl.editarphoto;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class FullScreenViewActivity extends Activity{

	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		viewPager = (ViewPager) findViewById(R.id.pager);

		utils = new Utils(getApplicationContext());

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);

		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
				utils.getFilePaths(), position);


		viewPager.setAdapter(adapter);

		// displaying selected image first

		viewPager.setCurrentItem(position);
	}

	public void onBackPressed(){
		Intent i = new Intent(this, GridViewActivity.class);
		startActivity(i);
		this.finish();
	}
}
