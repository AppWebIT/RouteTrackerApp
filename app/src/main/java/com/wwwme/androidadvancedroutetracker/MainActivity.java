package com.wwwme.androidadvancedroutetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private Button btnLogin;

    // Initialisierung Variablen für Login
    private EditText usernameEdit, passwordEdit;
    private TextView response, status;
    private String success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        usernameEdit = (EditText)findViewById(R.id.editEmail);
        passwordEdit = (EditText)findViewById(R.id.editPassword);

        // status = (TextView)findViewById(R.id.textView6);
        response = (TextView)findViewById(R.id.response);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);

        // 09.12.2015 Zlamala: Login Success bzw. Fehlermeldungen
        response.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "----- Response: " + s + " -----");

                if (s.length() > 0) {

                    success = s.toString();

                    if (success.equals("Success")) {
                        Log.d(TAG, "----- Startscreen aufrufen -----");
                        startActivity(new Intent("com.wwwme.androidadvancedroutetracker.Startscreen"));
                        response.setText("");
                        success = "";
                        usernameEdit.getText().clear();
                        passwordEdit.getText().clear();
                    } else {
                        response.setText("");
                        Context context = getApplicationContext();
                        CharSequence text = success;
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 150);
                        toast.show();
                        success = "";
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

        });

    }

    // 20.11.2015 Zlamala: Fokus auf Main-Screen, wenn Touch außerhalb der Text-Inputs
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public void btnLoginClick(){
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        new SigninActivity(this,status,response).execute(username, password);
    }

    public void onClick (View v){
        switch (v.getId()){
            case R.id.btnLogin:
                btnLoginClick();
                break;
        }
    }

}
