package com.kai.shoppingcart.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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


import com.kai.shoppingcart.R;
import com.kai.shoppingcart.databinding.FragmentLoginBinding;
import com.kai.shoppingcart.model.LoginData;



public class LoginFragment extends Fragment {


    AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    LoginViewModel loginViewModel;
    FragmentLoginBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getLoginLiveData().observe(getViewLifecycleOwner(), new Observer<LoginData>() {
            @Override
            public void onChanged(LoginData loginData) {
                if(loginData !=null){
                    spEditor.putString("id", loginData.getId());
                    spEditor.putString("jwtToken", loginData.getJwtToken());
                    spEditor.putString("customerId", loginData.getCustomerId());
                    spEditor.apply();
                    navigateToHome();
                }
            }
        });

        loginViewModel.getMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
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

    private void navigateToHome() {
        Toast.makeText(getContext(), "User Logged In", Toast.LENGTH_LONG).show();
        NavHostFragment.findNavController(this).navigate(R.id.action_LoginFragment_to_HomeFragment);
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

        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", "");
        String id = sharedPreferences.getString("id", "");
        String customerId = sharedPreferences.getString("customerId", "");
        spEditor = sharedPreferences.edit();

        if(jwtToken.length() != 0 && id.length() != 0 && customerId.length() != 0){
            navigateToHome();
        }

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });

        binding.buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
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
            loginViewModel.login(email,password);
        } else {
            builder.setMessage("Please fill out all required fields");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    void onRegistrationButtonClicked() {
        NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_LoginFragment_to_RegisterFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}