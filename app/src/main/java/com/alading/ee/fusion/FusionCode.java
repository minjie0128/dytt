
package com.alading.ee.fusion;

import android.os.Environment;

/**
 * Global constants.
 */
public class FusionCode {

    public static final String ALADING_PROPERTIES = "Alading_Properties";
    public static final String DB_AUTHORITY = "com.alading.ee";
    public static String DB_FILE_NAME = "alading.db";
    public final static String PKG_NAME = "com.alading.mobclient";

    // Global message ids, starting from 101.
    public static final int MSG_STORE_DOWNLOAD_COMPLETE = 101;
    public static final int MSG_NO_SDCARD = 102;
    public static final int MSG_AD_DOWNLOAD_COMPLETE = 103;
    public static final int MSG_INTERNAL_STORAGE_SPACE_NOT_ENOUGH = 104;
    public static final int MSG_FREE_SPACE_NOT_ENOUGH_TO_SAVE_FILE = 105;
    public static final int MSG_SDCARD_FREE_SPACE_TOO_LOW = 106;

    public static final int MSG_RETRIEVE_DATA_VERSION = 107;
    public static final int MSG_RETRIEVE_AD_INFO = 108;
    public static final int MSG_RETRIEVE_CITY = 109;
    public static final int MSG_RETRIEVE_CITY_AREA = 110;
    public static final int MSG_RETRIEVE_CHANNEL = 111;
    public static final int MSG_RETRIEVE_SHOP = 112;

    public static final int MSG_RETRIEVE_BASIC_PROVINCE = 113;
    public static final int MSG_RETRIEVE_BASIC_CITY = 114;
    public static final int MSG_RETRIEVE_BASIC_AREA = 115;
    public static final int MSG_RETRIEVE_RECHARGE_AMOUNT = 116;
    public static final int MSG_SYNC_GAME_INFO = 117;
    public static final int MSG_SYNC_GAME_PRODUCT = 118;
    public static final int MSG_SYNC_PLATE_NUMBER = 119;

    // Menu IDs of point exchange service.
    public static final String SVC_CHINA_TELECOM = "01";
    public static final String SVC_CHINA_TELECOM_ACTIVATION_CODE = "0103";
    public static final String SVC_CHINA_TELECOM_COCO_GIFT = "0102";
    public static final String SVC_CHINA_TELECOM_VOUCHER = "0101";

    public static final String SVC_SMART_CLUB = "02";
    public static final String SVC_SMART_CLUB_GIFT = "0202";
    public static final String SVC_SMART_CLUB_VOUCHER = "0201";

    public static final String SVC_STAFF_WELFARE = "03";
    public static final String SVC_STAFF_WELFARE_COCO_GIFT = "0302";
    public static final String SVC_STAFF_WELFARE_VOUCHER = "0301";

    public static final String SVC_PINGAN_MILES = "04";
    public static final String SVC_PINGAN_MILES_COCO_GIFT = "0402";
    public static final String SVC_PINGAN_MILES_VOUCHER = "0401";
    public static final String SVC_PINGAN_MILES_ACTIVATION = "0402";

    public static final String SVC_PINGAN_CAR_INSURANCE = "05";
    public static final String SVC_PINGAN_CAR_INSURANCE_COCO_GIFT = "0502";
    public static final String SVC_PINGAN_CAR_INSURANCE_GIFT = "0501";

    public static final String SVC_QUICK_EXCHANGE = "06";

    public static final String SVC_SPD_BANK = "07";

    public static final String JSON_RSP_ERROR = "0001";
    public static final String JSON_RSP_OK = "0000";
    public static final String JSON_RSP_SIG_FAIL = "1001";
    public static final String JSON_RSP_INSUFFICIENT_PARAM = "1002";
    public static final String JSON_RSP_PARAM_NULL = "1003";
    public static final String JSON_RSP_NO_QUALIFIED_RECORDS = "1111";
    public static final String JSON_RSP_NO_RECORDS = "0002";
    public static final String JSON_RSP_EXCHANGE_FAIL = "8888";
    public static final String JSON_RSP_SVC_EXCEPTION = "9999";

    // Bar code and QrCode dimension configuration.
    public static final int BAR_WIDTH = (int) (480 * FusionField.deviceDensity);
    public static final int BAR_HEIGHT = (int) (70 * FusionField.deviceDensity);

    public static final int QRCODE_SIZE = (int) (160 * FusionField.deviceDensity);
    public static final int QRCODE_LOGO_SIZE = (int) (40 * FusionField.deviceDensity);

    // File save path definition.
    public static final String SD_CARD_DIR = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    public final static String AD_PIC_PATH = SD_CARD_DIR + "/alading/ad";
    public static final String SYS_DATA_DIR = Environment.getDataDirectory().getAbsolutePath();
    public final static String DATA_BASE_PATH = SYS_DATA_DIR + "/data/" + PKG_NAME + "/databases/";

    // Update package save path without SD card.
    public final static String UPDATE_PACKAGE_SAVEPATH_NO_SDCARD = SYS_DATA_DIR + "/data/"
            + PKG_NAME + "/update/";

    // Ad auto slide interval
    public final static long AD_AUTO_SLIDE_INTERVAL = 5000;

    // Empty string definition
    public final static String EMPTY_STRING = "";

    // Agreement display type
    public static final int TEXT_DIPLAY_TYPE_MMILES = 0;
    public static final int TEXT_DIPLAY_TYPE_ALADING = 1;

    public static final int REDIRECT_PAGE_SHIP_ADDRESS = 0;
    public static final int REDIRECT_PAGE_POINT_ACCOUNT = 1;

    //recharge
    public static final String RECHARGE_PHONE="1";
    public static final String RECHARGE_ALIPAY="2";
    public static final String RECHARGE_ALIPAY_CODE="3";

    //Game Card Order Type
    public static final String ORDER_TYPE_GAME_CARD = "4";
    

    //Payment Pay Type
    public static final String PAY_TYPE_CASH = "0";
    public static final String PAY_TYPE_ALADING_ACCOUNT = "2";
    
    //Order Status
    public static final String ORDER_STATUS_NOT_PAY = "0";
    public static final String ORDER_STATUS_PAID = "1";
    
    

}
