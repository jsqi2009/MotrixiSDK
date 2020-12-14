package com.motrixi.datacollection.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context


/**
 * author : Jason
 *  date   : 2020/10/23 11:45 AM
 *  desc   :
 */
object EmailAccountUtil {

    fun getEmailAccount(context: Context): Array<Account> {

        try {
            val accounts = AccountManager.get(context).accounts


            return accounts
        } catch (e: Exception) {
            return arrayOf()
        }
    }
}