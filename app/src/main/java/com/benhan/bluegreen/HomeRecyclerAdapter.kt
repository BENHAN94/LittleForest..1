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



    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val sharedPreference = SharedPreference()
    val backgroundColor = ContextCompat.getColor(context, R.color.background)
    val naviColor = ContextCompat.getColor(context, R.color.navi)
    var onWriteCommentClicked: OnWriteCommentClicked? = null
    var onPostClicked: OnPostClicked? = null

    class MyViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {

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
        val containerMainComment = layout.findViewById<RelativeLayout>(R.id.mainComentContainer)
        val tvMainComentUserName = layout.findViewById<TextView>(R.id.commentUserName)
        val ivMainComentLike = layout.findViewById<ImageView>(R.id.mainCommentLike)
        val tvMainCommentContents = layout.findViewById<TextView>(R.id.commentContents)
        val ivMyProfile = layout.findViewById<ImageView>(R.id.myProfilePhoto)
        val etWriteComment = layout.findViewById<EditText>(R.id.writeComment)
        val tvPostedDate = layout.findViewById<TextView>(R.id.postedDate)
        val uploadComment = layout.findViewById<TextView>(R.id.uploadComment)
        val page = layout.findViewById<RelativeLayout>(R.id.page)
        val compass = layout.findViewById<ImageView>(R.id.compass)
        val likeBtn = layout.findViewById<ImageView>(R.id.likeBtn)
        val commentContainer = layout.findViewById<RelativeLayout>(R.id.mainComentContainer)
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

            val prettyTime = PrettyTime(Locale.KOREA)
            val item = postList[position]
            var isLikingPost = item.isLikingPost
            var postLikes = item.postLikes
            var isLikingComment = item.isLikingComment
            val email = sharedPreference.getString(context, "email")
            val user_name = sharedPreference.getString(context, "name")
            var commentCount = item.commentNumber


            fun likePost(email: String, postId: Int, user_name: String) {

                val call: Call<ServerResonse> = apiInterface.likePost(email, postId, user_name)
                call.enqueue(object : Callback<ServerResonse> {
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {


                    }
                })


            }

            fun unlikePost(email: String, postId: Int) {

                val call: Call<ServerResonse> = apiInterface.unLikePost(email, postId)
                call.enqueue(object : Callback<ServerResonse> {
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {

                    }
                })

            }

            fun likeComment(email: String, commentId: Int, user_name: String) {

                val call: Call<ServerResonse> = apiInterface.likeComment(email, commentId, user_name)
                call.enqueue(object : Callback<ServerResonse> {
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {


                    }
                })

            }

            fun unlikeComment(email: String, commentId: Int) {

                val call: Call<ServerResonse> = apiInterface.unLikeComment(email, commentId)
                call.enqueue(object : Callback<ServerResonse> {
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {

                    }
                })


            }


            if (item.postLikes == 0) {
                (holder as MyViewHolder).tvCountLikes.visibility = View.GONE
                holder.tvCountLikes.text = null
            } else {
                holder.tvCountLikes.visibility = View.VISIBLE
                holder.tvCountLikes.text = "좋아요 ${item.postLikes}개"
            }









            fun startOtherUserPage() {
                val intent = Intent(context, OtherUser::class.java)
                intent.putExtra("otherUserName", item.userName)
                context.startActivity(intent)
            }
            holder.ivUserProfilePhoto.setOnClickListener {


                startOtherUserPage()


            }
            holder.tvUserName.setOnClickListener {

                startOtherUserPage()
            }



            if (!isLikingPost!!) {
                holder.likeBtn.setImageResource(R.drawable.tree)
            } else {
                holder.likeBtn.setImageResource(R.drawable.tree_selected)
            }


            holder.likeBtn.setOnClickListener {
                if (!isLikingPost!!) {
                    likePost(email!!, item.postId!!, user_name!!)
                    postLikes = postLikes!! + 1
                    holder.likeBtn.setImageResource(R.drawable.tree_selected)
                    isLikingPost = true
                    if (postLikes == 0) {
                        holder.tvCountLikes.visibility = View.GONE
                        holder.tvCountLikes.text = null
                    } else {
                        holder.tvCountLikes.visibility = View.VISIBLE
                        holder.tvCountLikes.text = "좋아요 ${postLikes}개"
                    }
                } else {
                    unlikePost(email!!, item.postId!!)
                    postLikes = postLikes!! - 1
                    holder.likeBtn.setImageResource(R.drawable.tree)
                    isLikingPost = false
                    if (postLikes == 0) {
                        holder.tvCountLikes.visibility = View.GONE
                        holder.tvCountLikes.text = null
                    } else {
                        holder.tvCountLikes.visibility = View.VISIBLE
                        holder.tvCountLikes.text = "좋아요 ${postLikes}개"
                    }
                }
            }





            if (!isLikingComment!!) {
                holder.ivMainComentLike.setImageResource(R.drawable.tree)
            } else {
                holder.ivMainComentLike.setImageResource(R.drawable.tree_selected)

            }

            holder.ivMainComentLike.setOnClickListener {
                if (!isLikingComment!!) {
                    likeComment(email!!, item.mainCommentId!!, user_name!!)
                    holder.ivMainComentLike.setImageResource(R.drawable.tree_selected)
                    isLikingComment = true
                } else {
                    unlikeComment(email!!, item.mainCommentId!!)
                    holder.ivMainComentLike.setImageResource(R.drawable.tree)
                    isLikingComment = false
                }
            }
            // FILL IMAGE VIEW

            Glide.with(context).load(profilePhoto)
                .centerCrop()
                .into(holder.ivMyProfile)
            Glide.with(context).load(item.pageProfilePhoto)
                .fitCenter()
                .override(600, 200)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPageProfilePhoto)

            Glide.with(context).load(item.postImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPostImage)

            Glide.with(context).load(item.userProfilePhoto)
                .thumbnail(0.3f)
                .into(holder.ivUserProfilePhoto)



            fun openPage() {

                val intent = Intent(context, PlacePage::class.java)


                val placeName = item.pageName
                val placeId = item.pageId
                val placePhoto = item.pageProfilePhoto
                val placeType = item.pageType
                val placeProvince = item.pageProvince
                intent.putExtra("placeName", placeName)
                intent.putExtra("placeId", placeId)
                intent.putExtra("placePhoto", placePhoto)
                intent.putExtra("placeType", placeType)
                intent.putExtra("placeProvince", placeProvince)
                context.startActivity(intent)

            }


            holder.tvPageName.text = item.pageName
            holder.tvPageType.text = item.pageType
            holder.tvPageProvince.text = item.pageProvince


            holder.page.setOnClickListener {
                openPage()
            }
            holder.compass.setOnClickListener {
                openPage()
            }




            holder.tvUserName.text = item.userName
            holder.tvDescription.text = item.postDescription


//
//        holder.etWriteComment.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus)
//
//        }


            holder.etWriteComment.setOnClickListener {
                onWriteCommentClicked?.onWriteCommentClicked()
            }




            holder.tvShowAllComents.setOnClickListener {
                val intent = Intent(context, Comment::class.java)
                intent.putExtra("post_user_name", item.userName)
                intent.putExtra("post_description", item.postDescription)
                intent.putExtra("post_user_profile", item.userProfilePhoto)
                intent.putExtra("post_date", item.postDate)
                intent.putExtra("post_id", item.postId!!)
                context.startActivity(intent)
            }




            if (commentCount!! < 2) {
                holder.tvShowAllComents.visibility = View.GONE
                holder.tvShowAllComents.text = null
            } else {
                holder.tvShowAllComents.text = "댓글 ${commentCount}개 모두 보기"
                holder.tvShowAllComents.visibility = View.VISIBLE
            }

            holder.tvMainComentUserName.text = item.mainCommentUserName
            holder.tvMainCommentContents.text = item.mainComment

            if (holder.tvMainCommentContents.text.isNullOrBlank()) {
                holder.commentContainer.visibility = View.GONE
            } else {
                holder.commentContainer.visibility = View.VISIBLE

            }


            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val date = simpleDateFormat.parse(item.postDate)
            val ago = prettyTime.format(date)


            holder.tvPostedDate.text = ago
            holder.etWriteComment.clearFocus()



            holder.etWriteComment.setOnEditorActionListener(object :
                TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {


                        onPostClicked?.onPostClicked(position)

                        holder.commentContainer.visibility = View.VISIBLE
                        holder.tvMainComentUserName.text =
                            sharedPreference.getString(context, "name")
                        val call: Call<ArrayList<CommentData>> = this@HomeRecyclerAdapter.apiInterface
                            .writeComment(
                                sharedPreference.getString(context, "email")!!,
                                item.postId!!,
                                sharedPreference.getString(context, "name")!!,
                                holder.etWriteComment.text.toString()
                            )
                        if (holder.etWriteComment.text.isNotEmpty() && holder.etWriteComment.text.isNotBlank()) {
                            call.clone().enqueue(object : Callback<ArrayList<CommentData>> {
                                override fun onFailure(call: Call<ArrayList<CommentData>>, t: Throwable) {

                                }

                                override fun onResponse(
                                    call: Call<ArrayList<CommentData>>,
                                    response: Response<ArrayList<CommentData>>
                                ) {

                                }


                            })

                            commentCount = commentCount!! + 1

                            if (commentCount!! > 1) {
                                holder.tvShowAllComents.visibility = View.VISIBLE
                                holder.tvShowAllComents.text = "댓글 ${commentCount!!}개 모두 보기"
                            }
                            holder.tvMainCommentContents.text =
                                holder.etWriteComment.text.toString()
                        }
                        holder.etWriteComment.text = null
                        holder.etWriteComment.clearFocus()
                        hideKeyboard(activity)


                    }
                    return false
                }

            })
            holder.etWriteComment.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (holder.etWriteComment.text.isEmpty()) {
                        holder.uploadComment.setTextColor(naviColor)
                    } else {
                        holder.uploadComment.setTextColor(backgroundColor)
                        holder.uploadComment.setOnClickListener {

                            onPostClicked?.onPostClicked(position)

                            holder.commentContainer.visibility = View.VISIBLE
                            holder.tvMainComentUserName.text =
                                sharedPreference.getString(context, "name")
                            val call: Call<ArrayList<CommentData>> = this@HomeRecyclerAdapter.apiInterface
                                .writeComment(
                                    sharedPreference.getString(context, "email")!!,
                                    item.postId!!,
                                    sharedPreference.getString(context, "name")!!,
                                    holder.etWriteComment.text.toString()
                                )
                            if (holder.etWriteComment.text.isNotEmpty() && holder.etWriteComment.text.isNotBlank()) {
                                call.clone().enqueue(object : Callback<ArrayList<CommentData>> {
                                    override fun onFailure(call: Call<ArrayList<CommentData>>, t: Throwable) {

                                    }

                                    override fun onResponse(
                                        call: Call<ArrayList<CommentData>>,
                                        response: Response<ArrayList<CommentData>>
                                    ) {

                                    }


                                })

                                commentCount = commentCount!! + 1

                                if (commentCount!! > 1) {
                                    holder.tvShowAllComents.visibility = View.VISIBLE
                                    holder.tvShowAllComents.text = "댓글 ${commentCount!!}개 모두 보기"
                                }
                                holder.tvMainCommentContents.text =
                                    holder.etWriteComment.text.toString()
                            }
                            holder.etWriteComment.text = null
                            holder.etWriteComment.clearFocus()
                            hideKeyboard(activity)



                        }
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }


            })



            holder.ivPostImage.setOnClickListener {

                holder.etWriteComment.clearFocus()
                holder.etWriteComment.isFocusable = false
                hideKeyboard(activity)
            }

        }


    }





    fun notifyDataChanged(){
        notifyDataSetChanged()
        isLoading = false
    }





    interface OnWriteCommentClicked{

        fun onWriteCommentClicked()
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


}