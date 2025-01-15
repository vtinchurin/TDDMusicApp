package com.ru.androidexperts.muzicapp.di

import android.widget.ImageView
import com.ru.androidexperts.muzicapp.R
import com.squareup.picasso.Picasso

interface PicEngine {

    fun loadImage(view: ImageView, url: String)

    object Base : PicEngine {
        override fun loadImage(view: ImageView, url: String) {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_artwork)
                .error(R.drawable.ic_artwork)
                .into(view)

        }
    }

    object Test : PicEngine {
        override fun loadImage(view: ImageView, url: String) {
            view.setImageResource(R.drawable.ic_artwork)
        }
    }
}