package com.beihui.market.tang.rx;

import com.beihui.market.api.ResultEntity;
import com.beihui.market.tang.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/18
 */

public class RxResponse {
    public static <T> ObservableTransformer<ResultEntity<T>, T> compatT() {
        return new ObservableTransformer<ResultEntity<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<ResultEntity<T>> upstream) {
                return upstream.flatMap(new Function<ResultEntity<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull final ResultEntity<T> entity) throws Exception {
                        if (entity.isSuccess()) {
                            return Observable.create(new ObservableOnSubscribe<T>() {
                                @Override
                                public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {
                                    try {
                                        emitter.onNext(entity.getData());
                                        emitter.onComplete();
                                    } catch (Exception exception) {
                                        emitter.onError(exception);
                                    }
                                }
                            });
                        } else {
                            return Observable.error(new ApiException(entity.getCode(), entity.getMsg()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static ObservableTransformer<ResultEntity<Object>, Object> compatO() {
        return new ObservableTransformer<ResultEntity<Object>, Object>() {
            @Override
            public ObservableSource<Object> apply(@NonNull Observable<ResultEntity<Object>> upstream) {
                return upstream.flatMap(new Function<ResultEntity<Object>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(@NonNull final ResultEntity<Object> entity) throws Exception {
                        if (entity.isSuccess()) {
                            return Observable.create(new ObservableOnSubscribe<Object>() {
                                @Override
                                public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Exception {
                                    try {
                                        Object object = entity.getData();
                                        if (object != null) {
                                            emitter.onNext(object);
                                        } else {
                                            emitter.onNext(1);//仅仅发送个1,告诉view是成功的，让view做出更新
                                        }
                                        emitter.onComplete();
                                    } catch (Exception exception) {
                                        emitter.onError(exception);
                                    }
                                }
                            });
                        } else {
                            return Observable.error(new ApiException(entity.getCode(), entity.getMsg()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
