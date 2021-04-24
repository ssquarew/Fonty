package com.marcinorlowski.fonty

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.util.DisplayMetrics
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

/**
 ******************************************************************************
 *
 * Copyright 2013-2020 Marcin Orlowski <github@MarcinOrlowski.com>
 *
 * Licensed under the Apache License 2.0
 *
 ******************************************************************************
 */
class Fonty {

    /**
     * Application context
     */
    private var mContext: Context? = null

    /**
     * Prevents instantiation with new operator. Use context() instead
     */
    private constructor() {}

    /**
     * Instantiates a new FontTools object
     *
     * @param context the context
     */
    private constructor(context: Context) {
        mContext = context
    }

    // --------------------------------------------------------------------------------------------

    fun fontDir(fontDir: String): Fonty {

        this.fontDirTmp = fontDir
        return this
    }

    private var fontDirTmp: String? = null

    private fun setFontDir(fontDir: String?) {
        if (fontDir == null) {
            return
        }

        var dir = fontDir

        if (dir != "") {
            if (dir.substring(dir.length - 1) != "/") {
                if (dir != "") {
                    dir = "$dir/"
                }
            }
        }

        fontDirectoryPath = dir
    }


    private var fontDirectoryPath = "fonts/"


    // --------------------------------------------------------------------------------------------

    private var boldTypefaceNameTmp: String? = null

    /**
     * Set typeface to be used for BOLD
     *
     * @param fileName the file name of TTF asset file name
     *
     * @return instance of Fonty object for easy chaining
     */
    fun boldTypeface(fileName: String): Fonty {

        boldTypefaceNameTmp = fileName
        return this
    }

    /**
     * Set typeface to be used for BOLD
     *
     * @param fileNameId string resource id that holds TTF asset file name
     *
     * @return instance of Fonty object for easy chaining
     */
    fun boldTypeface(fileNameId: Int): Fonty {
        failIfConfigured()

        boldTypefaceNameTmp = null
        return this
    }

    // --------------------------------------------------------------------------------------------

    private var normalTypefaceNameTmp: String? = null

    /**
     * Set typeface to be used for NORMAL style
     *
     * @param fileName the file name of TTF asset file name
     *
     * @return instance of Fonty object for easy chaining
     */
    fun normalTypeface(fileName: String): Fonty {

        normalTypefaceNameTmp = fileName
        return this
    }

    /**
     * Set typeface to be used for NORMAL
     *
     * @param fileNameId string resource id that holds TTF asset file name
     *
     * @return instance of Fonty object for easy chaining
     */
    fun normalTypeface(fileNameId: Int): Fonty {
        failIfConfigured()

        normalTypefaceNameTmp = null
        return this
    }

    // --------------------------------------------------------------------------------------------

    private var italicTypefaceNameTmp: String? = null

    /**
     * Set typeface to be used for ITALIC style
     *
     * @param fileName string resource id that holds TTF asset file name
     *
     * @return instance of Fonty object for easy chaining
     */
    fun italicTypeface(fileName: String): Fonty {

        italicTypefaceNameTmp = fileName
        return this
    }

    /**
     * Set typeface to be used for ITALIC style
     *
     * @param fileNameId string resource id that holds TTF asset file name
     *
     * @return instance of Fonty object for easy chaining
     */
    fun italicTypeface(fileNameId: Int): Fonty {
        failIfConfigured()

        italicTypefaceNameTmp = null
        return this
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Add typeface to cache. Throws RuntimeException if Fonty's context is not set up.
     *
     * @param alias      the typeface alias
     * @param fileNameId string resource id that holds TTF asset file name
     *
     * @return instance of Fonty object for easy chaining
     */
    private fun addFromAsset(alias: String, @StringRes fileNameId: Int): Fonty {
        return addFromAsset(alias, mContext!!.resources.getString(fileNameId))
    }

    /**
     * Add typeface to cache. Throws RuntimeException if Fonty's context is not set up.
     *
     * @param aliasId    the typeface alias string resource Id
     * @param fileNameId string resource id that holds TTF asset file name
     *
     * @return instance of Fonty object for easy chaining
     */
    private fun addFromAsset(@StringRes aliasId: Int, @StringRes fileNameId: Int): Fonty {
        val res = mContext!!.resources
        return addFromFilePath(res.getString(aliasId), res.getString(fileNameId))
    }

    /**
     * Add typeface to Fonty's cache. Throws RuntimeException if Fonty's context is not set up,
     *
     * @param alias        the typeface alias
     * @param fontFileName the file name of TTF asset. Can contain folder names too (i.e. "fnt/foo.ttf"). It will be
     * automatically "glued" with font folder name (see @fontDir()). If you do not want this to happen
     * add "/" to fontFileName, i.e. "/foo.ttf" or "/foo/other-folder/foo.ttf". In such case
     * fontDir is not used.
     */
    private fun addFromAsset(alias: String, fontFileName: String): Fonty {
        when {
            alias.isEmpty() -> throw RuntimeException("Typeface alias cannot be empty string")
            fontFileName.isEmpty() -> throw RuntimeException("Typeface filename cannot be empty string")
        }

        var fontFile = fontFileName

        // strip leading "/" if present
        fontFile = if (fontFile.substring(0, 1) == "/") {
            fontFile.substring(1)
        } else {
            fontDirectoryPath + fontFile
        }

        Cache.instance.addFromAsset(mContext!!, alias, fontFile)

        return this
    }

    /**
     * Add typeface to Fonty's cache. Throws RuntimeException if Fonty's context is not set up,
     *
     * @param alias        the typeface alias
     * @param fontFilePath the file name of TTF asset. Can contain folder names too (i.e. "fnt/foo.ttf"). It will be
     * automatically "glued" with font folder name (see @fontDir()). If you do not want this to happen
     * add "/" to fontFileName, i.e. "/foo.ttf" or "/foo/other-folder/foo.ttf". In such case
     * fontDir is not used.
     */
    private fun addFromFilePath(alias: String, fontFilePath: String): Fonty {
        when {
            alias.isEmpty() -> throw RuntimeException("Typeface alias cannot be empty string")
            fontFilePath.isEmpty() -> throw RuntimeException("Typeface filename cannot be empty string")
        }

        var fontFile = fontFilePath

        // strip leading "/" if present
        fontFile = if (fontFile.substring(0, 1) == "/") {
            fontFile.substring(1)
        } else {
            fontDirectoryPath + fontFile
        }

        Cache.instance.addFromFilePath(mContext!!, alias, fontFile)

        return this
    }

    private fun clearFontCaches():Fonty{
        Cache.instance.clearCache()
        return this
    }

    /**
     * Controls typeface fallback mechanism. When widget requires BOLD or ITALIC font and such
     * typeface is not configured, then: when this option is set to @false RuntimeException
     * will be thrown due to missing typeface. If is set to @true (default), then error will be
     * logged indicating what typeface is missing and what is id and class of widget requesting
     * it. Then Fonty will fall back to NORMAL typeface.
     *
     * @param mode @true (default) to enable font substitution fallback, @false otherwise
     *
     * @return instance of Fonty object for easy chaining
     */
    fun typefaceFallback(mode: Boolean): Fonty? {
        failIfConfigured()

        fallback = mode
        return this
    }


    // --------------------------------------------------------------------------------------------

    /**
     * Concludes configuration phase. Must be called as last method of Fonty config call chain
     */
    fun build() {
        failIfConfigured()

        setFontDir(fontDirTmp)

        if (normalTypefaceNameTmp != null) {
            addFromFilePath(TYPE_NORMAL, normalTypefaceNameTmp!!)
        }

        if (boldTypefaceNameTmp != null) {
            addFromFilePath(TYPE_BOLD, boldTypefaceNameTmp!!)
        }

        if (italicTypefaceNameTmp != null) {
            addFromFilePath(TYPE_ITALIC, italicTypefaceNameTmp!!)
        }


        alreadyConfigured = true
    }

    /**
     * Concludes configuration phase. Must be called as last method of Fonty config call chain
     */
    fun refresh() {
        clearFontCaches()

        setFontDir(fontDirTmp)

        if (normalTypefaceNameTmp != null) {
            addFromFilePath(TYPE_NORMAL, normalTypefaceNameTmp!!)
        }

        if (boldTypefaceNameTmp != null) {
            addFromFilePath(TYPE_BOLD, boldTypefaceNameTmp!!)
        }

        if (italicTypefaceNameTmp != null) {
            addFromFilePath(TYPE_ITALIC, italicTypefaceNameTmp!!)
        }

        alreadyConfigured = true
    }

    // --------------------------------------------------------------------------------------------

    companion object {
        const val LOG_TAG = "Fonty"
        const val TYPE_NORMAL = "normal"
        const val TYPE_BOLD = "bold"
        const val TYPE_ITALIC = "italic"

        // --------------------------------------------------------------------------------------------

        fun initWithContext(context: Context): Fonty {
            if (!Context::class.java.isInstance(context)) {
                throw IllegalArgumentException("Invalid Context object passed.")
            }

            if (instance_ == null) {
                instance_ = Fonty(context)
            }

            return instance_!!
        }

        private var instance_: Fonty? = null
        // --------------------------------------------------------------------------------------------

        /**
         * Init font tools context. Must be called as first thin in setup chain! Throws
         * RuntimeException is context is `null` and IllegalArgumentException if passed
         * object is not a Context subclass.
         *
         * @param context the context to use with Fonty. Preferably application context.
         *
         * @return instance of Fonty object for easy chaining
         */
        @JvmStatic
        fun context(context: Context): Fonty {
            failIfConfigured()

            return initWithContext(context)
        }

        // --------------------------------------------------------------------------------------------

        /**
         * Get typeface.
         *
         * @param alias the alias
         *
         * @return the typeface
         */
        @JvmStatic
        operator fun get(alias: String): Typeface? {
            return Cache.instance[alias]
        }

        // --------------------------------------------------------------------------------------------


        @JvmStatic
        fun isFontyReady():Boolean{
            return alreadyConfigured
        }
        /**
         * Sets custom fonts.
         *
         * @param activity the activity
         */
        @JvmStatic
        fun setFonts(activity: Activity) {
            failIfNotReady()
            setFonts((activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup)
        }

        /**
         * Sets custom fonts.
         *
         * @param viewGroup the view group
         */
        @JvmStatic
        fun setFonts(viewGroup: ViewGroup?) {
            failIfNotReady()
            if (viewGroup != null && viewGroup.context.resources.displayMetrics.densityDpi >= DisplayMetrics.DENSITY_MEDIUM) {
                setFontsRaw(viewGroup)
            }
        }

        // --------------------------------------------------------------------------------------------

        private var fallback = true
        private var alreadyConfigured = false

        // --------------------------------------------------------------------------------------------



        private fun failIfConfigured() {
            if (alreadyConfigured) {
                throw RuntimeException("Already configured. build() must be last invoked method.")
            }
        }

        private fun failIfNotReady() {
            if (!alreadyConfigured) {
                throw RuntimeException("Not configured. You must call build() first.")
            }
        }

        // --------------------------------------------------------------------------------------------

        /**
         * All the font setting work is done here
         *
         * @param viewGroup we alter all known children of this group
         */
        private fun setFontsRaw(viewGroup: ViewGroup) {
            for (i in 0 until viewGroup.childCount) {
                val view = viewGroup.getChildAt(i)

                if (EditText::class.java.isInstance(view)) {
                    val oldTf = (view as EditText).typeface
                    view.typeface = Utils.substituteTypeface(oldTf, fallback, view.javaClass.name, view.getId())

                } else if (TextView::class.java.isInstance(view)) {
                    val oldTf = (view as TextView).typeface
                    view.typeface = Utils.substituteTypeface(oldTf, fallback, view.javaClass.name, view.getId())

                } else if (Button::class.java.isInstance(view)) {
                    val oldTf = (view as Button).typeface
                    view.typeface = Utils.substituteTypeface(oldTf, fallback, view.javaClass.name, view.getId())

                } else if (TabLayout::class.java.isInstance(view)) {
                    val vg = view as ViewGroup
                    val tabsCount = vg.childCount
                    for (j in 0 until tabsCount) {
                        val vgTab = vg.getChildAt(j) as ViewGroup
                        val tabChildsCount = vgTab.childCount
                        for (i in 0 until tabChildsCount) {
                            setFontsRaw(vgTab)
//
//                            val tabViewChild = vgTab.getChildAt(i)
//                            if (tabViewChild is TextView) {
//                                tabViewChild.setTypeface(Font.getInstance().getTypeFace(), Typeface.NORMAL)
//                            }
                        }
                    }
                } else if (NavigationView::class.java.isInstance(view)) {
                    val nv = view as NavigationView
                    // change font in header (if any)
                    val headerCount = nv.headerCount
                    for (headerIndex in 0 until headerCount) {
                        setFontsRaw(nv.getHeaderView(headerIndex) as ViewGroup)
                    }

                    // then modify menu items
                    setFontsMenu(nv.menu)

                } else if (TextInputLayout::class.java.isInstance(view)) {
                    val et = (view as TextInputLayout).editText
                    if (et != null) {
                        val oldTf = et.typeface
                        view.typeface = Utils.substituteTypeface(oldTf, fallback, view.javaClass.name, view.getId())

                        et.typeface = Utils.substituteTypeface(oldTf, fallback, et.javaClass.name, et.id)
                    }

                } else if (ViewGroup::class.java.isInstance(view)) {
                    setFontsRaw(view as ViewGroup)
                }
            }
        }


        /**
         * Sets custom fonts for menu items (and sub-menus too) using custom TypefaceSpan
         *
         * @param menu Menu to work on
         */
        private fun setFontsMenu(menu: Menu) {
            failIfNotReady()

            for (menuIndex in 0 until menu.size()) {
                val menuItem = menu.getItem(menuIndex)
                if (menuItem != null && menuItem.title != null) {
                    val spannableString = SpannableString(menuItem.title)
                    spannableString.setSpan(
                            TypefaceSpan(fallback, menuItem.javaClass.name, menuItem.itemId),
                            0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    menuItem.title = spannableString

                    if (menuItem.hasSubMenu()) {
                        setFontsMenu(menuItem.subMenu)
                    }
                }
            }
        }

    }

    // end of class
}
