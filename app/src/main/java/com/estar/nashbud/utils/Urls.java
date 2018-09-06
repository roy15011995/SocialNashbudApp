package com.estar.nashbud.utils;

/**
 * Created by Sudipta on 10/31/2017.
 */

public class Urls {
    public static String MOBILE_NUMBER_OTP, RESEND_OTP, VERIFY_OTP, PROFILE_PIC_NAME, GET_PROFILE_DETAILS, EDIT_PROFILE_DETAILS,FILE_UPLOAD;

    static {

        MOBILE_NUMBER_OTP = "http://mojohubapi.estarsoftware.com/api/Auth/VerifyMobileNumber?MobileNumber=";
        RESEND_OTP = "http://mojohubapi.estarsoftware.com/api/Auth/ResendOTP?MobileNumber=";
        VERIFY_OTP = "http://mojohubapi.estarsoftware.com/api/Auth/VerifyOTP?MobileNumber=";
        PROFILE_PIC_NAME = "http://mojohubapi.estarsoftware.com/api/Auth/ProfilePictureNameUpload";
        GET_PROFILE_DETAILS = "http://mojohubapi.estarsoftware.com/api/Auth/GetProfileDetails?MobileNumber=";
        EDIT_PROFILE_DETAILS = "http://mojohubapi.estarsoftware.com/api/Auth/EditProfileDetails";
        FILE_UPLOAD="http://mojohubapi.estarsoftware.com/api/FileUpload";


    }
}
