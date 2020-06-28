package com.benhan.bluegreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil.hideKeyboard
import org.ocpsoft.prettytime.PrettyTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeRecyclerAdapter(val context: Context, val activity: Activity, var postList: ArrayList<PostData>, val profilePhoto: String ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        const val TYPE_POST = 0
        const val TYPE_LOAD = 1

    }

    var isLoading = false
    var isMoreDataAvailable = true
    var loadMoreListener:OnLoadMoreListener? = null

    val prettyTime = PrettyTime(Locale.KOREA)

    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val sharedPreference = SharedPreference()
    val backgroundColor = ContextCompat.getColor(context, R.color.background)
    val naviColor = ContextCompat.getColor(context, R.color.navi)
    var onWriteCommentClicked: OnWriteCommentClicked? = null
    var onPostClicked: OnPostClicked? = null
    val email = sharedPreference.getString(context, "email")
    val user_name = sharedPreference.getString(context, "name")

    var onPageClickListener: OnPageClickListener? = null
    var onUserClickListener : OnUserClickListener? = null
    var onClickShowAllListener: OnClickShowAllListener? = null
    var onLikeClickListener: OnLikeClickListener? = null









    class MyViewHolder(val layout: View) : RecyclerView.ViewHolder(layout), View.OnClickListener {

        val ivPageProfilePhoto: ImageView = layout.findViewById<ImageView>(R.id.pageProfilePhoto)
        val tvPageName: TextView = layout.findViewById<TextView>(R.id.pageName)
        val tvPageType: TextView = layout.findViewById<TextView>(R.id.type)
        val tvPageProvince = layout.findViewById<TextView>(R.id.province)
        val ivPostImage: ImageView = layout.findViewById(R.id.postImage)
        val ivUserProfilePhoto = layout.findViewById<ImageView>(R.id.userProfilePhoto)
        val tvUserName = layout.findViewById<TextView>(R.id.userName)
        val tvDescription = layout.findViewById<TextView>(R.id.postDescription)
        val tvCountLikes = layout.findViewById<TextView>(R.id.countLikes)
        val tvShowAllComents = layout.findViewById<TextView>(R.id.showAllComents)
        val tvMainComentUserName = layout.findViewById<TextView>(R.id.commentUserName)
        val tvMainCommentContents = layout.findViewById<TextView>(R.id.commentContents)
        val ivMyProfile = layout.findViewById<ImageView>(R.id.myProfilePhoto)
        val tvWriteComment = layout.findViewById<TextView>(R.id.writeComment)
        val tvPostedDate = layout.findViewById<TextView>(R.id.postedDate)
        val uploadComment = layout.findViewById<TextView>(R.id.uploadComment)
        val page = layout.findViewById<RelativeLayout>(R.id.page)
        val compass = layout.findViewById<ImageView>(R.id.compass)
        val likeBtn = layout.findViewById<ImageView>(R.id.likeBtn)
        val commentContainer = layout.findViewById<RelativeLayout>(R.id.mainComentContainer)


        override fun onClick(v: View?) {

        }


    }

    class LoadHolder(view: View): RecyclerView.ViewHolder(view){


    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val treeRecyclerRow = inflater.inflate(R.layout.tree_recycler_row, parent, false)
        val loadLayout = inflater.inflate(R.layout.search_recycler_load, parent, false)



        if(viewType == TYPE_POST)
        {
            return MyViewHolder(treeRecyclerRow)
        } else {
            return LoadHolder(loadLayout)
        }
    }

    override fun getItemViewType(position: Int): Int {

        if(postList[position].kind == "post"){
            return TYPE_POST
        }else{
            return TYPE_LOAD
        }

    }

    override fun getItemCount(): Int {


        return postList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if(position>= itemCount-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null)
        {
            isLoading = true
            loadMoreListener?.onLoadMore()
        }


        if(getItemViewType(position) == TYPE_POST) {
            holder as MyViewHolder
            val item = postList[position]
            var isLikingPost = item?.isLikingPost
            var postLikes = item?.postLikes
            var commentCount = item?.commentNumber
            val profileUrl = MyApplication.severUrl+profilePhoto
            val pageProfileUrl = MyApplication.severUrl+item?.pageProfilePhoto
            val postImageUrl = MyApplication.severUrl+item?.postImage
            val userProfilePhotoUrl = MyApplication.severUrl+item?.userProfilePhoto
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")




            Glide.with(context).load(profileUrl)
                .centerCrop()
                .into(holder.ivMyProfile)
            Glide.with(context).load(pageProfileUrl)
                .fitCenter()
                .override(600, 200)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPageProfilePhoto)

            Glide.with(context).load(postImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPostImage)

            Glide.with(context).load(userProfilePhotoUrl)
                .thumbnail(0.3f)
                .into(holder.ivUserProfilePhoto)

            holder.tvPageName.text = item.pageName
            holder.tvPageType.text = item.pageType
            holder.tvPageProvince.text = item.pageProvince
            holder.tvUserName.text = item.userName
            holder.tvDescription.text = item.postDescription
            holder.tvMainComentUserName.text = item.mainCommentUserName
            holder.tvMainCommentContents.text = item.mainComment

            if (commentCount!! > 2) {
                holder.tvShowAllComents.text = "댓글 ${commentCount}개 모두 보기"
                holder.tvShowAllComents.visibility = View.VISIBLE
            } else {
                holder.tvShowAllComents.visibility = View.GONE
                holder.tvShowAllComents.text = null
            }
            if (!isLikingPost!!) {
                holder.likeBtn.setImageResource(R.drawable.tree)
            } else {
                holder.likeBtn.setImageResource(R.drawable.tree_selected)
            }
            if (holder.tvMainCommentContents.text.isNullOrBlank()) {
                holder.commentContainer.visibility = View.GONE
            }
            val date = simpleDateFormat.parse(item.postDate!!)
            val ago = prettyTime.format(date)
            holder.tvPostedDate.text = ago
            if(postLikes!! > 0 ){
                holder.tvCountLikes.text = "좋아요 ${item.postLikes}개"
            }else{
                holder.tvCountLikes.visibility = View.GONE
            }







            holder.page.setOnClickListener {
                onPageClickListener?.onPageClick(position)
            }
            holder.compass.setOnClickListener {
                onPageClickListener?.onPageClick(position)
            }


            holder.ivUserProfilePhoto.setOnClickListener{
                onUserClickListener?.onUserClick(position)
            }
            holder.tvUserName.setOnClickListener{
                onUserClickListener?.onUserClick(position)
            }


            holder.tvShowAllComents.setOnClickListener {
                onClickShowAllListener?.onClickShowAll(position)
            }


            holder.likeBtn.setOnClickListener {
                onLikeClickListener?.onLikeClick(position)
            }


            holder.tvWriteComment.setOnClickListener {
                onWriteCommentClicked?.onWriteCommentClicked(position)
            }



        }


    }




    /////////////////////////////////////





    fun notifyDataChanged(){
        notifyDataSetChanged()
        isLoading = false
    }





    interface OnWriteCommentClicked{

        fun onWriteCommentClicked(position: Int)
    }

    fun addWriteCommentClickListener(listener: OnWriteCommentClicked){

        onWriteCommentClicked = listener

    }

    interface OnPostClicked{

        fun onPostClicked(position: Int)

    }

    fun addOnPostClickListener(listener: OnPostClicked){

        onPostClicked = listener


    }

    interface OnLoadMoreListener{
        fun onLoadMore()
    }

    fun addLoadMoreListener(listener: OnLoadMoreListener){

        this.loadMoreListener = listener
    }










    interface OnPageClickListener{
        fun onPageClick(position: Int)
    }
    interface OnUserClickListener{
        fun onUserClick(position: Int)
    }
    interface OnClickShowAllListener{
        fun onClickShowAll(position: Int)
    }
    interface OnLikeClickListener{
        fun onLikeClick(position: Int)
    }
}