package com.priyank.wallday.utils

object Constants {
    const val PUSH_NOTIFICATION_CHANNEL_ID = "PUSH_NOTIFICATION_WALL_DAY"

    const val EXTRA_PHOTO_ITEM = "PHOTO_ITEM"
    const val EXTRA_IS_IMAGE_SELECTED = "IS_IMAGE_SELECTED"
    const val EXTRA_SELECT_IMAGE_WEEK_POSITION = "SELECT_IMAGE_WEEK_POSITION"
    const val EXTRA_SELECTED_IMAGE_PATH = "EXTRA_SELECTED_IMAGE_PATH"

    const val MINIMUM_DISTANCE_TO_MOVE_MARKER = 20
    const val GEO_FENCE_RADIUS_IN_METERS = 500.0f

    const val VIEW_TYPE_RECYCLE_ITEM = 0
    const val VIEW_TYPE_SHIMMER_ITEM = 1

    const val IMAGE_TYPE_CAMERA = 0
    const val IMAGE_TYPE_GALLERY = 1

    const val API_OFFSET_ITEM = 15

    const val CURRENCY_SIGN = "€"
    const val CURRENCY_CODE = "EUR"

    const val PAYMENT_STATUS_PAID = "paid"

    //region Preference Constants
    const val PREF_FIREBASE_TOKEN = "PREF_FIREBASE_TOKEN"
    const val PREF_BEARER_TOKEN = "PREF_BEARER_TOKEN"

    const val PREF_IS_INTRODUCTION_FINISHED_BOOL = "PREF_IS_INTRODUCTION_FINISHED"
    const val PREF_IS_USER_LOGIN_BOOL = "PREF_USER_LOGIN_BOOL"

    const val PREF_USER_ID_INT = "PREF_USER_ID"
    const val PREF_USER_FIRST_NAME = "PREF_USER_FIRST_NAME"
    const val PREF_USER_LAST_NAME = "PREF_USER_LAST_NAME"
    const val PREF_USER_PHONE_NUMBER = "PREF_USER_PHONE_NUMBER"
    const val PREF_USER_COUNTRY_CODE = "PREF_USER_COUNTRY_CODE"
    const val PREF_USER_EMAIL = "PREF_USER_EMAIL"
    const val PREF_USER_PROFILE_IMAGE = "PREF_USER_PROFILE_IMAGE"
    const val PREF_USER_IS_VERIFIED_BOOL = "PREF_USER_IS_VERIFIED"
    const val PREF_USER_SELECTED_CITY_ID_INT = "PREF_USER_SELECTED_CITY_ID"
    const val PREF_USER_WALLET = "PREF_USER_WALLET"
    const val PREF_USER_NOTIFICATION_COUNT_INT = "PREF_USER_NOTIFICATION_COUNT"
    const val PREF_PRICE_PER_MINUTE = "PREF_PRICE_PER_MINUTE"
    const val PREF_IS_LOCATION_FRAGMENT_SHOWN_BOOL = "PREF_IS_LOCATION_FRAGMENT_SHOWN_BOOL"

    //Subscription related preference
    const val PREF_HAS_USER_PURCHASED_SUBSCRIPTION_BOOL = "PREF_HAS_USER_PURCHASED_SUBSCRIPTION"
    const val PREF_USER_PURCHASED_SUBSCRIPTION_ID_INT = "PREF_USER_PURCHASED_SUBSCRIPTION_ID"
    const val PREF_USER_PURCHASED_SUBSCRIPTION_PRICE_FLOAT =
        "PREF_USER_PURCHASED_SUBSCRIPTION_PRICE"
    const val PREF_USER_PURCHASED_SUBSCRIPTION_PERIOD_INT =
        "PREF_USER_PURCHASED_SUBSCRIPTION_PERIOD"
    const val PREF_USER_PURCHASED_SUBSCRIPTION_END_DATE =
        "PREF_USER_PURCHASED_SUBSCRIPTION_END_DATE"
    const val PREF_USER_PURCHASED_SUBSCRIPTION_START_DATE =
        "PREF_USER_PURCHASED_SUBSCRIPTION_START_DATE"

    //Bike booking related preference
    const val PREF_IS_BOOKING_AVAILABLE_BOOL = "PREF_IS_BOOKING_AVAILABLE_BOOL"
    const val PREF_CURRENT_BIKE_BOOKING_DATA = "PREF_CURRENT_BIKE_BOOKING_DATA"
    //endregion

    //region EXTRAS
    const val EXTRA_COMING_FROM_INT = "COMING_FROM"

    const val EXTRA_PAYMENT_BROADCAST = "com.cesano.payment.broadcast"
    const val EXTRA_PAYMENT_ID = "PAYMENT_ID"
    const val EXTRA_BIKE_BOOKING_ID_INT = "BIKE_BOOKING_ID"
    const val EXTRA_BIKE_BOOKING_USER_ID_INT = "BIKE_BOOKING_USER_ID"
    const val EXTRA_SUBSCRIPTION_WALLET_SHARED_MODEL = "EXTRA_SUBSCRIPTION_WALLET_SHARED_MODEL"
    const val EXTRA_ALERT_TITLE = "EXTRA_ALERT_TITLE"
    const val EXTRA_ALERT_OK_STRING = "EXTRA_ALERT_OK_STRING"
    const val EXTRA_ALERT_CANCEL_STRING = "EXTRA_ALERT_CANCEL_STRING"
    const val EXTRA_ACTIVITY_RESULT_REQUEST_CODE = 101
    const val EXTRA_CAMERA_REQUEST_CODE = 102
    const val EXTRA_GALLERY_REQUEST_CODE = 103
    const val EXTRA_LOCATION_REQUEST_CODE = 104

    const val INTENT_ACTION_BIKE_EXTEND = "com.cesano.action.bike_extend"
    const val INTENT_FILTER_BIKE_EXTEND = "com.cesano.filter.bike_extend"

    const val FROM_SIDE_MENU = 0
    const val FROM_SIGN_IN = 1
    const val FROM_SIGN_UP = 2
    const val FROM_FORGOT_PASSWORD = 3
    const val FROM_SPLASH = 4
    const val FROM_DASH_BOARD = 5
    const val FROM_BIKE_DETAIL = 6
    const val FROM_SETTINGS = 7
    const val FROM_LOG_OUT = 8
    const val FROM_SUBSCRIPTION = 9
    //endregion

    //region CMS Type
    const val PRIVACY_POLICY = 1
    const val TERMS_CONDITION = 2
    const val ABOUT_US = 3
    //endregion

    //region Bike Status
    //This status will come when we have booked the bike but is yet to pickup.
    const val STATUS_BIKE_BOOKED = 1

    //This status will come when the user has picked up the bike.
    //Even if user doesn't pick-up the bike the charge for the bike will be considered.
    const val STATUS_BIKE_PICKED_UP = 2

    //This status will come when the user has dropped off the bike.
    const val STATUS_BIKE_DROP_OFF = 3
    //endregion

    //region Wallet transaction type
    const val TRANSACTION_MONEY_ADD = "1"
    const val TRANSACTION_BIKE_BOOKING = "3"
    const val TRANSACTION_SUBSCRIPTION_PURCHASE = "4"
    //endregion
}