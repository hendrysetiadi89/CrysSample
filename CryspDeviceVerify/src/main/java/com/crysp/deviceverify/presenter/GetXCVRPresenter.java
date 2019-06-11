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

public class GetXCVRPresenter {

    private CompositeDisposable composite = new CompositeDisposable();

    private View view;

    public interface View {
        void onSuccessGetXCVR(XConfirmModel xConfirmModel);

        void onErrorGetXCVR(Throwable throwable);
    }

    public GetXCVRPresenter(GetXCVRPresenter.View view) {
        this.view = view;
    }

    public void getXCVR(String xtid, String xaid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("xtid", xtid);
        map.put("xaid", xaid);
        CryspDataSource.getService().getXcvr(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<XConfirmModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        composite.add(d);
                    }

                    @Override
                    public void onNext(XConfirmModel xConfirmModel) {
                        view.onSuccessGetXCVR(xConfirmModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onErrorGetXCVR(e);
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
