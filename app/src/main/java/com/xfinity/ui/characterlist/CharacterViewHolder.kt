package com.xfinity.ui.characterlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import com.xfinity.data.entities.CharacterEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_character.*

class CharacterViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(character: CharacterEntity, picasso: Picasso) {
        character_name.text = character.name
        picasso.load(character.pictureUrl)
                .centerCrop()
                .resize(100,100)
                .into(character_picture)

    }
}