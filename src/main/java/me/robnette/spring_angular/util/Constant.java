package me.robnette.spring_angular.util;

import io.jsonwebtoken.SignatureAlgorithm;

public class Constant {
    public static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$;%^&+=])(?=\\S+$).{8,20}";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_AUTHENTICATION = "Bearer ";

    public static final long TOKEN_TIMEOUT_INMILISECOND = 3600000;
    public static final String JWT_SECRET = "qzerAZER985aDF";
    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public final static String AUTHORITIES_KEY = "roles";
    public final static String UID_KEY = "uid";
//    public final static String EXPIRE_TIME_KEY = "expireTime";

    //@Value("${password.salt:''}")
    public final static String PASSWORD_SALT = "ee95a6FZ5sd8";



}
