package com.disaster.managementsystem.component.route;

public abstract class ApiProvider {

    public static final String SEPARATOR = "/";
    public static final String DASH = "-";
    public static final String BASEPATH = SEPARATOR + "api";
    public static final String OPENBASE_PATH = "open";
    public static final String VERSION = "/v1";
    public static final String OPEN_PATH = BASEPATH + SEPARATOR + "v1/open/**";
    public static final String SWAGGER_PATH = SEPARATOR + "swagger-ui/**";
    public static final String API_DOCS_PATH = SEPARATOR + "v3/api-docs/**";

    public static final String OPEN_PARENTHESIS = "{";
    public static final String CLOSE_PARENTHESIS = "}";
    public static final String IDENTIFIER = SEPARATOR + OPEN_PARENTHESIS + "id" + CLOSE_PARENTHESIS;
    public static final String IDENTIFIER_USERNAME = SEPARATOR + OPEN_PARENTHESIS + "username" + CLOSE_PARENTHESIS;
    public static final String IDENTIFIER_CODE = SEPARATOR + OPEN_PARENTHESIS + "code" + CLOSE_PARENTHESIS;



    public static class User {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "user";
        public static final String USER_REGISTRATION = SEPARATOR + "registration";
        public static final String USER_LOGIN = SEPARATOR + "login";
        public static final String USER_LOGOUT = SEPARATOR + "logout";
        public static final String ME = SEPARATOR + "me";
    }

    public static class Crisis {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "crisis";
        public static final String CRISIS_IDENTIFIER = IDENTIFIER;
        public static final String VOLUNTEER_ASSIGNMENT = SEPARATOR + "volunteer-assignment" + IDENTIFIER;
    }

    public static class DonationFund {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "donations";
        public static final String DONATION_FUND_IDENTIFIER = IDENTIFIER;
        public static final String DAY_WISE_DONATION = "day-wise-donations";
    }

    public static class Division {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "divisions";
        public static final String DIVISION_IDENTIFIER = IDENTIFIER;
    }
    public static class District {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "districts";
        public static final String DISTRICT_IDENTIFIER = IDENTIFIER;
    }

    public static class PoliceStation {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "police-stations";
        public static final String POLICE_STATION_IDENTIFIER = IDENTIFIER;
    }

    public static class Inventory {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "inventories";
        public static final String INVENTORY_IDENTIFIER = IDENTIFIER;
    }

    public static class Dashboard {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "dashboards";
        public static final String DASHBOARD_IDENTIFIER = IDENTIFIER;
    }

    public static class Storage {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "file";
        public static final String STORAGE_IDENTIFIER = IDENTIFIER;
        public static final String STORAGE_UPLOAD = "/upload";
        public static final String GET_FILE_BY_NAME = "/get-file/by-name/{file-name}";
    }

    public static class Profile {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "profiles";
        public static final String PROFILE_IDENTIFIER = IDENTIFIER;
        public static final String PROFILE_CREATE_BY_ADMIN = SEPARATOR + "create-profile-by-admin";
    }

    public static class ProductCategory {
        public static final String ROOTPATH = BASEPATH + VERSION + SEPARATOR + "product-categories";
        public static final String PRODUCT_CATEGORY_IDENTIFIER = IDENTIFIER;
        public static final String ADD_JOB_SEEKER_TO_FOLDER = SEPARATOR + "add-job-seeker-to-folder" + SEPARATOR + OPEN_PARENTHESIS + "cvFolderId" + CLOSE_PARENTHESIS + SEPARATOR + OPEN_PARENTHESIS + "jobSeekerId" + CLOSE_PARENTHESIS;
        public static final String REMOVE_JOB_SEEKER_FROM_FOLDER = SEPARATOR + "remove-job-seeker-to-folder" + SEPARATOR + OPEN_PARENTHESIS + "cvFolderId" + CLOSE_PARENTHESIS + SEPARATOR + OPEN_PARENTHESIS + "jobSeekerId" + CLOSE_PARENTHESIS;
    }

}