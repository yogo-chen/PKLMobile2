package com.cendra.prayogo.pklmobile2.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.cendra.prayogo.pklmobile2.model.Product;
import com.cendra.prayogo.pklmobile2.model.Transaction;
import com.cendra.prayogo.pklmobile2.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PklServiceHelper {
    public static final int ERROR_UNAUTHORIZED = 401;
    public static final int ERROR_NOT_FOUND = 404;
    public static final int ERROR_REQUEST_TIMEOUT = 408;
    public static final int ERROR_SERVICE_UNAVAILABLE = 503;

    private static final String PREF_NAME = "PKL_ACCOUNT";
    private static final String SID = "SID";
    private static final String EMAIL = "EMAIL";
    private static final String BIRTHDAY = "BIRTHDAY";
    private static final String NAME = "NAME";
    private static final String ADDRESS = "ADDRESS";
    private static final String PHONE = "PHONE";
    private static final String FEATURED_PRODUCT = "FEATURED_PRODUCT";

    private PklServiceHelper() {
    }

    public static void register(final OnEventListener onEventListener, final User user) {
        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return PklWsdlUtils.registerPkl(user.email, user.birthday, user.name, user.address, user.phone, user.featuredProduct);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals(PklWsdlUtils.SERVER_ERROR)) {
                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                } else if (s.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                } else if (s.equals(PklWsdlUtils.PARSE_ERROR)) {
                    // ignored since impossible
                } else if (s.equals("(\"sukses\",\"" + user.email + "\",\"didaftarkan\")")) {
                    onEventListener.onResultSuccess(null);
                } else {
                    // should be unreachable
                    onEventListener.onResultFailed(0);
                }
            }
        };
        asyncTask.execute();
    }

    public static void login(final Context context, final OnEventListener onEventListener, final String email, final String birthday) {
        AsyncTask<Void, Void, String> asyncTaskLogin = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return PklWsdlUtils.login(email, birthday);
            }

            @Override
            protected void onPostExecute(String loginResultString) {
                if (loginResultString.equals(PklWsdlUtils.SERVER_ERROR)) {
                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                } else if (loginResultString.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                } else if (loginResultString.equals(PklWsdlUtils.PARSE_ERROR)) {
                    // ignored since impossible
                } else {
                    Pattern p = Pattern.compile("^\\(\"OK\",\"(.+)\"\\)$");
                    Matcher m = p.matcher(loginResultString);
                    if (m.find()) {
                        // login approved
                        // get returned sid
                        final String sid = m.group(1);

                        // get user detail
                        AsyncTask<Void, Void, String> asyncTaskGetPkl = new AsyncTask<Void, Void, String>() {

                            @Override
                            protected String doInBackground(Void... params) {
                                return PklWsdlUtils.getPkl(sid);
                            }

                            @Override
                            protected void onPostExecute(String getPklResultString) {
                                if (getPklResultString.equals(PklWsdlUtils.SERVER_ERROR)) {
                                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                                } else if (getPklResultString.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                                } else if (getPklResultString.equals(PklWsdlUtils.PARSE_ERROR)) {
                                    // ignored since impossible
                                } else {
                                    Pattern p = Pattern.compile("^\\(\"(.+)\",\"(.+)\",\"(.+)\",\"(.+)\",\"(.+)\",\"(.+)\"\\)$");
                                    Matcher m = p.matcher(getPklResultString);
                                    if (m.find()) {
                                        String userEmail = m.group(1);
                                        String userName = m.group(2);
                                        String userAddress = m.group(3);
                                        String userPhone = m.group(4);
                                        String userBirthday = m.group(5);
                                        String userFeaturedProduct = m.group(6);

                                        // store sid and user detail
                                        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor ed = pref.edit();
                                        ed.putString(SID, sid);
                                        ed.putString(EMAIL, userEmail);
                                        ed.putString(BIRTHDAY, userBirthday);
                                        ed.putString(NAME, userName);
                                        ed.putString(ADDRESS, userAddress);
                                        ed.putString(PHONE, userPhone);
                                        ed.putString(FEATURED_PRODUCT, userFeaturedProduct);
                                        ed.apply();

                                        onEventListener.onResultSuccess(null);
                                    } else {
                                        onEventListener.onResultFailed(ERROR_UNAUTHORIZED);
                                    }
                                }
                            }
                        };
                        asyncTaskGetPkl.execute();
                    } else {
                        onEventListener.onResultFailed(ERROR_UNAUTHORIZED);
                    }
                }
            }
        };
        asyncTaskLogin.execute();
    }

    public static void logout(Context context) {
        final SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final String sid = pref.getString(SID, "");

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                PklWsdlUtils.logout(pref.getString(sid, ""));
                return null;
            }
        };
        asyncTask.execute();

        SharedPreferences.Editor ed = pref.edit();
        ed.remove(SID);
        ed.remove(EMAIL);
        ed.remove(BIRTHDAY);
        ed.remove(NAME);
        ed.remove(ADDRESS);
        ed.remove(PHONE);
        ed.remove(FEATURED_PRODUCT);
        ed.apply();
    }

    public static void registerProduct(final Context context, final OnEventListener onEventListener, final Product product) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final String sid = pref.getString(SID, "");

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return PklWsdlUtils.registerProduct(sid, product.name, product.basePrice, product.sellPrice);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals(PklWsdlUtils.SERVER_ERROR)) {
                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                } else if (s.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                } else if (s.equals(PklWsdlUtils.PARSE_ERROR)) {
                    // ignored since impossible
                } else if (s.equals("(\"sid=" + sid + "\",\"tidak ditemukan\")")) {
                    onEventListener.onResultFailed(ERROR_UNAUTHORIZED);
                } else if (s.equals("(\"" + product.name + "\",\"diregistrasi\")")) {
                    onEventListener.onResultSuccess(null);
                } else {
                    // unreachable
                    onEventListener.onResultFailed(0);
                }
            }
        };
        asyncTask.execute();
    }

    public static void getProduct(final Context context, final OnEventListener onEventListener, final String name) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final String sid = pref.getString(SID, "");

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return PklWsdlUtils.getProduct(sid, name);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals(PklWsdlUtils.SERVER_ERROR)) {
                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                } else if (s.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                } else if (s.equals(PklWsdlUtils.PARSE_ERROR)) {
                    // ignored since impossible
                } else if (s.equals("(\"namaproduk=" + name + "\",\"tidak ditemukan\")")) {
                    onEventListener.onResultFailed(ERROR_NOT_FOUND);
                } else if (s.equals("(\"sid=" + sid + "\",\"tidak ditemukan\")")) {
                    onEventListener.onResultFailed(ERROR_UNAUTHORIZED);
                } else {
                    Pattern p = Pattern.compile("^\\(\"(.+)\",\"(.+)\",\"(.+)\"\\)$");
                    Matcher m = p.matcher(s);
                    if (m.find()) {
                        String productName = m.group(1);
                        int productBasePrice = Integer.parseInt(m.group(2));
                        int productSellPrice = Integer.parseInt(m.group(3));

                        Product product = new Product(productName, productBasePrice, productSellPrice);

                        onEventListener.onResultSuccess(product);
                    } else {
                        // unreachable
                        onEventListener.onResultFailed(0);
                    }
                }
            }
        };
        asyncTask.execute();
    }

    public static void deleteProduct(final Context context, final OnEventListener onEventListener, final String name) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final String sid = pref.getString(SID, "");

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return PklWsdlUtils.deleteProduct(sid, name);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals(PklWsdlUtils.SERVER_ERROR)) {
                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                } else if (s.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                } else if (s.equals(PklWsdlUtils.PARSE_ERROR)) {
                    // ignored since impossible
                } else if (s.equals("(\"namaproduk=" + name + "\",\"tidak ditemukan\")")) {
                    onEventListener.onResultFailed(ERROR_NOT_FOUND);
                } else if (s.equals("(\"sid=" + sid + "\",\"tidak ditemukan\")")) {
                    onEventListener.onResultFailed(ERROR_UNAUTHORIZED);
                } else if (s.equals("(\"" + name + "\",\"dihapus\")")) {
                    onEventListener.onResultSuccess(null);
                } else {
                    // unreachable
                    onEventListener.onResultFailed(0);
                }
            }
        };
        asyncTask.execute();
    }

    public static void getCatalog(final Context context, final OnEventListener onEventListener) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final String sid = pref.getString(SID, "");

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return PklWsdlUtils.getCatalog(sid);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals(PklWsdlUtils.SERVER_ERROR)) {
                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                } else if (s.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                } else if (s.equals(PklWsdlUtils.PARSE_ERROR)) {
                    // ignored since impossible
                } else if (s.equals("(\"sid=" + sid + "\",\"tidak ditemukan\")")) {
                    onEventListener.onResultFailed(ERROR_UNAUTHORIZED);
                } else {
                    s = s.replace("(", "").replace(")", "").replace("\"", ""); // remove brackets and quotes
                    if (s.equals("")) {
                        onEventListener.onResultSuccess(null);
                    } else {
                        String[] productNames = s.split(",");
                        onEventListener.onResultSuccess(productNames);
                    }
                }
            }
        };
        asyncTask.execute();
    }

    public static void registerTransaction(final Context context, final OnEventListener onEventListener, final Product product, final int quantity, final String date) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final String sid = pref.getString(SID, "");

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return PklWsdlUtils.registerTransaction(sid, product.name, product.sellPrice, quantity, date);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals(PklWsdlUtils.SERVER_ERROR)) {
                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                } else if (s.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                } else if (s.equals(PklWsdlUtils.PARSE_ERROR)) {
                    // ignored since impossible
                } else if (s.equals("(\"sid=" + sid + "\",\"tidak ditemukan\")")) {
                    onEventListener.onResultFailed(ERROR_UNAUTHORIZED);
                } else if (s.equals("(\"Transaksi = " + product.name + "\",\"ditambahkan\")")) {
                    onEventListener.onResultSuccess(null);
                } else {
                    // unreachable
                    onEventListener.onResultFailed(0);
                }
            }
        };
        asyncTask.execute();
    }

    public static void getTransaction(final Context context, final OnEventListener onEventListener, final String date) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final String sid = pref.getString(SID, "");

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return PklWsdlUtils.getTransaction(sid, date);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals(PklWsdlUtils.SERVER_ERROR)) {
                    onEventListener.onResultFailed(ERROR_SERVICE_UNAVAILABLE);
                } else if (s.equals(PklWsdlUtils.CONNECTION_ERROR)) {
                    onEventListener.onResultFailed(ERROR_REQUEST_TIMEOUT);
                } else if (s.equals(PklWsdlUtils.PARSE_ERROR)) {
                    // ignored since impossible
                } else if (s.equals("(\"sid=" + sid + "\",\"tidak ditemukan\")")) {
                    onEventListener.onResultFailed(ERROR_UNAUTHORIZED);
                } else {
                    if (s.equals("(\"\")")) {
                        onEventListener.onResultSuccess(null);
                    } else {
                        String[] productStrings = s.substring(1, s.length() - 1).split("\\),\\(");
                        int transactionLength = productStrings.length;
                        Transaction[] transactions = new Transaction[transactionLength];
                        for (int i = 0; i < transactionLength; ++i) {
                            String[] productProperties = productStrings[i].split(",");
                            String name = productProperties[0].substring(1, productProperties[0].length() - 1);
                            int soldPrice = Integer.parseInt(productProperties[1].substring(1, productProperties[1].length() - 1));
                            int quantity = Integer.parseInt(productProperties[2].substring(1, productProperties[2].length() - 1));
                            String date = productProperties[3].substring(1, productProperties[3].length() - 1);

                            transactions[i] = new Transaction(name, soldPrice, quantity, date);
                        }
                        onEventListener.onResultSuccess(transactions);
                    }
                }
            }
        };
        asyncTask.execute();
    }

    public static User getUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String sid = pref.getString(SID, "");
        if (sid.equals("")) {
            return null;
        } else {
            String email = pref.getString(EMAIL, "");
            String birthday = pref.getString(BIRTHDAY, "");
            String name = pref.getString(NAME, "");
            String address = pref.getString(ADDRESS, "");
            String phone = pref.getString(PHONE, "");
            String featuredProduct = pref.getString(FEATURED_PRODUCT, "");
            return new User(sid, email, birthday, name, address, phone, featuredProduct);
        }
    }

    public interface OnEventListener {
        void onResultSuccess(Object result);

        void onResultFailed(int statusCode);
    }
}
