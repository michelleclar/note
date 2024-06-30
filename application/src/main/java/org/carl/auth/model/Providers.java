package org.carl.auth.model;

public enum Providers {
  GITHUB("github");

  // GOOGLE("google");
  public final String providersName;

  Providers(String provider) {
    this.providersName = provider;
  }

  public static Providers of(String provider) {
    for (Providers p : Providers.values()) {
      if (p.providersName.equals(provider)) {
        return p;
      }
    }
    return null;
  }

}
