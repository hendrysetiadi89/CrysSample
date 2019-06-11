package com.crysp.deviceverify.presenter;


import com.crysp.deviceverify.model.XConfirmModel;
import com.crysp.network.CryspDataSource;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class XConfirmPresenter {

    private CompositeDisposable composite = new CompositeDisposable();

    private View view;

    public interface View {
        void onSuccessGetVerif(XConfirmModel xConfirmModel);

        void onErrorGetVerif(Throwable throwable);
    }

    public XConfirmPresenter(XConfirmPresenter.View view) {
        this.view = view;
    }

    public void xConfirm(String xtid, String xaid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("xtid", xtid);
        map.put("xaid", xaid);
        CryspDataSource.getService().xconfirm(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<XConfirmModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        composite.add(d);
                    }

                    @Override
                    public void onNext(XConfirmModel xConfirmModel) {
                        if (!xConfirmModel.getErrorCode().equals("0")) {
                            view.onErrorGetVerif(new RuntimeException(xConfirmModel.getResult()));
                        } else {
                            view.onSuccessGetVerif(xConfirmModel);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onErrorGetVerif(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void unsubscribe() {
        composite.dispose();
    }
}
