package com.piapps.flashcardpro.features.study.adapter

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kent.layouts.textColorResource
import com.piapps.flashcardpro.R
import com.piapps.flashcardpro.core.db.tables.CardDb
import com.piapps.flashcardpro.core.extension.color
import com.piapps.flashcardpro.core.extension.getLocalizedString
import com.piapps.flashcardpro.core.extension.load
import com.piapps.flashcardpro.core.extension.toColor
import com.piapps.flashcardpro.core.platform.LONG_ANIMATION
import com.piapps.flashcardpro.core.platform.component.FlipAnimation

/**
 * Created by abduaziz on 2019-10-30 at 22:56.
 */

class CardsAdapter : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    val list = arrayListOf<CardDb>()
    var backgroundColor = ""
    var textColor = ""

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(CardUI().createView(p0.context))
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val card = list[pos % list.size]
        holder.bind(card)
    }

    override fun getItemCount(): Int {
        return if (list.size == 0) 0 else Int.MAX_VALUE
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val root: CardView

        val front: FrameLayout
        val tvFront: TextView
        val ivFront: AppCompatImageView

        val back: FrameLayout
        val tvBack: TextView
        val ivBack: AppCompatImageView

        init {
            root = itemView.findViewById(CardUI.rootId)

            front = itemView.findViewById(CardUI.frontId)
            tvFront = itemView.findViewById(CardUI.frontTvId)
            ivFront = itemView.findViewById(CardUI.frontIvId)

            back = itemView.findViewById(CardUI.backId)
            tvBack = itemView.findViewById(CardUI.backTvId)
            ivBack = itemView.findViewById(CardUI.backIvId)

            front.setOnClickListener(this)
            back.setOnClickListener(this)
        }

        fun bind(card: CardDb) {
            front.visibility = View.VISIBLE
            back.visibility = View.GONE

            card.order = adapterPosition
            // set texts
            tvFront.text =
                if (card.front.isNotBlank() || card.frontImage.isNotBlank()) card.front else
                    itemView.context.getLocalizedString(R.string.text)
            tvBack.text =
                if (card.back.isNotBlank() || card.backImage.isNotBlank()) card.back else
                    itemView.context.getLocalizedString(R.string.text)

            // set background colors
            val backColor = backgroundColor.toColor()
            front.setBackgroundColor(backColor)
            back.setBackgroundColor(backColor)

            // set text colors
            val txtColor = textColor.toColor()
            tvFront.setTextColor(txtColor)
            tvBack.setTextColor(txtColor)

            // set images
            if (card.frontImage.isNotBlank())
                ivFront.load(card.frontImage)
            else
                ivFront.load("")
            if (card.backImage.isNotBlank())
                ivBack.load(card.backImage)
            else
                ivBack.load("")

            // set font size
            tvFront.setTextSize(TypedValue.COMPLEX_UNIT_SP, card.frontTextSize ?: 28F)
            tvBack.setTextSize(TypedValue.COMPLEX_UNIT_SP, card.backTextSize ?: 28F)
        }

        override fun onClick(v: View?) {
            flip()
        }

        fun flip() {
            val anim = FlipAnimation(front, back, LONG_ANIMATION.toInt())
            if (back.visibility == View.VISIBLE) anim.reverse()
            root.startAnimation(anim)
        }
    }


}