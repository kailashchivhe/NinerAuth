package com.kai.ninerauth.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.kai.ninerauth.R;
import com.kai.ninerauth.databinding.FragmentFirstBinding;
import com.kai.ninerauth.ui.register.RegisterFragment;
import com.kai.ninerauth.util.DataSingleton;

public class LoginFragment extends Fragment implements LoginListener {

    private FragmentFirstBinding binding;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
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

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });

        binding.buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegistrationButtonClicked();
            }
        });
    }

    void onLoginButtonClicked() {
        String email = binding.editTextLoginEmail.getText().toString();
        String password = binding.editTextLoginPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()) {
            DataSingleton.login(email, password, this);
        } else {
            builder.setMessage("Please fill out all required fields");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    void onRegistrationButtonClicked() {
        NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void loggedIn() {
        NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_FirstFragment_to_ProfileFragment);
    }

    @Override
    public void loggedInFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}