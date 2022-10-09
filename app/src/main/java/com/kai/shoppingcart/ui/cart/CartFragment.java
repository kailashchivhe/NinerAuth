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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeClient;
import com.braintreepayments.api.DropInClient;
import com.braintreepayments.api.DropInListener;
import com.braintreepayments.api.DropInRequest;
import com.braintreepayments.api.DropInResult;
import com.braintreepayments.api.UserCanceledException;
import com.kai.shoppingcart.MainActivity;
import com.kai.shoppingcart.R;
import com.kai.shoppingcart.adapter.CartItemAdapter;
import com.kai.shoppingcart.databinding.FragmentCartBinding;
import com.kai.shoppingcart.model.Item;

import java.util.List;


public class CartFragment extends Fragment implements DropInListener {

    private CartViewModel cartViewModel;
    FragmentCartBinding binding;
    List<Item> list;
    String total;
    String jwtToken;
    String customerId;
    CartItemAdapter cartItemAdapter;
    SharedPreferences sharedPreferences;
    private DropInClient dropInClient;
    String TAG = "vidit";
//    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//        @Override
//        public void onActivityResult(ActivityResult result) {
//            if(result.getResultCode()== MainActivity.RESULT_OK){
//                DropInResult dropInResult = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
//                String paymentMethodNonce = dropInResult.getPaymentMethodNonce().getString();
//                String deviceDataFromTheClient = null;
//                cartViewModel.transaction(jwtToken,paymentMethodNonce,deviceDataFromTheClient,total);
//            }
//        }
//    });

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
                getPaymentNounce(token);
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

    private void getPaymentNounce(String token) {
        DropInRequest dropInRequest = new DropInRequest();

        dropInClient = new DropInClient(getActivity(),token,dropInRequest);

        dropInClient.setListener((DropInListener) this);
        dropInClient.fetchMostRecentPaymentMethod(getActivity(), (dropInResult, error) -> dropInResult.describeContents());
        dropInClient.launchDropInForResult(getActivity(),001);

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

    @Override
    public void onDropInSuccess(@NonNull DropInResult dropInResult) {
        String paymentMethodNonce = dropInResult.getPaymentMethodNonce().getString();
        String deviceDataFromTheClient = null;
        cartViewModel.transaction(jwtToken,paymentMethodNonce,deviceDataFromTheClient,total);
        // use the result to update your UI and send the payment method nonce to your server
    }

    @Override
    public void onDropInFailure(@NonNull Exception error) {
        if (error instanceof UserCanceledException) {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            // the user canceled
        } else {
            Toast.makeText(getContext(), "Error occured", Toast.LENGTH_LONG).show();
            // handle error
        }
    }
}