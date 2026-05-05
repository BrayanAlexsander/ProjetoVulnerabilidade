package br.com.unisales.locadora.service;

public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }
}

