package com.marcinorlowski.fonty.demo

import android.app.Application
import com.marcinorlowski.fonty.Fonty.Companion.context

/**
 ******************************************************************************
 *
 * Copyright 2013-2020 Marcin Orlowski <github@MarcinOrlowski.com>
 *
 * Licensed under the Apache License 2.0
 *
 ******************************************************************************
 */
class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        val files = this.filesDir.absolutePath
        context(this)
              .fontDir(files)
//              .typefaceFallback(false)
                .italicTypeface("ko_three.ttf")
                .normalTypeface("ko_three.ttf")
                .boldTypeface("ko_three.ttf")
                .build()
    }
}
