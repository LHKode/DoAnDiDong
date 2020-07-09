package com.example.mymall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.mymall.DBqueries.categoryModelList;
import static com.example.mymall.DBqueries.lists;
import static com.example.mymall.DBqueries.loadCategories;
import static com.example.mymall.DBqueries.loadFragmentData;
import static com.example.mymall.DBqueries.loadedCategoriesNames;
//import static com.example.mymall.DBqueries.loadedCategoriesNames;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =inflater.inflate(R.layout.fragment_home, container, false);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
       // if(networkInfo !=null && networkInfo.isConnected()== true) {
//            noInternetConnection.setVisibility(View.GONE);
//           // categoryRecyclerView.setVisibility(View.VISIBLE);
//            homePageRecyclerView.setVisibility(View.VISIBLE);
            Main2Activity.drawer.setDrawerLockMode(0);
            categoryRecyclerView= view.findViewById(R.id.category_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categoryRecyclerView.setLayoutManager(layoutManager);
            categoryAdapter= new CategoryAdapter(categoryModelList);
            categoryRecyclerView.setAdapter(categoryAdapter);

            if(categoryModelList.size() == 0){
                loadCategories(categoryAdapter,getContext());
            }
            else {
                categoryAdapter.notifyDataSetChanged();
            }

            homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
            LinearLayoutManager testingLayoutManeger = new LinearLayoutManager(getContext());
            testingLayoutManeger.setOrientation(LinearLayoutManager.VERTICAL);
            homePageRecyclerView.setLayoutManager(testingLayoutManeger);

            if(lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                adapter = new HomePageAdapter(lists.get(0));
                loadFragmentData(adapter,getContext(),0,"Home");
            }
            else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);
        //}
//        else{
//            categoryRecyclerView.setVisibility(View.GONE);
//            homePageRecyclerView.setVisibility(View.GONE);
//            Glide.with(this).load(R.drawable.bin).into(noInternetConnection);
//            noInternetConnection.setVisibility(View.VISIBLE);
//            Main2Activity.drawer.setDrawerLockMode(1);
//        }

        return view;
    }
}
