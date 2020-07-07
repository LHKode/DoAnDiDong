package com.example.mymall;

import java.util.List;

public class HomePageModel {
    public static final int BANNER_SLIDER =0;
    public static final int STRIP_AD_BANNER =1;
    public static final int HORIZONTAL_PRODUCT_VIEW =2;
    public static final int GRID_PRODUCT_VIEW =3;

    private int type;

    /////////// Banner Slide
    private List<SliderModel> sliderModelList;

    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }


    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    /////////// Banner Slide
    /////////// Strip ad
    private String resource;
    private String backgroundColor;

    public HomePageModel(int type, String resource, String backgroundColor) {
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /////////// Strip ad

    ///////////// Horizontal Product Layout && Grid Product Layout

    private String title;
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    private List<ViewAllProduct> viewAllProductList;


    public HomePageModel(int type, String title,String backgroundColor ,List<HorizontalProductScrollModel> horizontalProductScrollModelList,List<ViewAllProduct> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.viewAllProductList = viewAllProductList;
    }

    public List<ViewAllProduct> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<ViewAllProduct> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }
    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }



    ///////////// Horizontal Product Layout && Grid Product Layout
}
