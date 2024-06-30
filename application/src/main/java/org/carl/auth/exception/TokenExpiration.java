package org.carl.auth.exception;

import org.carl.commons.BaseException;

public class TokenExpiration extends BaseException {
  public TokenExpiration(String s) {
    super(s);
  }
}
