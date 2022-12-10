package com.gg.sellers;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsFragment extends Fragment {

    public AboutUsFragment()
    {

    }

    private ImageView whatsappBtn;
    private TextView tvt2,aboutus,contactus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        whatsappBtn = view.findViewById(R.id.whatsappBtn);
        tvt2 = view.findViewById(R.id.txt_2);
        aboutus = view.findViewById(R.id.heading);
        contactus = view.findViewById(R.id.contactusbtn);
        tvt2.setPaintFlags(tvt2.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        aboutus.setPaintFlags(aboutus.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        contactus.setPaintFlags(contactus.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String whatsappURL = "https://wa.me/+917456989840?text=Hi,is anyone Available?";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(whatsappURL));
                startActivity(intent);

            }
        });

        return view;
    }
}