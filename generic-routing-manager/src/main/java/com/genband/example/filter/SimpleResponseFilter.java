package com.genband.example.filter;

import com.netflix.zuul.ZuulFilter;

//@Component
public class SimpleResponseFilter extends ZuulFilter {

  @Override
  public boolean shouldFilter() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public Object run() {

    return null;
  }

  @Override
  public String filterType() {
    // TODO Auto-generated method stub
    return "post";
  }

  @Override
  public int filterOrder() {
    //send response filter = 1000
    return 1001;
  }

}
