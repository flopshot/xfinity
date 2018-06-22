package com.xfinity.ui.characterlist

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import com.xfinity.R
import com.xfinity.data.entities.CharacterEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_character.*

class CharacterViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(character: CharacterEntity, picasso: Picasso, isSelected: Boolean) {
        character_name.text = character.name

        if (character.pictureUrl.trim() == "") {
            character_picture.setImageDrawable(ContextCompat.getDrawable(containerView.context, R.drawable.placeholder))
        } else{
            character_picture.setImageDrawable(null)
            picasso.load(character.pictureUrl)
                    .centerInside()
                    .resize(200,200)
                    .placeholder(R.drawable.placeholder)
                    .into(character_picture)
        }

        if (character.isFavorite) {
            favorite_icon.visibility = View.VISIBLE
        } else {
            favorite_icon.visibility = View.GONE
        }

        if (isSelected) {
            row_container.setBackgroundColor(ContextCompat.getColor(containerView.context, R.color.selected_character_row))
        } else {
            row_container.setBackgroundColor(ContextCompat.getColor(containerView.context, android.R.color.transparent))
        }
    }
}