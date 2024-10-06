package com.booyue.poetry.adapter2

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import com.booyue.base.util.LoggerUtils
import com.booyue.database.download.Conf
import com.booyue.database.greendao.bean.DownloadBean
import com.booyue.net.image.glide.GlideImageLoader
import com.booyue.poetry.MyApp
import com.booyue.poetry.R
import com.booyue.poetry.bean.VideoVo
import com.booyue.poetry.constant.ItemType
import com.booyue.poetry.ui.CompleteVideoActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruffian.library.widget.RImageView

class MainVideoAdapter : BaseQuickAdapter<VideoVo, BaseViewHolder>(R.layout.item_main_video) {

    val spanSize = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(p0: Int): Int {
            return if (getDefItemViewType(p0) == ItemType.TYPE_VIDEO_SUB) {
                4
            } else {
                1
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: VideoVo) {
        when (item.type) {

            ItemType.TYPE_VIDEO_SUB -> {
                holder.setImageResource(R.id.videoSubIv, item.subImg)
            }

            ItemType.TYPE_VIDEO_VIDEO -> {
                val bgIv = holder.getView<RImageView>(R.id.videoIv)
                val ivDownloadControl = holder.getView<ImageView>(R.id.downloadIv)
                val pbProgress = holder.getView<ProgressBar>(R.id.progressBar)
                GlideImageLoader().loadImage(mContext, item.image, bgIv)
                holder.setText(R.id.videoNameTv, item.name)
                LoggerUtils.d("tempTag", "state: ${item.status}")
                pbProgress.visibility = View.INVISIBLE
                ivDownloadControl.visibility = View.VISIBLE
                when (item.status) {
                    0 -> {
                        ivDownloadControl.setImageResource(R.mipmap.ic_main_download)
                    }
                    Conf.STATE_TASK_WAIT, Conf.STATE_TASK_PAUSE -> {
                        ivDownloadControl.setImageResource(R.mipmap.ic_play)
                    }
                    Conf.STATE_TASK_PROCESSING -> {
                        ivDownloadControl.setImageResource(R.mipmap.ic_stop)
                        pbProgress.visibility = View.VISIBLE
                        pbProgress.progress = item.percent
                    }
                    Conf.STATE_TASK_ERROR -> {
                        ivDownloadControl.setImageResource(R.drawable.btn_download_refresh)
                    }
                    Conf.STATE_TASK_COMPLETE -> {
                        ivDownloadControl.visibility = View.INVISIBLE
                        pbProgress.visibility = View.INVISIBLE
                    }
                }
                holder.itemView.setOnClickListener {

                    when (item.status) {
                        0 -> {
                            val downloadBean = DownloadBean()
                            downloadBean.guid = item.id
                            downloadBean.title = item.name
                            downloadBean.url = item.url
                            downloadBean.coverImage = item.image
                            downloadBean.isChack = false
                            downloadBean.groupName = item.groupName
                            downloadBean.groupId = item.groupId.toString()
                            MyApp.getInstance().downloadHelper.insertAndStart(downloadBean)
                            item.status = Conf.STATE_TASK_PROCESSING
                            notifyDataSetChanged()
                        }
                        Conf.STATE_TASK_WAIT, Conf.STATE_TASK_PAUSE, Conf.STATE_TASK_ERROR -> {
                            MyApp.getInstance().downloadHelper.Resume(item.id)
                            item.status = Conf.STATE_TASK_PROCESSING
                            notifyDataSetChanged()
                        }
                        Conf.STATE_TASK_PROCESSING -> {
                            MyApp.getInstance().downloadHelper.Pause(item.id)
                            item.status = Conf.STATE_TASK_PAUSE
                            notifyDataSetChanged()
                        }
                        Conf.STATE_TASK_COMPLETE -> {
                            val intent = Intent(mContext, CompleteVideoActivity::class.java)
                            intent.putExtra("uid", item.id)
                            mContext.startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    override fun getDefItemViewType(position: Int): Int {
        return data[position].type
    }

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        var layout = R.layout.item_main_video
        if (viewType == ItemType.TYPE_VIDEO_SUB) {
            layout = R.layout.item_video_sub
        }
        return createBaseViewHolder(parent, layout)
    }
}