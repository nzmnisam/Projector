package com.example.tvremote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrugiFragment extends Fragment {
    private  String CMD_TV_POWER = "0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e";
    private  String CMD_TV_CH_NEXT = "0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 0015 0015 0040 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e";
    private  String CMD_TV_CH_PREV = "0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e";
   // private  String CMD_SB_VOLUP = "0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e";
   // private  String CMD_SB_VOLDOWN = "0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e";
    private ConsumerIrManager irManager;
    private RequestQueue mQue;
    private String izabranTV;
    private Button btnBack;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.drugi_fragment, container, false);
        Bundle bundle = getArguments();
        izabranTV = bundle.getString("TV");
        Log.d("MARKA", izabranTV);
        btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                fragmentTransaction.replace(R.id.frame, new PrviFragment());
                fragmentTransaction.commit();
            }
        });
        irManager = (ConsumerIrManager) getActivity().getSystemService(Context.CONSUMER_IR_SERVICE);
        mQue = Volley.newRequestQueue(getActivity());
        ucitajSpec(izabranTV, rootView);
        return rootView;
    }

    private void ucitajSpec(final String marka, View rootView) {
        String url = "https://api.myjson.com/bins/p1jiq";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray niz = response.getJSONArray("SveMarke");
                    for (int i = 0; i < niz.length(); i++) {
                        JSONObject object = niz.getJSONObject(i);
                        String name = object.getString("Name");
                        if (name.equals(marka)) {
                            CMD_TV_POWER = object.getString("Power");
                            CMD_TV_CH_NEXT = object.getString("Ch+");
                            CMD_TV_CH_PREV = object.getString("Ch-");
                          //  CMD_SB_VOLUP = object.getString("Vol+");
                          //  CMD_SB_VOLDOWN = object.getString("Vol-");
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
            }
        });
        mQue.add(request);
        rootView.findViewById(R.id.tvpower).setOnClickListener(new ClickListener(hex2ir(CMD_TV_POWER)));
        rootView.findViewById(R.id.tvchnext).setOnClickListener(new ClickListener(hex2ir(CMD_TV_CH_NEXT)));
        rootView.findViewById(R.id.tvchprev).setOnClickListener(new ClickListener(hex2ir(CMD_TV_CH_PREV)));
//        rootView.findViewById(R.id.sbvoldown).setOnClickListener(new ClickListener(hex2ir(CMD_SB_VOLDOWN)));
//        rootView.findViewById(R.id.sbvolup).setOnClickListener(new ClickListener(hex2ir(CMD_SB_VOLUP)));
    }

    // based on code from http://stackoverflow.com/users/1679571/randy (http://stackoverflow.com/a/25518468)
    private IRCommand hex2ir(final String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData.split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2
        frequency = (int) (1000000 / (frequency * 0.241246));
        int pulses = 1000000 / frequency;
        int count;
        int[] pattern = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            count = Integer.parseInt(list.get(i), 16);
            pattern[i] = count * pulses;
        }
        return new IRCommand(frequency, pattern);
    }

    private class ClickListener implements View.OnClickListener {
        private final IRCommand cmd;

        public ClickListener(final IRCommand cmd) {
            this.cmd = cmd;
        }

        @Override
        public void onClick(final View view) {
            android.util.Log.d("Remote", "frequency: " + cmd.freq);
            android.util.Log.d("Remote", "pattern: " + Arrays.toString(cmd.pattern));
            irManager.transmit(cmd.freq, cmd.pattern);
        }
    }

    private class IRCommand {
        private final int freq;
        private final int[] pattern;

        private IRCommand(int freq, int[] pattern) {
            this.freq = freq;
            this.pattern = pattern;
        }
    }
}
