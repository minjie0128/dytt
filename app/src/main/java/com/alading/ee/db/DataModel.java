package com.alading.ee.db;

import android.net.Uri;
import android.provider.BaseColumns;

import com.alading.ee.fusion.FusionCode;

/**
 * Data table structure definition class.
 */
public class DataModel {

	public static final class TableScanLog implements BaseColumns {

		private TableScanLog() {

		}

		public static final String TABLE_NAME = "table_exchange_log";

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ FusionCode.DB_AUTHORITY + "/" + TABLE_NAME);

		/**
		 * 手机用户
		 */
		public static final String SCAN_LOG_MOBILE = "mobile";
		/**
		 * 券号
		 */
		public static final String SCAN_LOG_NUMBER = "number";
		/**
		 * 兑换金额
		 */
		public static final String SCAN_LOG_MONEY_AMOUNT = "amount";
		/**
		 * 兑换时间
		 */
		public static final String SCAN_LOG_TIME = "log_time";
		/**
		 * 兑换结果，0:失败，1:成功
		 */
		public static final String SCAN_LOG_RESULT = "use_result";
		/**
		 * 兑换操作者用户名
		 */
		public static final String SCAN_LOG_USER_NAME = "user_name";
		/**
		 * 手机UDID
		 */
		public static final String SCAN_LOG_UDID = "udid";

		/**
		 * 日志记录状态，0:准备记录，1:记录成功
		 */
		public static final String SCAN_LOG_STATUS = "log_status";

		/**
		 * 日志记录状态，0:未上传，1:已上传
		 */
		public static final String SCAN_LOG_UPLOAD_STATUS = "upload_status";

		/**
		 * 订单生成时间
		 */
		public static final String SCAN_LOG_ORDER_EXCHANGE_TIME = "order_exchange_time";

		/**
		 * 订单失效时间
		 */
		public static final String SCAN_LOG_ORDER_EXPIRATION_TIME = "order_expiration_time";
		
		public static final String SCAN_LOG_ORDER_BARCODE = "order_bar_code";
	}

	private DataModel() {

	}

}
