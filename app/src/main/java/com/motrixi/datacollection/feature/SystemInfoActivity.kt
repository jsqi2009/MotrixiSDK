package com.motrixi.datacollection.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.motrixi.datacollection.R
import com.motrixi.datacollection.utils.SystemInfoUtils
import kotlinx.android.synthetic.main.activity_main.*


class SystemInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_system_info)

        initView()
    }

    private fun initView() {
        tv_system_info.text = SystemInfoUtils.getDeviceAllInfo(this)
    }
}