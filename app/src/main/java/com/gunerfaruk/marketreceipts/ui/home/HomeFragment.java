package com.gunerfaruk.marketreceipts.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gunerfaruk.marketreceipts.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button chooseButton,uploadButton, cameraButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        chooseButton = root.findViewById(R.id.btnChoose);
        uploadButton = root.findViewById(R.id.btnUpload);
        cameraButton = root.findViewById(R.id.btnCamera);
        return root;
    }
}