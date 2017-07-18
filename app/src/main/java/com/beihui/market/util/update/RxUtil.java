package com.beihui.market.util.update;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class RxUtil {

    public static <T> ObservableTransformer<T, T> io2main() {    //compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


//    public static <T> Observable rxCreateDiskObservable(final String key, final Class<T> clazz) {
//        return Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                String json = ACache.get(App.getInstance()).getAsString(key);
//                if (!TextUtils.isEmpty(json)) {
//                    subscriber.onNext(json);
//                }
//                subscriber.onCompleted();
//            }
//        })
//                .map(new Func1<String, T>() {
//                    @Override
//                    public T call(String s) {
//                        return new Gson().fromJson(s, clazz);
//                    }
//                })
//                .subscribeOn(Schedulers.io());
//    }
//
//    public static <T> Observable.Transformer<T, T> rxCacheListHelper(final String key) {
//        return new Observable.Transformer<T, T>() {
//            @Override
//            public Observable<T> call(Observable<T> observable) {
//                return observable
//                        .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
//                        .doOnNext(new Action1<T>() {
//                            @Override
//                            public void call(final T data) {
//                                Schedulers.io().createWorker().schedule(new Action0() {
//                                    @Override
//                                    public void call() {
//                                        //通过反射获取List,再判空决定是否缓存
//                                        Class clazz = data.getClass();
//                                        Field[] fields = clazz.getFields();
//                                        for (Field field : fields) {
//                                            String className = field.getType().getSimpleName();
//                                            // 得到属性值
//                                            if (className.equalsIgnoreCase("List")) {
//                                                try {
//                                                    List list = (List) field.get(data);
//                                                    if (list != null && !list.isEmpty()) {
//                                                        ACache.get(App.getInstance())
//                                                                .put(key, new Gson().toJson(data, clazz));
//                                                    }
//                                                } catch (IllegalAccessException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }
//                                    }
//                                });
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }
//
//    public static <T> Observable.Transformer<T, T> rxCacheBeanHelper(final String key) {
//        return new Observable.Transformer<T, T>() {
//            @Override
//            public Observable<T> call(Observable<T> observable) {
//                return observable
//                        .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
//                        .doOnNext(new Action1<T>() {
//                            @Override
//                            public void call(final T data) {
//                                Schedulers.io().createWorker().schedule(new Action0() {
//                                    @Override
//                                    public void call() {
//                                        ACache.get(App.getInstance())
//                                                .put(key, new Gson().toJson(data, data.getClass()));
//                                    }
//                                });
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }


}
