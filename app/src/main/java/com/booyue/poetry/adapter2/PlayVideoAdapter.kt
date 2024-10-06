package com.booyue.poetry.adapter2

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.booyue.net.image.glide.GlideImageLoader
import com.booyue.poetry.R
import com.booyue.poetry.bean.VideoVo
import com.booyue.poetry.constant.ItemType
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruffian.library.widget.RImageView

class PlayVideoAdapter : BaseQuickAdapter<VideoVo, BaseViewHolder>(R.layout.item_main_video) {

    override fun convert(holder: BaseViewHolder, item: VideoVo) {
        when (item.type) {

            ItemType.TYPE_VIDEO_SUB -> {
                holder.setText(R.id.subTv, "《" + item.name + "》")
            }

            ItemType.TYPE_VIDEO_VIDEO -> {
                val bgIv = holder.getView<RImageView>(R.id.videoIv)
                val ivDownloadControl = holder.getView<ImageView>(R.id.downloadIv)
                val pbProgress = holder.getView<ProgressBar>(R.id.progressBar)
                GlideImageLoader().loadImage(mContext, item.image, bgIv)
                holder.setText(R.id.videoNameTv, item.name)
                pbProgress.visibility = View.INVISIBLE
                ivDownloadControl.visibility = View.INVISIBLE
            }
        }
    }

    override fun getDefItemViewType(position: Int): Int {
        return data[position].type
    }

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        var layout = R.layout.item_main_video
        if (viewType == ItemType.TYPE_VIDEO_SUB) {
            layout = R.layout.item_com_video_sub
        }
        return createBaseViewHolder(parent, layout)
    }
}