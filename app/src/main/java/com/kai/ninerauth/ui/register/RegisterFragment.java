package com.kai.ninerauth.ui.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.kai.ninerauth.R;
import com.kai.ninerauth.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    AlertDialog.Builder builder;
    RegisterViewModel registerViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        registerViewModel.getRegisterLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(getContext(), "User Registered", Toast.LENGTH_LONG).show();
                    navigateToLogin();
                }
            }
        });
        registerViewModel.getMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty()){
                    displayMessageToast(s);
                }
            }
        });
    }

    private void displayMessageToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    private void navigateToLogin() {
        NavHostFragment.findNavController(this).navigate(R.id.action_RegisterFragment_to_LoginFragment);
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

        binding.buttonRegisterCancel.setOnClickListener(new View.OnClickListener() {
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
        navigateToLogin();
    }

    void onRegisterClicked() {
        String firstName = binding.editTextRegisterFirstName.getText().toString();
        String lastName = binding.editTextRegisterLastName.getText().toString();
        String email = binding.editTextRegisterEmail.getText().toString();
        String password = binding.editTextRegisterPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
            registerViewModel.registeration(email, password, firstName, lastName);
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
}