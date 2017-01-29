package com.genband.example.exception;

public class KubernetesServicesNotAvailableException extends Exception {

  private static final long serialVersionUID = 1L;

  public KubernetesServicesNotAvailableException(String message) {
    super(message);
  }

}
