package br.com.unisales.locadora.service;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}

