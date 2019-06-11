package com.crysp.deviceverify;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.crysp.sdk.CryspAPI;

public class settingFragment extends Fragment {

    EditText xas, asp;



    private static int numSamplesRequired = 7;
    private String auid;


    public settingFragment() {

    }

    public static settingFragment newInstance(String userid) {

        settingFragment fragment = new settingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getActionBar().setTitle(Html.fromHtml("<small>Settings</small>"));

        View view = inflater.inflate(R.layout.settings, container, false);

        final SharedPreferences spf = getActivity().getSharedPreferences("crysp", Context.MODE_PRIVATE);
        final SharedPreferences cryspspf = getActivity().getSharedPreferences("crysp", Context.MODE_PRIVATE);

        xas = (EditText) view.findViewById(R.id.xasedt);
        xas.setText(spf.getString("xas", "https://xas-demo.crysp.com/"));
        //xas.setText(spf.getString("xas", "https://xas-asia.crysp.com/"));

        asp = (EditText) view.findViewById(R.id.aspedt);
        asp.setText(spf.getString("asp", "https://xas-demo.crysp.com/"));
        //asp.setText(spf.getString("asp", "https://xas-asia.crysp.com/"));

        auid = getArguments().getString("userid") == null ? "" : getArguments().getString("userid");




        final Button reset_button = (Button) view.findViewById(R.id.reset_button);
        reset_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete App Data ?");
                builder.setMessage("All Application data will be deleted.");
                builder.setCancelable(false);
                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                CryspAPI.getInstance().resetAppData(getActivity());
                            }
                        });
                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                //getActivity().finish();
                                // Need to delete a bunch of things here
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.settings, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        InputMethodManager inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

		/*
         * getActivity().getWindow().setSoftInputMode(
		 * WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		 */

        switch (item.getItemId()) {

            case R.id.save:

                SharedPreferences spf = getActivity().getSharedPreferences("crysp", Context.MODE_PRIVATE);
                SharedPreferences cryspspf = getActivity().getSharedPreferences("crysp", Context.MODE_PRIVATE);
                Editor editor = spf.edit();

                editor.putString("xas", xas.getText().toString());
                //DeviceUtility.setServer(getActivity(), xas.getText().toString());
                CryspAPI.getInstance().setServerURL(getActivity(), xas.getText().toString());

                editor.putString("asp", asp.getText().toString());

                editor.commit();


                break;

            case R.id.cancel:

                break;
        }

        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
