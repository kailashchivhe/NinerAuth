package com.kai.shoppingcart.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BCBeaconManager;
import com.bluecats.sdk.BCBeaconManagerCallback;
import com.bluecats.sdk.BlueCatsSDK;
import com.kai.shoppingcart.MainActivity;
import com.kai.shoppingcart.R;
import com.kai.shoppingcart.adapter.HomeItemAdapter;
import com.kai.shoppingcart.databinding.FragmentHomeBinding;
import com.kai.shoppingcart.model.Item;
import com.kai.shoppingcart.util.ApplicationPermission;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    static String CURRENT_BEACON = null;
    static String NEW_BEACON = null;
    static int COUNT = 0;
    static HashMap<String, String> data = new HashMap<>();
    static {
        data.put("B100", "produce");
        data.put("B200", "grocery");
        data.put("B300", "lifestyle");
    }

    HomeViewModel homeViewModel;
    SharedPreferences sharedPreferences;
    String email;
    String jwtToken;
    HomeItemAdapter homeItemAdapter;
    FragmentHomeBinding binding;
    ApplicationPermission applicationPermission;
    BCBeaconManager beaconManager = new BCBeaconManager();

    private final BCBeaconManagerCallback beaconManagerCallback = new BCBeaconManagerCallback()
    {
        @Override
        public void didRangeBlueCatsBeacons( final List<BCBeacon> beacons ) {
            Log.d("BLE", "didRangeBlueCatsBeacons: "+ beacons.size());
            super.didRangeBlueCatsBeacons(beacons);

            if(beacons.size() <= 0)
            {
                if(CURRENT_BEACON != null){
                    CURRENT_BEACON = null;
                    homeViewModel.getItems(jwtToken, "all");
                }
                return;
            }

            HashMap<BCBeacon.BCProximity, ArrayList<String>> groups = new HashMap<>();
            BCBeacon.BCProximity min = BCBeacon.BCProximity.BC_PROXIMITY_FAR;

            for(BCBeacon beacon : beacons)
            {
                ArrayList<String> group = groups.getOrDefault(beacon.getProximity(), new ArrayList<>());
                group.add(beacon.getName());
                groups.put(beacon.getProximity(), group);

                Log.d("ddd", "didRangeBlueCatsBeacons: " + beacon.getName() + " - " + beacon.getProximity());
                if(beacon.getProximity().getValue() < min.getValue()) min = beacon.getProximity();
            }

            if(min == BCBeacon.BCProximity.BC_PROXIMITY_FAR){
                if(CURRENT_BEACON != null){
                    CURRENT_BEACON = null;
                    homeViewModel.getItems(jwtToken, "all");
                }
                return;
            }

            ArrayList<String> closest_beacons = groups.get(min);
            if(CURRENT_BEACON == null || !closest_beacons.contains(CURRENT_BEACON))
            {
                if(NEW_BEACON != null && !closest_beacons.contains(NEW_BEACON)){
                    COUNT = 0;
                    NEW_BEACON = closest_beacons.get(0);
                    return;
                }

                if(NEW_BEACON == null){
                    COUNT = 0;
                    NEW_BEACON = closest_beacons.get(0);
                }

                if(COUNT++ > 4)
                {
                    CURRENT_BEACON = NEW_BEACON;
                    NEW_BEACON = null;
                    COUNT = 0;
                    homeViewModel.getItems(jwtToken, data.get(CURRENT_BEACON));
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        BlueCatsSDK.didEnterForeground();
        beaconManager.registerCallback(beaconManagerCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        BlueCatsSDK.didEnterBackground();
        beaconManager.unregisterCallback(beaconManagerCallback);
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
        applicationPermission = new ApplicationPermission( requireActivity() );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applicationPermission.verifyPermissions();
        }
        final Map<String, String> options = new HashMap<>();
        options.put(BlueCatsSDK.BC_OPTION_DISCOVER_BEACONS_NEARBY, "true");
        BlueCatsSDK.setOptions(options);
        BlueCatsSDK.startPurringWithAppToken(requireActivity(), "06e8c088-fae4-419c-aeb6-c56e8def1c42");
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
        homeItemAdapter.submit
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

        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        binding.recyclerViewHome.setLayoutManager(layoutManager);

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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            if (applicationPermission != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    applicationPermission.verifyPermissions();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (applicationPermission != null) {
            applicationPermission.onRequestPermissionResult(requestCode, permissions, grantResults);
        }
    }
}