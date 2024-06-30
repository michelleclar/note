package org.carl.commons;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.Family;

public enum AppStatus implements Response.StatusType {
    LOGIN_ERROR(460, "Login error. Please check your username and password"),
    CODE_ERROR(461, "Verification code error"),
    USER_EMAIL_EXISTS(207, "User email already exists"),
    USER_NOT_REGISTERED(208, "User not registered"),
    OAUTH_ERROR(465, "Authentication failed"),
    EMAIL_FORMAT_ERROR(463, "Invalid email format"),
    ACCESS_DENIED(464, "Access denied");

    private final int code;
    private final String reason;
    private final Family family;

    AppStatus(final int statusCode, final String reasonPhrase) {
        this.code = statusCode;
        this.reason = reasonPhrase;
        this.family = Family.familyOf(statusCode);
    }

    /**
     * Get the class of status code.
     *
     * @return the class of status code.
     */
    @Override
    public Family getFamily() {
        return family;
    }

    /**
     * Get the associated status code.
     *
     * @return the status code.
     */
    @Override
    public int getStatusCode() {
        return code;
    }

    /**
     * Get the reason phrase.
     *
     * @return the reason phrase.
     */
    @Override
    public String getReasonPhrase() {
        return toString();
    }

    /**
     * Get the reason phrase.
     *
     * @return the reason phrase.
     */
    @Override
    public String toString() {
        return reason;
    }

    // public static Response toR(AppStatus appStatus) {}
}
