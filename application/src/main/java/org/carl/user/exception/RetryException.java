package org.carl.user.exception;

import org.carl.commons.BaseException;

public class RetryException extends BaseException {
  public RetryException(String s) {
    super(s);
  }
}