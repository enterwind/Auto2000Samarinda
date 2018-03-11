package vay.enterwind.auto2000samarinda.utils;

/**
 * Created by novay on 04/03/18.
 */

public class Config {

    // All We Need
    public static final String FIREBASE_TOKEN = "AAAALT-Y--Q:APA91bGWWg90qz_BKiblD-ALeluw3qTBd_V10b4okuE0vQO0leLqXPmHcaAR6-cLDp_y7z17D2X6HRPOGkdqczasgTxpShpI8CDLgpGUN4iTBOXdBf0OASfZ3J6XDAMzRKEzjVPXp4My";

    // List URL
    static public final String URL_LOGIN = "http://auto2000.enterwind.com/api/v1/login/";
    static public final String URL_PASSWORD_SAVE = "http://auto2000.enterwind.com/api/v1/password/update/";
    static public final String URL_PROFIL_SAVE = "http://auto2000.enterwind.com/api/v1/profil/update/";

    static public final String URL_WIDGET = "http://auto2000.enterwind.com/api/v1/widget/";
    static public final String URL_PLAN = "http://auto2000.enterwind.com/api/v1/planning/";
    static public final String URL_CHECKPOINT = "http://auto2000.enterwind.com/api/v1/checkpoint/";

    static public final String URL_WIDGET_OTHERS = "http://auto2000.enterwind.com/api/v1/widget-others/";
    static public final String URL_PROSPEK = "http://auto2000.enterwind.com/api/v1/prospek/";
    static public final String URL_TIMELINE = "http://auto2000.enterwind.com/api/v1/timeline?page=";

    static public final String URL_REBUTAN = "http://auto2000.enterwind.com/api/v1/rebutan/";

    static public final String URL_SALES = "http://auto2000.enterwind.com/api/v1/sales/";


    //JSON TAGS
    public static final String TAG_UUID = "uuid";
    public static final String TAG_PEGAWAI = "pegawai_id";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_TELEPON = "telepon";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_LATITUDE = "latitude";

    public static final String TAG_CATATAN = "catatan";
    public static final String TAG_JENIS = "jenis";

    public static final String TAG_TAKEN_BY = "taken_by";
    public static final String TAG_FOTO = "foto";
    public static final String TAG_STATUS = "status";
    public static final String TAG_PESAN = "pesan";

    public static final String TAG_CREATED_AT = "created_at";

    public static final String TAG_TGL_TAKEN_BY = "tgl_taken_by";
    public static final String TAG_TANGGAL = "tanggal";
    public static final String TAG_UPDATED_AT = "updated_at";
    public static final String TAG_DESC = "desc";
    public static final String TAG_OLEH = "oleh";
    public static final String TAG_KEPADA = "kepada";

    // Notifikasi
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";

}
