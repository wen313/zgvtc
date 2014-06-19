package com.zgvtc;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class MainActivity extends FragmentActivity implements OnClickListener{

	private MyNewsFragment myNewsPage;//新闻中心
	private WalkCampusFragment walkCampusPage;//走进校园
	private DemoSchoolFragment demoSchoolPage;//示范校
	private ShorttermTrainFragment shorttermTrainPage;//成教培训
	private AdmissionEmpFragment admissionEmpPage;//招生就业
	
	private FrameLayout mynews,walkCampus,demoSchool,shorttermTrain,admissionEmp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);		
		setupView();
		clickMyNews();
	}
	
	private void setupView() {
		mynews = (FrameLayout) findViewById(R.id.fl_mynews);
		walkCampus = (FrameLayout) findViewById(R.id.fl_walkintocampus);
		demoSchool = (FrameLayout) findViewById(R.id.fl_demonstrationschool);
		shorttermTrain = (FrameLayout) findViewById(R.id.fl_shorttermtraining);
		admissionEmp = (FrameLayout) findViewById(R.id.fl_admissionemployment);

		mynews.setOnClickListener(this);
		walkCampus.setOnClickListener(this);
		//manageStorage.setOnClickListener(this);
		demoSchool.setOnClickListener(this);
		shorttermTrain.setOnClickListener(this);
		admissionEmp.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fl_mynews:
			clickMyNews();
			break;
		case R.id.fl_walkintocampus:
			clickWalkcampus();
			break;

		case R.id.fl_demonstrationschool:
			clickDemonSchool();
			break;
		case R.id.fl_shorttermtraining:
			clickShorttermTrain();
			break;
		case R.id.fl_admissionemployment:
			clickAdmissionEmp();
			break;

		default:
			break;
		}
	}
	
	private void clickMyNews() {
		myNewsPage = new MyNewsFragment();
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.frame_content, myNewsPage);
		fragmentTransaction.commit();
		mynews.setSelected(true);
		walkCampus.setSelected(false);
		demoSchool.setSelected(false);
		shorttermTrain.setSelected(false);
		//manageStorage.setSelected(false);
		admissionEmp.setSelected(false);

	}
	
	private void clickWalkcampus() {
		walkCampusPage = new WalkCampusFragment();
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.frame_content, walkCampusPage);
		fragmentTransaction.commit();
		mynews.setSelected(false);
		walkCampus.setSelected(true);
		demoSchool.setSelected(false);
		shorttermTrain.setSelected(false);
		admissionEmp.setSelected(false);

	}
	
	private void clickDemonSchool() {
		demoSchoolPage = new DemoSchoolFragment();
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.frame_content, demoSchoolPage);
		fragmentTransaction.commit();
		mynews.setSelected(false);
		walkCampus.setSelected(false);
		demoSchool.setSelected(true);
		shorttermTrain.setSelected(false);
		admissionEmp.setSelected(false);

	}
	
	private void clickShorttermTrain() {
		shorttermTrainPage = new ShorttermTrainFragment();
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.frame_content, shorttermTrainPage);
		fragmentTransaction.commit();
		mynews.setSelected(false);
		walkCampus.setSelected(false);
		demoSchool.setSelected(false);
		shorttermTrain.setSelected(true);
		admissionEmp.setSelected(false);

	}
	
	private void clickAdmissionEmp() {
		admissionEmpPage = new AdmissionEmpFragment();
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.frame_content, admissionEmpPage);
		fragmentTransaction.commit();
		mynews.setSelected(false);
		walkCampus.setSelected(false);
		demoSchool.setSelected(false);
		shorttermTrain.setSelected(false);
		//manageStorage.setSelected(false);
		admissionEmp.setSelected(true);

	}

}
