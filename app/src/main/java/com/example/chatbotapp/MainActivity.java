package com.example.chatbotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.telephony.SmsManager.getDefault;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    TextView state;
    TextView detail;
    BroadcastReceiver myReceiver;
    IntentFilter intentFilter1;
    SmsMessage[] smsMessages;
    Handler handler = new Handler();
    SmsManager smsManager = SmsManager.getDefault();
    static final int num = 1234;
    Runnable runnable;
    String answer;
    String str;
    int currentState = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state = findViewById(R.id.id_text_view);
        detail = findViewById(R.id.id_text_two);



        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, num);
        else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, num);


        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    Bundle bundle = intent.getExtras();
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (bundle != null) {
                        smsMessages = new SmsMessage[pdus.length];
                        for (int i = 0; i < pdus.length; i++) {
                            smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            str = smsMessages[i].getMessageBody();
                            Log.d("STR", str);
                            Log.d("TAG", "onReceive: " + "Num: " + smsMessages[i].getOriginatingAddress() + ", Msg:" + smsMessages[i].getMessageBody());
                            Toast toast = Toast.makeText(MainActivity.this, "Num: " + smsMessages[i].getOriginatingAddress() + ", Msg:" + smsMessages[i].getMessageBody(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                        handler.postDelayed(sendMessage(str), 1000);
                    }
            }
        };


    } //onCreate

    public Runnable sendMessage(String message){
        final String msg = message;
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("TEST", "Msg: " + msg);
                if(currentState==0 && (msg.toLowerCase().contains("hello") || msg.toLowerCase().contains("hi") || msg.toLowerCase().contains("hey"))){
                    state.setText("Greeting/Intro State");
                    answer = "Hey, have you been hearing about this Coronavirus?";
                    currentState++;
                }
                else if(currentState == 1) {
                    if (msg.toLowerCase().contains("yes") || msg.toLowerCase().contains("yeah")) {
                        detail.setText(" ");
                        answer = "Nice, it is really important you keep up with the news! Many countries have been facing terrible problems.";
                        currentState++;
                    } else if (msg.toLowerCase().contains("no") || msg.toLowerCase().contains("not") || msg.toLowerCase().contains("haven't")) {
                        detail.setText(" ");
                        answer = "Well, it is a dangerous virus going around making people sick, and there has been a lot going on in the news.";
                        currentState++;
                    } else if (msg.contains("little")) {
                        detail.setText(" ");
                        answer = "It's the dangerous virus going around. One effect is that there are many schools and buildings that are shut down, and probably for a long time!";
                        currentState++;
                    } else {
                        detail.setText("Confusion detected. Directing user to new response.");
                        answer = "I don't understand. Anyways...you should make sure you know what is going on in the news to stay informed, which is very important during times like these!";
                        currentState++;
                    }
                }

                else if(currentState == 2){
                    state.setText("Discussion State");
                    if ((msg.toLowerCase().contains("what") && msg.toLowerCase().contains("countries")) || (msg.toLowerCase().contains("world"))) {
                        detail.setText(" ");
                        answer = "As of now, the US has the most cases. We have more than China! It's quite insane, how fast this spreads. The symptoms of this virus are not good at all.";
                        currentState++;
                    } else if ((msg.toLowerCase().contains("what") && msg.toLowerCase().contains("going")) || (msg.toLowerCase().contains("what's"))) {
                        detail.setText(" ");
                        answer = "They showed ways to stay safe, such as washing hands with soap for 20 seconds, self isolation, and avoiding close contact with the sick!";
                        currentState++;
                    } else if (msg.toLowerCase().contains("why") && msg.toLowerCase().contains("happening")) {
                        detail.setText(" ");
                        answer = "To prevent large gatherings. But to let you know, this virus is more known for affecting elderly groups of people. You should still stay isolated, though.";
                        currentState++;
                    } else {
                        detail.setText("Confusion detected. Directing user to new response.");
                        answer = "Alright?? But make sure that you are doing activities such as washing hands regularly and avoiding close contact with the sick!";
                        currentState++;
                    }
                }

                else if(currentState == 3){
                    state.setText("Effects State");
                    if ((msg.toLowerCase().contains("what") && msg.toLowerCase().contains("symptoms")) || (msg.toLowerCase().contains("affect"))) {
                        detail.setText(" ");
                        answer = "This is very similar to flu-like symptoms. People can get cough, fever, difficulty breathing, and tiredness.";
                        currentState++;
                    } else if ((msg.toLowerCase().contains("yeah") && msg.toLowerCase().contains("doing")) || (msg.toLowerCase().contains("have"))) {
                        detail.setText(" ");
                        answer = "Nice job. Also, here's a tip: you should not stock up on unnecessary items. Many people have been doing this for no reason.";
                        currentState++;
                    } else if (msg.toLowerCase().contains("wow") && msg.toLowerCase().contains("yep") || msg.toLowerCase().contains("agree")) {
                        detail.setText(" ");
                        answer = "This is because they are more likely to get it due to their chronic health conditions, and the Coronavirus makes it worse.";
                        currentState++;
                    } else {
                        detail.setText("Confusion detected. Directing user to new response.");
                        answer = "I'm not sure I understand...Well, did you know that the symptoms of this virus are very similar to the flu symptoms, such as cough and cold??";
                        currentState++;
                    }
                }

                else if(currentState == 4){
                    state.setText("Final/Goodbyes State");
                    if ((msg.toLowerCase().contains("yeah") && msg.toLowerCase().contains("similar")) || (msg.toLowerCase().contains("alright"))) {
                        detail.setText(" ");
                        answer = "Well, that's all I have for today! Nice job for keeping up with the news, and I hope you stay healthy.";
                        currentState++;
                    } else if ((msg.toLowerCase().contains("ok") && msg.toLowerCase().contains("sense")) || (msg.toLowerCase().contains("yeah") && msg.toLowerCase().contains("sense"))) {
                        detail.setText(" ");
                        answer = "Alright! My time has come to leave, but stay tuned, as I may come back for more tips!";
                        currentState++;
                    } else if (msg.toLowerCase().contains("that") && msg.toLowerCase().contains("bad")) {
                        detail.setText(" ");
                        answer = "That's what happens in life. Well, I must get going, as I need to help more people out!";
                        currentState++;
                    } else {
                        detail.setText("Confusion detected. Directing user to new response.");
                        answer = "Didn't quite get that...Well, I hope you have learned some information, and have a great rest of your day!.";
                        currentState++;
                    }
                }
                else {
                    if(currentState < 5) {
                        state.setText("Greeting/Intro State");
                        detail.setText("Confusion detected. Directing user to new response.");
                        answer = "I'm not sure I understand...but anyways, have you heard about this Coronavirus?!";
                        currentState = 1;
                    }
                }
                if((msg.toLowerCase().contains("goodbye") || msg.toLowerCase().contains("bye") || msg.toLowerCase().contains("have") || msg.toLowerCase().contains("nice") || msg.toLowerCase().contains("thank")) && currentState > 4){
                    state.setText("Finished!");
                    detail.setText("END!");
                    answer = "Bye!";
                }

                Log.d("currentState", currentState + "");
                Log.d("answer", answer + " ");
                smsManager.sendTextMessage("5556", null, answer, null, null);
            }
        };
        return runnable;
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter1 = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(myReceiver, intentFilter1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }
} //MainActivity
