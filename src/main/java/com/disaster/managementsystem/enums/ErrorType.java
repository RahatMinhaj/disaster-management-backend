package com.disaster.managementsystem.enums;

public enum ErrorType{

  // System
  SYS0001("System error."),
  SYS0002("Param is not acceptable."),
  SYS0003("You need use 'bearer' token."),
  SYS0004("Signature error."),
  SYS0005("Format error."),
  SYS0006("Invalid client"),
  SYS0007("Invalid token"),
  SYS0008("Request too frequently"),

  // COMMONS
  SYS0100("%s error."),
  SYS0110("%s create failed."),
  SYS0111("%s already existing, %s taken"),
  SYS0120("%s find error."),
  SYS0121("%s find error, no %s exists."),
  SYS0122("Cannot find any %s by %s param."),
  SYS0130("%s update failed."),
  SYS0131("%s's %s update failed."),
  SYS0140("%s delete failed."),

  // LOGIN
  LOG0001("User not exists."),
  LOG0002("Wrong password."),
  LOG0003("Disabled account."),
  LOG0004("Expired account."),
  LOG0005("Locked account."),
  LOG0006("Expired credentials."),
  LOG0007("Illegal token type."),


  //Custom Defined Error
  ALREADY_EXISTS("%s_ALREADY_EXISTS"),
  ALREADY_EXIST("%s_ALREADY_EXIST"),
  NOT_EXIST("%s_NOT_EXIST"),
  INVALID_INPUT("%s_INVALID_INPUT"),
  INVALID("%s_INVALID"),
  DATA_NOT_FOUND("%s_DATA_NOT_FOUND"),
  DATA_MISMATCH("%s_DATA_MISMATCH"),
  DATA_NOT_CREATED("%s_DATA_NOT_CREATED"),
  NOT_SAVED("%s_NOT_SAVED"),
  FAILED("%s_FAILED"),
  MOBILE_NUMBER_VERIFICATION_FAILED("%s_MOBILE_NUMBER_VERIFICATION_FAILED"),
  FILE_NOT_SUPPORTED("%s_FILE_NOT_SUPPORTED"),
  NOT_ALLOWED("%s_NOT_ALLOWED"),
  NOT_ALLOWED_NID_VERIFY("%s_NOT_ALLOWED_NID_VERIFY"),
  DATA_NOT_CONFIGURED("%s_DATA_NOT_CONFIGURED"),
  NOT_PERMITTED("%s_NOT_PERMITTED"),
  DATA_SHOULD_BE_NOT_NULL("%s_DATA_SHOULD_BE_NOT_NULL"),
  NOT_FOUND_OR_NOT_HANDLE_WORKFORCE("%s_NOT_FOUND_OR_NOT_HANDLE_WORKFORCE"),


  // Unknown error.
  UNKNOWN("unknown error."),;

  private final String description;

  ErrorType(String description) {
    this.description = description;
  }



  public static ErrorType parse(String name) {
    ErrorType[] errorTypes = ErrorType.values();
    for (ErrorType errorType : errorTypes) {
      if (errorType.name().equals(name)) {
        return errorType;
      }
    }
    return UNKNOWN;
  }
}
