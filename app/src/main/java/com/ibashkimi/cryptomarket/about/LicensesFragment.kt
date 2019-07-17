package com.ibashkimi.cryptomarket.about

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibashkimi.cryptomarket.R
import java.security.InvalidParameterException

class LicensesFragment : Fragment() {

    private val intro: Int = R.string.about_lib_intro

    private val libraries: Array<Library> = arrayOf(
            Library(R.string.android_jetpack_name,
                    R.string.android_jetpack_website,
                    R.string.apache_v2),
            Library(R.string.kotlin_name,
                    R.string.kotlin_website,
                    R.string.apache_v2),
            Library(R.string.glide_name,
                    R.string.glide_website,
                    R.string.glide_license),
            Library(R.string.material_components_name,
                    R.string.material_components_website,
                    R.string.apache_v2),
            Library(R.string.moshi_name,
                    R.string.moshi_website,
                    R.string.apache_v2),
            Library(R.string.MPAndroidChart_name,
                    R.string.MPAndroidChart_website,
                    R.string.apache_v2),
            Library(R.string.retrofit_name,
                    R.string.retrofit_website,
                    R.string.apache_v2))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_licenses, container, false)

        root.findViewById<Toolbar>(R.id.toolbar).apply {
            setTitle(R.string.title_licenses)
            setNavigationIcon(R.drawable.ic_back_nav)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = LibraryAdapter(this.libraries)

        return root
    }


    private inner class LibraryAdapter(val libs: Array<Library>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when (viewType) {
                VIEW_TYPE_INTRO -> return LibraryIntroHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.about_lib_intro, parent, false))
                VIEW_TYPE_LIBRARY -> return createLibraryHolder(parent)
            }
            throw InvalidParameterException()
        }

        private fun createLibraryHolder(parent: ViewGroup): LibraryHolder {
            return LibraryHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.about_library, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (getItemViewType(position) == VIEW_TYPE_LIBRARY) {
                bindLibrary(holder as LibraryHolder, libs[position - 1])
            } else {
                (holder as LibraryIntroHolder).intro.setText(intro)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) VIEW_TYPE_INTRO else VIEW_TYPE_LIBRARY
        }

        override fun getItemCount(): Int {
            return libs.size + 1 // + 1 for the static intro view
        }

        private fun bindLibrary(holder: LibraryHolder, lib: Library) {
            holder.name.setText(lib.name)
            holder.website.setText(lib.website)
            holder.licence.setText(lib.license)

            val clickListener: View.OnClickListener = View.OnClickListener {
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    CustomTabsIntent.Builder().build().launchUrl(requireContext(),
                            Uri.parse(getString(lib.website)))
                }
            }
            holder.itemView.setOnClickListener(clickListener)
        }
    }

    private class LibraryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.library_name)
        var website: TextView = itemView.findViewById(R.id.library_link)
        var licence: TextView = itemView.findViewById(R.id.library_license)
    }

    private class LibraryIntroHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var intro: TextView = itemView as TextView
    }

    /**
     * Models an open source library we want to credit
     */
    data class Library(@StringRes val name: Int,
                       @StringRes val website: Int,
                       @StringRes val license: Int)

    companion object {

        private const val VIEW_TYPE_INTRO = 0
        private const val VIEW_TYPE_LIBRARY = 1
    }
}