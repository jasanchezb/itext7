package com.itextpdf.kernel;

import com.itextpdf.io.util.MessageFormatUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exception class for License-key version exceptions throw in the Version class
 */
public class LicenseVersionException extends  RuntimeException{

    public static final String NO_I_TEXT7_LICENSE_IS_LOADED_BUT_AN_I_TEXT5_LICENSE_IS_LOADED ="No iText7 License is loaded but an iText5 license is loaded.";
    public static final String THE_MAJOR_VERSION_OF_THE_LICENSE_0_IS_LOWER_THAN_THE_MAJOR_VERSION_1_OF_THE_CORE_LIBRARY ="The major version of the license ({0}) is lower than the major version ({1}) of the Core library.";
    public static final String THE_MAJOR_VERSION_OF_THE_LICENSE_0_IS_HIGHER_THAN_THE_MAJOR_VERSION_1_OF_THE_CORE_LIBRARY ="The major version of the license ({0}) is higher than the major version ({1}) of the Core library.";
    
    public static final String THE_MINOR_VERSION_OF_THE_LICENSE_0_IS_LOWER_THAN_THE_MINOR_VERSION_1_OF_THE_CORE_LIBRARY ="The minor version of the license ({0}) is lower than the minor version ({1}) of the Core library.";
    public static final String THE_MINOR_VERSION_OF_THE_LICENSE_0_IS_HIGHER_THAN_THE_MINOR_VERSION_1_OF_THE_CORE_LIBRARY ="The minor version of the license ({0}) is higher than the minor version ({1}) of the Core library.";

    public static final String VERSION_STRING_IS_EMPTY_AND_CANNOT_BE_PARSED = "Version string is empty and cannot be parsed.";
    public static final String MAJOR_VERSION_IS_NOT_NUMERIC ="Major version is not numeric";
    public static final String MINOR_VERSION_IS_NOT_NUMERIC ="Minor version is not numeric";
    public static final String UNKNOWN_EXCEPTION_WHEN_CHECKING_LICENSE_VERSION ="Unknown Exception when checking License version";

    public static final String LICENSE_FILE_NOT_LOADED = "License file not loaded.";
    /**
     * Object for more details
     */
    protected Object object;

    private List<Object> messageParams;

    /**
     * Creates a new instance of PdfException.
     *
     * @param message the detail message.
     */
    public LicenseVersionException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of PdfException.
     *
     * @param cause the cause (which is saved for later retrieval by {@link #getCause()} method).
     */
    public LicenseVersionException(Throwable cause) {
        this(UNKNOWN_EXCEPTION_WHEN_CHECKING_LICENSE_VERSION, cause);
    }

    /**
     * Creates a new instance of PdfException.
     *
     * @param message the detail message.
     * @param obj     an object for more details.
     */
    public LicenseVersionException(String message, Object obj) {
        this(message);
        this.object = obj;
    }

    /**
     * Creates a new instance of PdfException.
     *
     * @param message the detail message.
     * @param cause   the cause (which is saved for later retrieval by {@link #getCause()} method).
     */
    public LicenseVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of PdfException.
     *
     * @param message the detail message.
     * @param cause   the cause (which is saved for later retrieval by {@link #getCause()} method).
     * @param obj     an object for more details.
     */
    public LicenseVersionException(String message, Throwable cause, Object obj) {
        this(message, cause);
        this.object = obj;
    }

    @Override
    public String getMessage() {
        if (messageParams == null || messageParams.size() == 0) {
            return super.getMessage();
        } else {
            return MessageFormatUtil.format(super.getMessage(), getMessageParams());
        }
    }

    /**
     * Sets additional params for Exception message.
     *
     * @param messageParams additional params.
     * @return object itself.
     */
    public LicenseVersionException setMessageParams(Object... messageParams) {
        this.messageParams = new ArrayList<>();
        Collections.addAll(this.messageParams, messageParams);
        return this;
    }

    /**
     * Gets additional params for Exception message.
     */
    protected Object[] getMessageParams() {
        Object[] parameters = new Object[messageParams.size()];
        for (int i = 0; i < messageParams.size(); i++) {
            parameters[i] = messageParams.get(i);
        }
        return parameters;
    }
}