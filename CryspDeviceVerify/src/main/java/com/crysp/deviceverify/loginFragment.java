package com.crysp.deviceverify;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crysp.deviceverify.model.XConfirmModel;
import com.crysp.deviceverify.presenter.GetXCVRPresenter;
import com.crysp.deviceverify.presenter.XConfirmPresenter;
import com.crysp.sdk.CryspAPI;
import com.crysp.sdk.CryspNetworkManager;
import com.crysp.sdk.CryspResponse;
import com.devspark.appmsg.AppMsg;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* Need to Import These 3 Packages */


public class loginFragment extends Fragment implements com.crysp.deviceverify.presenter.XConfirmPresenter.View, GetXCVRPresenter.View {

    public static View loginview = null;

    private AppMsg.Style style = null;

    private static String userId;

    private EditText et2;
    private EditText et;
    private EditText et3;

    private int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private int ALL_PERMISSIONS = 100;

    private XConfirmPresenter XConfirmPresenter;
    private GetXCVRPresenter getXCVRPresenter;

    //API Key
    // Replace this with your own API Key
    private String MY_API_KEY = "06D0B2E1AE5C433997947BA348FA4742";
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_CONTACTS};
    private String xtid;

    public loginFragment() {

    }

    public static loginFragment newInstance(String user) {

        loginFragment fragment = new loginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        XConfirmPresenter = new XConfirmPresenter(this);
        getXCVRPresenter = new GetXCVRPresenter(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager manager = null;
        FragmentTransaction transactionm = null;

        //Log.i("Crysp", "ACTIVITY onOptionsItemSelected");
        switch (item.getItemId()) {


            case R.id.action_settings:

                userId = getActivity().getSharedPreferences("crysp", Context.MODE_PRIVATE).getString("email", "");
                manager = getActivity().getSupportFragmentManager();
                transactionm = manager.beginTransaction();
                transactionm.replace(R.id.activity_home_fragment_container, settingFragment.newInstance(userId), "settings");
                transactionm.addToBackStack(null);
                transactionm.commit();
                return super.onOptionsItemSelected(item);


            default:
                return super.onOptionsItemSelected(item);

        }

    }


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        loginview = view;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Check if Android 6.0+ and check if runtime permissions were granted
            if (arePermissionsEnabled()) { // All Permissions are Available
                CryspAPI.getInstance().initXAC(getActivity(), getActivity());
            } else {
                requestMultiplePermissions();
            }

        } else { // This is not Marshmallow. No runtime permissions required
            CryspAPI.getInstance().initXAC(getActivity(), getActivity());
        }


        final Button login = (Button) view.findViewById(R.id.login);

        et = view.findViewById(R.id.result);
        et2 = view.findViewById(R.id.result2);
        et3 = view.findViewById(R.id.result3);


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard (getActivity());

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        final String username = "test@crysp.com";

                        CryspNetworkManager.sendAysncRequest(getActivity(), username, MY_API_KEY, new CryspNetworkManager.getAsyncResponseCallback() {

                            @Override
                            public void getAsyncResponse(final CryspResponse cryspresponse) {

                                if (cryspresponse.getErrorCode() == 1) {
                                    //Do some Error Handling Here
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            style = new AppMsg.Style(5000, R.color.md_red_400);
                                            AppMsg appMsg = AppMsg.makeText(getActivity(), cryspresponse.getErrorMsg(), style);
                                            appMsg.setLayoutGravity(Gravity.TOP);
                                            appMsg.show();
                                            et2.setText(cryspresponse.getErrorCode()+ " - " + cryspresponse.getErrorMsg());
                                        }
                                    });
                                } else { // No Error. Received a valid Txn_ID

                                    // Now submit the user Credentials and CryspResponse
                                    // to your Authentication Server

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            et.setText("XTID: " + cryspresponse.getXtid() + "\nisDeviceInitialized " + cryspresponse.getIsDevInit()+ "\n" +
                                                    "TimeStamp: " + cryspresponse.getTimestamp() + "\nHMAC " + cryspresponse.getHmac());

                                            style = new AppMsg.Style(5000, R.color.md_teal_200);
                                            AppMsg appMsg = AppMsg.makeText(getActivity(), "Successfully Submitted Context Data", style);


                                            // Note: If this is the first time then Device is not Initialized yet.
                                            //This xtid and MY_API_KEY should be used to send Xconfirm if getIsDevInit is False;
                                            // Send Xconfirm from your Authentication server to initialize this device after out of band Authentication has been verified

                                            appMsg.setLayoutGravity(Gravity.TOP);
                                            appMsg.show();

                                            xtid = cryspresponse.getXtid();
                                            doSubmit(cryspresponse.getXtid(),  cryspresponse.getIsDevInit(),
                                                    cryspresponse.getTimestamp(),
                                                    cryspresponse.getHmac());
                                        }
                                    });

                                }

                            }
                        });


                    }
                });

                thread.run();

            }
        });


        return view;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    private void doSubmit(String txn_Id, Boolean isDeviceInitialized, String timestamp, String hmac){
        String sep = ".";
        String serverKey = "2484F4ED016C41FEAAC1BB894490B034";

        String shmac = "";
        String message = "";

        try {
            message = txn_Id + sep + String.valueOf(isDeviceInitialized) + sep + timestamp;
            // These values are obtained from the CryspResponse
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(serverKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            shmac = new String(Hex.encodeHex(sha256_HMAC.doFinal(message.getBytes("ASCII"))));
        } catch (Throwable e) {
            et2.setText("Error generating HMAC SHA256");
            shmac = "";
        }
        if(shmac.equals(hmac)) { // hmac value obtained from CryspResponse in Step 6
            // signature match
            Toast.makeText(getContext(), "match!", Toast.LENGTH_LONG).show();
            if (!isDeviceInitialized) {
                XConfirmPresenter.xConfirm(txn_Id, MY_API_KEY);
            } else {
                getXCVRPresenter.getXCVR(txn_Id, MY_API_KEY);
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled() {
        for (String permission : permissions) {
            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), ALL_PERMISSIONS);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ALL_PERMISSIONS) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Need Permissions to launch App")
                                .setPositiveButton("Allow", (dialog, which) -> requestMultiplePermissions())
                                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                                .create()
                                .show();
                    }
                    return;
                }
            }
            //all is good, continue flow

            CryspAPI.getInstance().initXAC(getActivity(), getActivity());
        }
    }


    @Override
    public void onSuccessGetVerif(XConfirmModel xConfirmModel) {
        et2.setText(xConfirmModel.getResult());
        getXCVRPresenter.getXCVR(xtid, MY_API_KEY);
    }

    @Override
    public void onErrorGetVerif(Throwable throwable) {
        Toast.makeText(getContext(), "match!", Toast.LENGTH_LONG).show();
        et2.setText(throwable.getMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XConfirmPresenter.unsubscribe();
        getXCVRPresenter.unsubscribe();
    }

    @Override
    public void onSuccessGetXCVR(XConfirmModel xConfirmModel) {
        et3.setText(new Gson().toJson(xConfirmModel));
    }

    @Override
    public void onErrorGetXCVR(Throwable throwable) {
        et3.setText(throwable.getMessage());
    }
} // End LoginFragment Class
