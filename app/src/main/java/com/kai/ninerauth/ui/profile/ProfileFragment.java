package com.kai.ninerauth.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kai.ninerauth.R;
import com.kai.ninerauth.databinding.FragmentProfileBinding;
import com.kai.ninerauth.listener.ProfileRetrivalListener;
import com.kai.ninerauth.listener.ProfileUpdateListener;
import com.kai.ninerauth.ui.login.LoginViewModel;

public class ProfileFragment extends Fragment implements ProfileRetrivalListener, ProfileUpdateListener {

    private FragmentProfileBinding binding;
    AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        binding.buttonProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateClicked();
            }
        });

        binding.buttonProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutClicked();
            }
        });
    }

    void onLogoutClicked() {
        //TODO: logout user and delete jwt token
        NavHostFragment.findNavController(ProfileFragment.this)
                .navigate(R.id.action_ProfileFragment_to_LoginFragment);
    }

    void onUpdateClicked() {
        //TODO: update profile
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void profileRetrivalSuccessful() {

    }

    @Override
    public void profileRetrivalFailure(String message) {

    }

    @Override
    public void profileUpdateSuccessful() {

    }

    @Override
    public void profileUpdateFailure(String message) {

    }
}