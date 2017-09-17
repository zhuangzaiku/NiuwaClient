package com.smart.loginsharesdk.login.third;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    private String userId;
    private String userIcon;
    private String userName;
    private Gender gender;
    private String userGender;
    private String userToken;
    private String expiresTime;
    private String expiresIn;

    public UserInfo() {

    }

    protected UserInfo(Parcel in) {
        userId = in.readString();
        userIcon = in.readString();
        userName = in.readString();
        userGender = in.readString();
        userToken = in.readString();
        expiresTime = in.readString();
        expiresIn = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userIcon);
        dest.writeString(userName);
        dest.writeString(userGender);
        dest.writeString(userToken);
        dest.writeString(expiresTime);
        dest.writeString(expiresIn);
    }

    public static enum Gender {MALE, FEMALE}

}
