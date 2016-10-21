package com.jack.fibonacci;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mFibonacciEditText;
    private TextView mFibonacciResultTextView;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        if (savedInstanceState != null)
            updateFields(savedInstanceState);

    }

    private void updateFields(Bundle bundle) {
        mFibonacciEditText.setText(bundle.getString(getString(R.string.fibonacci_edit_text)));
        mFibonacciResultTextView.setText(bundle.getString(getString(R.string.result_text_field)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.fibonacci_edit_text), mFibonacciEditText.getText().toString());
        outState.putString(getString(R.string.result_text_field), mFibonacciResultTextView.getText().toString());
    }

    private void initFields() {
        mFibonacciEditText = (EditText) findViewById(R.id.fibonacci_edit_text);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mFibonacciResultTextView = (TextView) findViewById(R.id.result_fibonacci_text_view);
        mSubmitButton.setOnClickListener(submitButtonListener);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        ServiceResultReceiver receiver = new ServiceResultReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.intent_filter_action));
        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mFibonacciEditText.getText().length() == 0) {
                showErrorMessage();
                return;
            }
            int inputValue = Integer.parseInt(mFibonacciEditText.getText().toString());
            if (inputValue > 0 && inputValue <= 1000) {
                Intent intent = new Intent(getApplicationContext(), CalculateService.class);
                intent.putExtra(getString(R.string.user_number), Integer.parseInt(mFibonacciEditText.getText().toString()));
                startService(intent);
            } else
                showErrorMessage();

        }
    };

    private void showErrorMessage() {
        mFibonacciEditText.setError(getString(R.string.error_message));
    }

    public class ServiceResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mFibonacciResultTextView.setText("Result : " + intent.getStringExtra(getString(R.string.result)));
        }
    }
}
