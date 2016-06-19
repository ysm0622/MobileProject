package com.example.ysm0622.app_when.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.ysm0622.app_when.R;
import com.example.ysm0622.app_when.global.Gl;
import com.example.ysm0622.app_when.group.GroupList;
import com.example.ysm0622.app_when.login.Login;
import com.example.ysm0622.app_when.server.ServerConnection;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Intro extends AppCompatActivity {


    // TAG
    private static final String TAG = Intro.class.getName();

    // Intent
    private Intent mIntent;

    //Shared Preferences
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mIntent = new Intent(Intro.this, GroupList.class);
        setContentView(R.layout.intro_main);
        new ServerConnection().execute(Gl.SELECT_ALL_USER);

        mSharedPref = getSharedPreferences(Gl.FILE_NAME_NOTICE, MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        Date d = c.getTime();

        long test1 = c.getTimeInMillis();
        long test2 = d.getTime();
        Date t = new Date(test2);
        long test3 = t.getTime();
        c.setTimeInMillis(test3);

        Log.d("testtest", "캘린더 값 : " + test1 + " / c : ");
        Log.d("testtest", "캘린더받은 데이트 값 : " + test2 + " / d : " + d.toString());
        Log.d("testtest", "데이트 값 : " + test3 + " / t : " + t.toString());
        Log.d("testtest", "캘린더 값 : " + c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DATE));

        Gl.initialize(this);
        if (mSharedPref == null || !mSharedPref.contains(Gl.NOTICE_CHECK))
            noticeInit();

        mSharedPref = getSharedPreferences(Gl.FILE_NAME_LANGUAGE, MODE_PRIVATE);

        if (mSharedPref == null || !mSharedPref.contains(Gl.LANGUAGE_CHECK)) {//처음 한글 언어 선택
            languageInit();
        } else {//설정된 언어로 표시
            if (!mSharedPref.getString(Gl.LANGUAGE_CHECK, Gl.LANGUAGE_KOREAN).equalsIgnoreCase(Gl.LANGUAGE_KOREAN)) {
                setLocale("en");
            }
        }

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
//                new JSONParse().execute();
                if (PRF_AUTO_LOGIN()) {
                    startActivityForResult(mIntent, Gl.INTRO_GROUPLIST);
////                    new JSONParse().execute();
                } else {
                    startActivity(new Intent(Intro.this, Login.class));
                    finish();
                }
//                else {
//                    new JSONParse().execute()
//                }
            }
        }.start();
//
//        Gl.USERS.add(new User("지정한", "wlwjdgks123@gmail.com", "1234"));
//        Gl.USERS.add(new User("조동현", "ehdguso@naver.com", "1234"));
//        Gl.USERS.add(new User("조동현", "aa.com", "1234"));
//
//        Gl.USERS.add(new User("조서형", "westbro00@naver.com", "1234"));
//        Gl.USERS.add(new User("김영송", "infall346@naver.com", "1234"));
//        Gl.USERS.add(new User("장영준", "cyj9212@gmail.com", "1234"));
//        Gl.USERS.add(new User("유영준", "yyj@gmail.com", "1234"));
//        Gl.USERS.add(new User("김원", "wonkimtx@gachon.ac.kr", "1234"));
//        Gl.USERS.add(new User("정옥란", "orjeong@gachon.ac.kr", "1234"));
//        Gl.USERS.add(new User("최재혁", "jchoi@gachon.ac.kr", "1234"));
//        Gl.USERS.add(new User("유준", "joon.yoo@gachon.ac.kr", "1234"));
//        Gl.USERS.add(new User("노웅기", "wkloh2@gachon.ac.kr", "1234"));
//        Gl.USERS.add(new User("최아영", "aychoi@gachon.ac.kr", "1234"));
//        Gl.USERS.add(new User("정용주", "coolyj.jung@gmail.com", "1234"));
    }

    public void onBackPressed() {
    }

    public void languageInit() {
        mSharedPref = getSharedPreferences(Gl.FILE_NAME_LANGUAGE, MODE_PRIVATE);
        mEdit = mSharedPref.edit();

        mEdit.putString(Gl.LANGUAGE_CHECK, Gl.LANGUAGE_KOREAN);
        mEdit.apply();
    }

    public void noticeInit() {
        mSharedPref = getSharedPreferences(Gl.FILE_NAME_NOTICE, MODE_PRIVATE);
        mEdit = mSharedPref.edit();

        mEdit.putBoolean(Gl.NOTICE_CHECK, false);
        mEdit.putBoolean(Gl.NOTICE_SOUND, false);
        mEdit.putBoolean(Gl.NOTICE_VIBRATION, false);
        mEdit.putBoolean(Gl.NOTICE_POPUP, false);
        mEdit.apply();
    }

    public boolean PRF_AUTO_LOGIN() {
        String email, password;

        mSharedPref = getSharedPreferences(Gl.FILE_NAME_LOGIN, MODE_PRIVATE);
        //로그인 상태
        if (mSharedPref != null && mSharedPref.contains(Gl.USER_EMAIL)) {
            email = mSharedPref.getString(Gl.USER_EMAIL, "DEFAULT");
            if(isExistEmail(email)==-1) return false;
            password = mSharedPref.getString(Gl.USER_PASSWORD, "DEFAULT");
            if (isRightPassword(password, isExistEmail(email))) {
                mIntent.putExtra(Gl.USER, Gl.getUser(isExistEmail(email)));
                return true;
            }
            return false;
        }
        return false;
    }

    private int isExistEmail(String s) {
        for (int i = 0; i < Gl.getUserCount(); i++) {
            if (Gl.getUser(i).getEmail().equals(s)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isRightPassword(String s, int i) {
        if (Gl.getUser(i).getPassword().equals(s)) {
            return true;
        } else {
            return false;
        }
    }

    // 언어 설정 메소드
    public void setLocale(String charicter) {
        Locale locale = new Locale(charicter);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Gl.INTRO_GROUPLIST) {
            if (resultCode == Gl.RESULT_DELETE) {
                startActivity(new Intent(Intro.this, Login.class));
                Toast.makeText(getApplicationContext(), R.string.delete_acc_msg, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Gl.RESULT_LOGOUT) {
                startActivity(new Intent(Intro.this, Login.class));
                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
                System.exit(0);
                super.onBackPressed();
            }
        }
    }
}