package com.threespay.rxjavaserver;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ssl.SSLSockets;
import android.os.Bundle;
import android.util.Log;


import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CompositeDisposable disposable = new CompositeDisposable();
        Observable<ServerSocket> serverSocketObservable = Observable.create(emitter -> {
            if(emitter.isDisposed()) return;
            try {
                ServerSocket serverSocket = new ServerSocket(4000);
                emitter.onNext(serverSocket);
                emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
                });
        serverSocketObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<ServerSocket>() {
                              @Override
                              public void onSubscribe(@NonNull Disposable d) {
                                  disposable.add(d);
                              }

                              @Override
                              public void onNext(@NonNull ServerSocket serverSocket) {
                                  Observable<Socket>   socketObservable = Observable.create(emitter -> {
                                      try {
                                          serverSocket.setSoTimeout(1000);
                                          Socket socket = serverSocket.accept();
                                          emitter.onNext(socket);
                                      }catch (SocketException socketException){
                                          emitter.onError(socketException);
                                      }
                                  });
                                  socketObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                                          subscribe(new Observer<Socket>() {
                                              @Override
                                              public void onSubscribe(@NonNull Disposable d) {

                                              }

                                              @Override
                                              public void onNext(@NonNull Socket socket) {
                                                  Log.e("socket","Client join");
                                              }

                                              @Override
                                              public void onError(@NonNull Throwable e) {
                                                  Log.e("socket",e.getMessage());
                                              }

                                              @Override
                                              public void onComplete() {

                                              }
                                          });
                              }

                              @Override
                              public void onError(@NonNull Throwable e) {
                                  Log.e("server",e.getMessage());
                              }

                              @Override
                              public void onComplete() {

                              }
                          }
                );
    }

}