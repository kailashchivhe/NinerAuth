package com.kai.ninerauth.ui.register;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.kai.ninerauth.R;
import com.kai.ninerauth.databinding.FragmentSecondBinding;
import com.kai.ninerauth.util.DataSingleton;

public class RegisterFragment extends Fragment implements RegisterListener {

    private FragmentSecondBinding binding;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
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

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClicked();
            }
        });

        binding.buttonRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterClicked();
            }
        });
    }

    void onCancelClicked() {
        NavHostFragment.findNavController(RegisterFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    void onRegisterClicked() {
        String firstName = binding.editTextRegisterFirstName.getText().toString();
        String lastName = binding.editTextRegisterLastName.getText().toString();
        String email = binding.editTextRegisterEmail.getText().toString();
        String password = binding.editTextRegisterPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
            try {
                DataSingleton.register(email, password, firstName, lastName, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            builder.setMessage("Please fill out all required fields");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void registered() {
        NavHostFragment.findNavController(RegisterFragment.this)
                .navigate(R.id.action_SecondFragment_to_ProfileFragment);
    }

    @Override
    public void registeredFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}