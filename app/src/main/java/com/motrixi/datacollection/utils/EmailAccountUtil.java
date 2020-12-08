package com.motrixi.datacollection.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * author : Jason
 * date   : 2020/12/8 1:15 PM
 * desc   :
 */
public class EmailAccountUtil {

    public static Account[] getEmailAccount(Context context) {

        try {
            Account[] accounts = AccountManager.get(context).getAccounts();

            return accounts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
