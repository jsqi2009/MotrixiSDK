package com.motrixi.datacollection

import android.content.Intent
import android.widget.Toast
import com.adobe.fre.*
import com.motrixi.datacollection.feature.SystemInfoActivity
import com.motrixi.datacollection.utils.MotrixiSDK


/**
 * author : Jason
 *  date   : 2020/12/4 1:36 PM
 *  desc   :
 */
class MotrixiSDKFunction : FREFunction {

    override fun call(mContext: FREContext?, objectAry: Array<out FREObject>?): FREObject {

        var result: FREObject? = null
        try {
            val msg: String = objectAry!![0].asString


            var context = mContext as NativeContext

            //MotrixiSDK.init(context!!, msg)

            val intent: Intent = Intent(context.activity, SystemInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.activity.startActivity(intent)

            // 返回值
            result = FREObject.newObject(msg)
        } catch (e: IllegalStateException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: FRETypeMismatchException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: FREInvalidObjectException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: FREWrongThreadException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }


        return result!!
    }
}