package com.meizu.jarutil;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by libinhui on 2016/5/9.
 */
public class DataToy {
    private static ContentResolver cr;
    private static Cursor cursor;

    /**
     * 获取短信/彩信当前发送/接收状态
     */
    public static String getMessageStatus(Context cn, String uri, String phonenum, String smstext) {
        cr = cn.getContentResolver();
        cursor = cr.query(Uri.parse(uri), null, null, null, null);
        while (cursor.moveToNext()) {
            int phoneColumn = cursor.getColumnIndex("address");
            int infoColumn = cursor.getColumnIndex("body");
            String num = cursor.getString(phoneColumn);
            String smsText = cursor.getString(infoColumn);
            if (num.contains(phonenum) && smsText.equals(smstext)) {
                int typeColumn = cursor.getColumnIndex("type");
                return cursor.getString(typeColumn);
            }
        }
        return null;
    }

    /**
     * 新增一个联系人
     */
    public static void insertOneContact(Context cn, String DisplayName, String MobileNumber, String EmailAddr) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        if (EmailAddr != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, EmailAddr)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            cn.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(cn, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除所有联系人
     * */
    public static void deleteAllContact(Context cn) {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        cn.getContentResolver().delete(uri, "_id!=-1", null);
    }

    /**
     * 删除所有通话记录
     */
    public static void deleteAllCallLog(Context cn) {
        ContentResolver resolver = cn.getContentResolver();
        resolver.delete(CallLog.Calls.CONTENT_URI, "_id!=-1", null);
    }

    /**
     * 新增一条日历事件
     */
    public static void addOneCalendarEvent(Context cn){
        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        String currentTime = GetInfoUtils.getCurrentTime();
        int eventyear = Integer.valueOf(currentTime.split(":")[0]);
        int eventmonth = Integer.valueOf(currentTime.split(":")[1]);
        int eventday = Integer.valueOf(currentTime.split(":")[2]);
        int eventhour = Integer.valueOf(currentTime.split(":")[3]);
        beginTime.set(eventyear, eventmonth-1, eventday, eventhour, 00);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(eventyear, eventmonth-1, eventday, eventhour+1, 00);
        endMillis = endTime.getTimeInMillis();
        ContentResolver cr = cn.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "testSucess");
        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }

    /**
     * 删除所有日历事件
     */
    public static void deleteAllCalendarEvents(Context cn) {
        ContentResolver resolver = cn.getContentResolver();
        resolver.delete(CalendarContract.Events.CONTENT_URI, "_id!=-1", null);
    }

}
