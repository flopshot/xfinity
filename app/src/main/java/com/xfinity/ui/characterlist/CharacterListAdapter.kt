package com.xfinity.ui.characterlist

import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.xfinity.R
import com.xfinity.data.entities.CharacterEntity
import com.xfinity.ui.util.AsyncDiffUtilAdapter
import javax.inject.Inject


class CharacterListAdapter @Inject constructor(private val picasso: Picasso)
    : AsyncDiffUtilAdapter<CharacterViewHolder, CharacterEntity>() {

    private var characterList: MutableList<CharacterEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder
            = CharacterViewHolder (
                    LayoutInflater
                            .from(parent.context)
                            .inflate(R.layout.row_character, parent, false)
            )

    override fun getItemCount(): Int {
        return characterList.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int)
            = holder.bind(characterList[position], picasso)

    override fun itemsDiffUtilCallback(oldItems: List<CharacterEntity>, newItems: List<CharacterEntity>): DiffUtil.Callback {

        return object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldItems.size

            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = oldItems[oldItemPosition - 1].description == newItems[newItemPosition - 1].description

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = oldItems[oldItemPosition].name == newItems[newItemPosition].name
                    && oldItems[oldItemPosition].pictureUrl == newItems[newItemPosition].pictureUrl
                    && oldItems[oldItemPosition].isFavorite == newItems[newItemPosition].isFavorite
        }
    }

    fun setCharacterList(newCharacterList: MutableList<CharacterEntity>) {
        if (characterList.size == 0) {
            characterList = newCharacterList
            notifyItemRangeInserted(0, newCharacterList.size - 1)
        } else {
            this.updateItems(newCharacterList, characterList)
        }
    }
}