package com.example.tvremote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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


public class PrviFragment extends Fragment {
    Spinner tvList;
    Button btnRemote;
    private RequestQueue mQue;
    private ArrayList<String> markeTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prvi_fragment, container, false);

        tvList = rootView.findViewById(R.id.tvList);
        tvList.setPrompt("Select your Projector");

        btnRemote = rootView.findViewById(R.id.btnRemote);
        markeTV = new ArrayList<>();
        mQue = Volley.newRequestQueue(getActivity());

        markeTV.add("Prvi");
        markeTV.add("Drugi");
        markeTV.add("Treci");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, markeTV);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tvList.setAdapter(adapter);


        //ucitajListu();

        btnRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String markaTV = tvList.getSelectedItem().toString();
                Bundle bundle = new Bundle();
                bundle.putString("TV", markaTV);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                DrugiFragment drugiFragment = new DrugiFragment();
                drugiFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.frame, drugiFragment);
                fragmentTransaction.commit();

            }
        });

        return rootView;
    }

    private void ucitajListu() {
        String url = "https://api.myjson.com/bins/p1jiq";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray niz = response.getJSONArray("SveMarke");
                    for (int i = 0; i < niz.length(); i++) {
                        JSONObject object = niz.getJSONObject(i);
                        String name = object.getString("Name");
                        markeTV.add(name);
                    }
                    markeTV.add("Cmar");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, markeTV);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tvList.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("asfsadfsd");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
            }
        });
        mQue.add(request);
    }
}
