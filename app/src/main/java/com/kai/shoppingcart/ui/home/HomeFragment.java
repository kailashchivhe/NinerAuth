package com.kai.shoppingcart.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kai.shoppingcart.R;
import com.kai.shoppingcart.adapter.HomeItemAdapter;
import com.kai.shoppingcart.databinding.FragmentHomeBinding;
import com.kai.shoppingcart.model.Item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    String email;
    String jwtToken;
    HomeItemAdapter homeItemAdapter;
    FragmentHomeBinding binding;
    String TAG="Vidit";

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            onProfileClicked();
            return true;
        }
        else if (id == R.id.action_logout) {
            onLogoutClicked();
            return true;
        }
        return false;
    }

    private void onLogoutClicked() {
        //Delete preferences
        sharedPreferences.edit().remove("jwtToken").commit();
        sharedPreferences.edit().remove("email").commit();
        sharedPreferences.edit().remove("customerId").commit();
        NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_LoginFragment);
    }

    private void onProfileClicked() {
        NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_ProfileFragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getMessageMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                displayMessageToast(s);
            }
        });

        homeViewModel.getItemsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                setItemsOnView(items);
            }
        });
    }

    private void setItemsOnView(List<Item> items) {
        homeItemAdapter = new HomeItemAdapter(items);
        binding.recyclerViewHome.setAdapter(homeItemAdapter);
    }

    private void displayMessageToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("jwtToken", "");
        email = sharedPreferences.getString("email", "");

        homeViewModel.getItems(jwtToken);
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartButtonClicked();
            }
        });
    }

    private void onCartButtonClicked() {
        Bundle bundle = new Bundle();
        List<Item> list = homeItemAdapter.getCartItemList();
        bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>)list);
        bundle.putString("totalPrice", getTotalPrice(list));
        NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_CartFragment, bundle);
    }

    private String getTotalPrice(List<Item> cartItemList){
        double total = 0.0;
        for(Item item: cartItemList){
            double finalPrice = item.getPrice() - ((item.getDiscount() * item.getPrice())/100);
            total += (finalPrice * item.getQuantity());
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

}