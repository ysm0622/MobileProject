package com.teamw.ysm0622.app_when.group;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamw.ysm0622.app_when.R;
import com.teamw.ysm0622.app_when.global.Gl;

public class CreateGroup extends AppCompatActivity implements View.OnFocusChangeListener, TextWatcher, View.OnClickListener {

    // TAG
    private static final String TAG = CreateGroup.class.getName();

    // Const
    private static final int mInputNum = 2;
    private static final int mToolBtnNum = 2;

    // Intent
    private Intent mIntent;

    // Toolbar
    private ImageView mToolbarAction[];
    private TextView mToolbarTitle;

    private TextInputLayout mTextInputLayout[] = new TextInputLayout[mInputNum];
    private ImageView mImageView[] = new ImageView[mInputNum];
    private EditText mEditText[] = new EditText[mInputNum];
    private TextView mTextViewErrMsg[] = new TextView[mInputNum];
    private TextView mTextViewCounter[] = new TextView[mInputNum];
    private int mMinLength[] = new int[mInputNum];
    private int mMaxLength[] = new int[mInputNum];
    private String mErrMsg[] = new String[mInputNum];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroup_main);

        // Receive intent
        mIntent = getIntent();

        Drawable[] toolbarIcon = new Drawable[2];
        toolbarIcon[0] = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        toolbarIcon[1] = getResources().getDrawable(R.drawable.ic_arrow_forward_white_24dp);
        String toolbarTitle = getResources().getString(R.string.title_activity_create_group);

        initToolbar(toolbarIcon, toolbarTitle);

        initialize();
    }

    private void initialize() {

        // Array allocation

        // Create instance

        // View allocation
        mTextInputLayout[0] = (TextInputLayout) findViewById(R.id.TextInputLayout0);
        mTextInputLayout[1] = (TextInputLayout) findViewById(R.id.TextInputLayout1);

        mImageView[0] = (ImageView) findViewById(R.id.ImageView0);
        mImageView[1] = (ImageView) findViewById(R.id.ImageView1);

        mEditText[0] = (EditText) findViewById(R.id.EditText0);
        mEditText[1] = (EditText) findViewById(R.id.EditText1);

        mTextViewErrMsg[0] = (TextView) findViewById(R.id.TextView0);
        mTextViewErrMsg[1] = (TextView) findViewById(R.id.TextView2);

        mTextViewCounter[0] = (TextView) findViewById(R.id.TextView1);
        mTextViewCounter[1] = (TextView) findViewById(R.id.TextView3);

        // Add listener
        for (int i = 0; i < mInputNum; i++) {
            mEditText[i].setOnFocusChangeListener(this);
            mEditText[i].addTextChangedListener(this);
        }

        // Default setting
        mToolbarAction[1].setVisibility(View.INVISIBLE);

        for (int i = 0; i < mInputNum; i++) {
            mImageView[i].setColorFilter(getResources().getColor(R.color.grey7), PorterDuff.Mode.SRC_ATOP);
            mTextViewErrMsg[i].setVisibility(View.INVISIBLE);
            mTextViewCounter[i].setVisibility(View.INVISIBLE);
            mTextViewCounter[i].setTextColor(getResources().getColor(R.color.grey6));
            mTextViewErrMsg[i].setText("");
        }

        mMinLength[0] = 1;
        mMinLength[1] = 0;

        mMaxLength[0] = 20;
        mMaxLength[1] = 128;

        mErrMsg[0] = getResources().getString(R.string.group_title_errmsg1);
        mErrMsg[1] = getResources().getString(R.string.desc_errmsg1);
    }

    private void initToolbar(Drawable Icon[], String Title) {
        mToolbarAction = new ImageView[2];
        mToolbarAction[0] = (ImageView) findViewById(R.id.Toolbar_Action0);
        mToolbarAction[1] = (ImageView) findViewById(R.id.Toolbar_Action1);
        mToolbarTitle = (TextView) findViewById(R.id.Toolbar_Title);

        for (int i = 0; i < mToolBtnNum; i++) {
            mToolbarAction[i].setOnClickListener(this);
            mToolbarAction[i].setImageDrawable(Icon[i]);
            mToolbarAction[i].setBackground(getResources().getDrawable(R.drawable.selector_btn));
        }
        mToolbarTitle.setText(Title);
    }

    @Override
    public void onClick(View v) {
        if (mToolbarAction[0].getId() == v.getId()) {
            super.onBackPressed();
        }
        //그룹으로 초대할 유저 검색
        if (mToolbarAction[1].getId() == v.getId()) {
            mIntent.setClass(CreateGroup.this, InvitePeople.class);
            mIntent.putExtra(Gl.GROUP_TITLE, mEditText[0].getText().toString());
            mIntent.putExtra(Gl.GROUP_DESC, mEditText[1].getText().toString());
            startActivityForResult(mIntent, Gl.CREATEGROUP_INVITEPEOPLE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        for (int i = 0; i < mInputNum; i++) {
            if (v.getId() == mEditText[i].getId()) {
                mImageView[i].clearColorFilter();
                if (hasFocus) {
                    mImageView[i].setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                    mTextViewCounter[i].setVisibility(View.VISIBLE);
                    mTextViewCounter[i].setText(mEditText[i].getText().toString().length() + " / " + mMinLength[i] + "–" + mMaxLength[i]);
                } else {
                    mImageView[i].setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    mTextViewCounter[i].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int cnt = 0;
        for (int i = 0; i < mInputNum; i++) {
            if (mEditText[i].getText().toString().length() >= mMinLength[i]) {
                cnt++;
            }
        }
        if (cnt == mInputNum) {
            mToolbarAction[1].setVisibility(View.VISIBLE);
        } else {
            mToolbarAction[1].setVisibility(View.INVISIBLE);
        }
        validationCheck();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //Input 데이터 확인
    private void validationCheck() {
        for (int i = 0; i < mInputNum; i++) {
            if (mEditText[i].hasFocus()) {
                mTextViewCounter[i].setText(mEditText[i].getText().toString().length() + " / " + mMinLength[i] + "–" + mMaxLength[i]);
            }
            if (mEditText[i].getText().toString().length() < mMinLength[i] && mEditText[i].hasFocus()) {
                mTextViewErrMsg[i].setText(mErrMsg[i]);
                mTextViewErrMsg[i].setVisibility(View.VISIBLE);
                mTextViewCounter[i].setTextColor(getResources().getColor(R.color.red_dark));
                mEditText[i].getBackground().setColorFilter(getResources().getColor(R.color.red_dark), PorterDuff.Mode.SRC_ATOP);
            } else if (mEditText[i].getText().toString().length() >= mMinLength[i] && mEditText[i].hasFocus()) {
                mTextViewErrMsg[i].setText("");
                mTextViewErrMsg[i].setVisibility(View.INVISIBLE);
                mTextViewCounter[i].setTextColor(getResources().getColor(R.color.colorAccent));
                mEditText[i].getBackground().setColorFilter(getResources().getColor(R.color.textPrimary), PorterDuff.Mode.SRC_ATOP);
                mEditText[i].getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Gl.CREATEGROUP_INVITEPEOPLE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
