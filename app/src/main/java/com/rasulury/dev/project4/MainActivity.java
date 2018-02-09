package com.rasulury.dev.project4;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends Activity {
    static final int UPDATE_SECRET_CODE = 1;
    static final int SET_RESULT = 2;
    static final int SET_RESULT2 = 8;
    static final int RECEIVE_GUESS = 6;
    static final int RECEIVE_EVAL = 7;
    static int stop,counter1,counter2;
    static List<Integer> workQ = new ArrayList<>();
    static List<Integer> workQ2 = new ArrayList<>();
    static int[] a = new int[4];
    static int[] a2 = new int[4];
    TextView tv1guess;
    TextView tv2guess, tvOneLog, tvTwoLog;
    int response1[] = new int[4];
    int response2[] = new int[4];
    int gc1[] = new int[4];
    int gc2[] = new int[4];
    Bundle uitot2, Uitot1;
    Handler Player1Handler, Player2Handler;
    TextView tvOneStatus, tvTwoStatus;
    String s1 = "";
    String s2 = "";

    //Getting reference for UI handler
    private Handler uiHandler = new Handler() {
        public int flag1, flag2;

        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                //opening corresponding message types
                case UPDATE_SECRET_CODE:
                    //Setting secret code on app
                    if (msg.arg1 == 1)
                        tv1guess.setText((String) msg.obj);
                    else if (msg.arg1 == 2)
                        tv2guess.setText((String) (msg.obj));

                    break;
                case SET_RESULT:
                    //Bundle for getting guessed code and response code
                    Bundle b = msg.getData();
                    String s = "Player 2 guessed ", GCUI = "";

                    s += b.getString("key");
                    GCUI += b.getString("key");
                    s += " ,";

                    int[] res = (int[]) msg.obj;
                    flag1 = 1;
                    for (int i = 0; i < 4; i++) {
                        if (res[i] != 1)
                            //using flag to check if the current guess is equal to secret code
                            //this can be determined when res[] array contains all its elements as 1
                            flag1 = 0;
                        s += res[i];
                        response1[i] = res[i];
                        //converting string to int array
                        gc1[i] = Integer.parseInt(GCUI.substring(i, i + 1));
                    }
                        //when flag1 is 0, current guess is not equal to secret code
                        //sending message to player 2 handler
                    if (flag1 == 0 && msg.arg2 == 1) {
                        Message T2msg = Player2Handler.obtainMessage(RECEIVE_EVAL);
                        //sending guess code, response code
                        uitot2.putIntArray("res", response1);
                        uitot2.putIntArray("gc", gc1);
                        T2msg.setData(uitot2);
                        Player2Handler.sendMessage(T2msg);
                    } else if (flag1 == 1 && msg.arg2 == 1) {
                        //flag1 is 1, making a toast saying Player 2 won
                        tvOneStatus.setText(s + "success");
                        //updating log
                        tvOneLog.setText(tvOneLog.getText() + "\n" + s + "\nPlayer 2 won, GAME OVER!");
                        Toast.makeText(getApplicationContext(), "Player 2 Won", Toast.LENGTH_LONG).show();
                        //using stop variable to halt the game
                        stop = 0;

                    }
                    if (msg.arg2 == 1 && flag1 == 0) {
                        //updating log
                        tvOneLog.setText(tvOneLog.getText() + "\n" + s);
                        //updating status
                        tvOneStatus.setText(s);
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    break;
                case SET_RESULT2:
                    //Bundle for getting guessed code and response code
                    Bundle b2 = msg.getData();
                    String s2 = "Player 1 guessed ", GCUI2 = "";

                    s2 += b2.getString("key");
                    GCUI2 += b2.getString("key");
                    s2 += " ,";
                    int[] res2 = (int[]) msg.obj;
                    flag2 = 1;
                    for (int i = 0; i < 4; i++) {
                        if (res2[i] != 1)
                            //using flag to check if the current guess is equal to secret code
                            //this can be determined when res[] array contains all its elements as 1
                            flag2 = 0;
                        s2 += res2[i];
                        response2[i] = res2[i];
                        //converting string to int array
                        gc2[i] = Integer.parseInt(GCUI2.substring(i, i + 1));
                    }

                    //when flag1 is 0, current guess is not equal to secret code
                    //sending message to player 2 handler
                    if (flag2 == 0 && msg.arg2 == 2) {


                        Message T1msg = Player1Handler.obtainMessage(RECEIVE_EVAL);
                        //sending guess code, response code
                        Uitot1.putIntArray("res", response2);
                        Uitot1.putIntArray("gc", gc2);
                        T1msg.setData(Uitot1);
                        Player1Handler.sendMessage(T1msg);
                    } else if (flag2 == 1 && msg.arg2 == 2) {
                        //flag2 is 1, making a toast saying Player 2 won
                        tvTwoStatus.setText(s2 + "success");
                        //updating log
                        tvTwoLog.setText(tvTwoLog.getText() + "\n" + s2 + "\n Player 1 won, GAME OVER!");
                        Toast.makeText(getApplicationContext(), "Player 1 Won", Toast.LENGTH_LONG).show();
                        //using stop variable to halt the game
                        stop = 0;
                    }

                    if (msg.arg2 == 2 && flag2 == 0) {
                        //updating log
                        tvTwoLog.setText(tvTwoLog.getText() + "\n" + s2);
                        //updating status
                        tvTwoStatus.setText(s2);

                    }


                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting stop variable
        stop=1;
        counter1=0;
        counter2=0;
        //getting referencing for all views
        tv1guess = findViewById(R.id.tvOneGuess);
        tv2guess = findViewById(R.id.tvTwoGuess);
        tvOneStatus = findViewById(R.id.tvOneStatus);
        tvTwoStatus = findViewById(R.id.tvTwoStatus);
        tvOneLog = findViewById(R.id.tvOneLog);
        tvTwoLog = findViewById(R.id.tvTwoLog);
        uitot2 = new Bundle();
        Uitot1 = new Bundle();
        //initializing the workq's
        for (int i = 0; i < 10; i++) {
            workQ.add(i);
            workQ2.add(i);
        }


        for (int j = 0; j < 4; j++) {
            a[j] = -1;
            a2[j] = -1;
        }

        //starting threads T1 and T2
        Thread t1 = new Thread(new Player1Runnable());
        t1.start();


        Thread t2 = new Thread(new Player2Runnable());
        t2.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.restart:
                //creating intent to restart activity
                Intent i = new Intent(MainActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.exit:
                //Exiting the app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
                break;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Compares secret code with guessed code and create res array
    //res array contains 1 - if guessed digit is at exact position
    //0 - if guessed digit is at wrong position
    //-1 - if guessed digit is not present in secret code
    private int[] compareString(String secretCode, String guessedCode) {
        String c1 = secretCode, c2 = guessedCode;
        int[] res = new int[c1.length()];

        for (int i = 0; i < c1.length(); i++) {
            if (c1.charAt(i) == c2.charAt(i)) {
                res[i] = 1;

            } else if (secretCode.indexOf(c2.charAt(i)) >= 0 && secretCode.indexOf(c2.charAt(i)) < c2.length()) {
                res[i] = 0;

            } else {
                res[i] = -1;
            }
        }
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.
                menu, menu);
        return true;
    }
    //Player 1 thread
    public class Player1Runnable implements Runnable {


        int result1[] = new int[4];
        int next1[] = new int[4];
        int gc1[] = new int[4];
        private Bundle bundle_RG = new Bundle();
        private Bundle bundle_RE = new Bundle();

        public void run() {

            Looper.prepare();
            //getting reference for thread 1 handler
            Player1Handler = new Handler() {
                public void handleMessage(Message msg) {
                    int what = msg.what;
                    switch (what) {
                        //When a guess is received
                        case RECEIVE_GUESS:
                            if (stop == 0) {
                                break;
                            }
                            int[] res = compareString(s1, (String) msg.obj);
                            //Sending message to UI
                            Message msgUI1 = uiHandler.obtainMessage(SET_RESULT);

                            msgUI1.obj = res;
                            msgUI1.arg2 = 1;

                            bundle_RG.putString("key", (String) msg.obj);

                            msgUI1.setData(bundle_RG);
                            uiHandler.sendMessage(msgUI1);


                            break;
                            //Making the next guess
                        case RECEIVE_EVAL:

                            bundle_RE = msg.getData();
                            result1 = bundle_RE.getIntArray("res");
                            gc1 = bundle_RE.getIntArray("gc");
                            if(counter1 < 20) {
                                next1 = improvedGuessing(result1, gc1);
                                String s = "";
                                for (int i : next1)
                                    s += i;
                                //sending message to Player Thread 2
                                msg = Player2Handler.obtainMessage(RECEIVE_GUESS);
                                msg.obj = s;
                                Player2Handler.sendMessage(msg);

                                counter1++;
                            }
                            else{
                                stop = 0;
                                Toast.makeText(getApplicationContext(),"Past 20 Guesses",Toast.LENGTH_LONG).show();
                            }

                            break;
                    }

                }
            };

            //Using Collections class to shuffle and generate the secret code
            Message msg = uiHandler.obtainMessage(UPDATE_SECRET_CODE);
            List<Integer> numbers = new ArrayList<>();

            for (int i = 0; i <= 9; i++)
                numbers.add(i);
            Collections.shuffle(numbers);
            for (int i = 0; i <= 3; i++)
                s1 += numbers.get(i);
            msg.obj = s1;
            msg.arg1 = 1;
            uiHandler.sendMessage(msg);

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //making the initial guess as 1234
            msg = Player2Handler.obtainMessage(RECEIVE_GUESS);
            msg.obj = "1234";
            Player2Handler.sendMessage(msg);
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Looper.loop();


        }
        //To make next prediction based on the last guess and res array
        //Player 1 strategy
        int[] improvedGuessing(int[] arr, int[] last) {

            for (int i = 0; i <= 3; i++) {
                if (arr[i] == 1) {
                    a2[i] = last[i];
                    workQ2.remove((Integer) (last[i]));
                } else if (arr[i] == -1) {
                    workQ2.remove((Integer) last[i]);
                }

            }
            if (workQ2.isEmpty())
                return a2;
            Collections.shuffle(workQ2);

            int ctr = 0;

            int[] temp = new int[4];
            for (int i = 0; i <= 3; i++) {
                if (a2[i] != -1)
                    temp[i] = a2[i];
                else {
                    temp[i] = workQ2.get(ctr);
                    ctr++;
                }
            }

                return temp;
/*
            else
                return improvedGuessing(arr, last);
*/
        }
    }
    //Player 2 thread
    public class Player2Runnable implements Runnable {


        int result2[] = new int[4];
        int next2[] = new int[4];
        int gc2[] = new int[4];
        private Bundle bundle2_RE = new Bundle();
        private Bundle bundle2_RG = new Bundle();

        public void run() {

            Looper.prepare();
            //getting reference for Thread 2 handler
            Player2Handler = new Handler() {
                public void handleMessage(Message msg) {
                    int what = msg.what;
                    switch (what) {
                        case RECEIVE_GUESS:
                            //receiving guess
                            int[] res = compareString(s2, (String) msg.obj);
                            try {
                                sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (stop == 0) {
                                break;
                            }
                            //sending res array and guessed code to UI
                            Message msgUI2 = uiHandler.obtainMessage(SET_RESULT2);
                            msgUI2.obj = res;
                            msgUI2.arg2 = 2;
                            bundle2_RG.putString("key", (String) msg.obj);

                            msgUI2.setData(bundle2_RG);
                            uiHandler.sendMessage(msgUI2);

                            break;

                        case RECEIVE_EVAL:
                            //making next prediction
                            try {
                                sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            bundle2_RE = msg.getData();
                            result2 = bundle2_RE.getIntArray("res");
                            gc2 = bundle2_RE.getIntArray("gc");
                            if(counter2 < 20)
                            {
                            next2 = improvedGuessing(result2, gc2);

                            String s = "";
                            for (int i : next2)
                                s += i;
                            //sending our next guess to thread 1
                            msg = Player1Handler.obtainMessage(RECEIVE_GUESS);
                            msg.obj = s;
                            Player1Handler.sendMessage(msg);
                            counter2++;
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Past 20 Guesses",Toast.LENGTH_LONG).show();
                             stop = 0;
                            }
                            break;
                    }

                }
            };

            //using collection class to generate a random secret code
            Message msg = uiHandler.obtainMessage(UPDATE_SECRET_CODE);
            List<Integer> numbers = new ArrayList<>();


            for (int i = 0; i <= 9; i++)
                numbers.add(i);
            Collections.shuffle(numbers);
            for (int i = 0; i <= 3; i++)
                s2 += numbers.get(i);
            msg.obj = s2;
            msg.arg1 = 2;
            uiHandler.sendMessage(msg);


            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //making the intial guess as 0123
            msg = Player1Handler.obtainMessage(RECEIVE_GUESS);
            msg.obj = "0123";
            Player1Handler.sendMessage(msg);

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Looper.loop();//will check till messagequeue is empty



        }

        //To make next prediction based on the last guess and res array
        //player 2 guessing strategy
        int[] improvedGuessing(int[] arr, int[] last) {

            for (int i = 0; i <= 3; i++) {
                if (arr[i] == 1) {
                    a[i] = last[i];
                    workQ.remove((Integer) (last[i]));
                }

            }
            if (workQ.isEmpty())
                return a;
            Collections.shuffle(workQ);

            int ctr = 0;

            int[] temp = new int[4];
            for (int i = 0; i <= 3; i++) {
                if (a[i] != -1)
                    temp[i] = a[i];
                else {
                    temp[i] = workQ.get(ctr);
                    ctr++;

                }
            }
                return temp;
        }

    }
}
