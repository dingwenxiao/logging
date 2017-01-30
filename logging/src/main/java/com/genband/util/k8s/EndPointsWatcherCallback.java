package com.genband.util.k8s;

import java.util.List;

public interface EndPointsWatcherCallback {
  public void sendAddressList(List<String> addressList);
}
