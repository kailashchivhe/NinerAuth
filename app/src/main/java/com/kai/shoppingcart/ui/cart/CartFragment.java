package com.kai.shoppingcart.ui.cart;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.kai.shoppingcart.MainActivity;
import com.kai.shoppingcart.R;
import com.kai.shoppingcart.adapter.CartItemAdapter;
import com.kai.shoppingcart.databinding.FragmentCartBinding;
import com.kai.shoppingcart.model.Item;

import java.util.List;


public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    FragmentCartBinding binding;
    List<Item> list;
    String total;
    String jwtToken;
    String customerId;
    CartItemAdapter cartItemAdapter;
    SharedPreferences sharedPreferences;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if( result.getResultCode() == MainActivity.RESULT_OK ) {
                DropInResult dropresult = result.getData().getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = dropresult.getPaymentMethodNonce().getNonce();
                cartViewModel.transaction(jwtToken,paymentMethodNonce,total);
            }
            else{
                Toast.makeText(getContext(), "Error occured", Toast.LENGTH_LONG).show();
            }
        }
    });

    public static CartFragment newInstance(String param1) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = getArguments().<Item>getParcelableArrayList("list");
            total = getArguments().getString("totalPrice");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        cartViewModel.getMessageMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty()){
                    displayToastMessage(s);
                }
            }
        });
        cartViewModel.tokenMutableLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                String token = s;
                getPaymentNonce(token);
            }
        });
        cartViewModel.getTransactionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    navigateToHome();
                }
            }
        });
    }

    private void navigateToHome() {
        Toast.makeText(getContext(), "Transaction Complete", Toast.LENGTH_LONG).show();
        NavHostFragment.findNavController(this).navigate(R.id.action_CartFragment_to_HomeFragment);
    }

    private void getPaymentNonce(String token) {
        DropInRequest dropInRequest = new DropInRequest();
        dropInRequest.clientToken( token );
        activityResultLauncher.launch(dropInRequest.getIntent(getContext()));
    }

    private void displayToastMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItemAdapter = new CartItemAdapter(list);
        binding.recyclerViewCart.setAdapter(cartItemAdapter);
        binding.textViewCartTotal.setText("$: " + total);
        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("jwtToken", "");
        customerId = sharedPreferences.getString("customerId", "");
        binding.buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPayClicked();
            }
        });
    }

    private void onPayClicked() {
        cartViewModel.getToken(jwtToken, customerId);
    }
}