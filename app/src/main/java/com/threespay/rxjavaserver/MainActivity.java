package com.threespay.rxjavaserver;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("output",Thread.currentThread().getName());

        Observable.merge(Observable.just("one", "two", "three", "four", "five"), Observable.just("1", "2", "3", "4", "5"))
                .subscribeOn(Schedulers.io()).map(s-> {
            Random rand = new Random(); //instance of random class
            int upperbound = 20;
            //generate random values from 0-24
            int random = rand.nextInt(upperbound);
            Thread.sleep(random);
            Log.e("output",Thread.currentThread().getName());
            return s;
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> Log.e("output",s));
    }
}