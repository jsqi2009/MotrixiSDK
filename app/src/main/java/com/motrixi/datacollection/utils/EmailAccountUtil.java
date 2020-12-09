package com.motrixi.datacollection.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.adobe.fre.FREContext;

/**
 * author : Jason
 * date   : 2020/12/8 1:15 PM
 * desc   :
 */
public class EmailAccountUtil {

    public static Account[] getEmailAccount(FREContext context) {

        try {
            Account[] accounts = AccountManager.get(context.getActivity()).getAccounts();

            return accounts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
