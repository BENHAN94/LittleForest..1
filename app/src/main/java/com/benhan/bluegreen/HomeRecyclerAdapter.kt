package com.benhan.bluegreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import kotlin.collections.ArrayList

class HomeRecyclerAdapter(val context: Context, val activity: Activity, val postList: ArrayList<PostData>, val profilePhoto: String ): RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>() {


    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val sharedPreference = SharedPreference()
    val backgroundColor = ContextCompat.getColor(context, R.color.background)
    val naviColor = ContextCompat.getColor(context, R.color.navi)
    val getCurrentTime = Calendar.getInstance().time
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val currentTime = sdf.format(getCurrentTime)
    var isLoading = false
    var isLikingComment = false
    var onWriteCommentClicked: OnWriteCommentClicked? = null
    var onClickPostCommentClicked: FragmentTree.OnClickPostCommentClicked? = null
    val fragmentTree = FragmentTree()


    class MyViewHolder(val layout: RelativeLayout) : RecyclerView.ViewHolder(layout) {

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
        //        val containerReply = layout.findViewById<RelativeLayout>(R.id.commentReplyContainer)
//        val tvReplyUserName = layout.findViewById<TextView>(R.id.replyUserName)
//        val tvReplyContents = layout.findViewById<TextView>(R.id.replyContents)
//        val ivCommentReplyLike = layout.findViewById<ImageView>(R.id.replyLikes)
        val etWriteComment = layout.findViewById<TextView>(R.id.writeComment)
        val tvPostedDate = layout.findViewById<TextView>(R.id.postedDate)
        val uploadComment = layout.findViewById<TextView>(R.id.uploadComment)
        val page = layout.findViewById<RelativeLayout>(R.id.page)
        val compass = layout.findViewById<ImageView>(R.id.compass)
        val likeBtn = layout.findViewById<ImageView>(R.id.likeBtn)
        val commentContainer = layout.findViewById<RelativeLayout>(R.id.mainComentContainer)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val treeRecyclerRow = LayoutInflater.from(parent.context)
            .inflate(R.layout.tree_recycler_row, parent, false) as RelativeLayout






        return MyViewHolder(treeRecyclerRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {




        val prettyTime = PrettyTime(Locale.getDefault())
        val item = postList[position]
        var commentLikeClicked = false
        val email = sharedPreference.getString(context, "email")

        fun likePost(email: String, postId: Int){

            val call: Call<ServerResonse> = apiInterface.likePost(email, postId )
            call.enqueue(object : Callback<ServerResonse>{
                override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                }

                override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {


                    item.postLikes = item.postLikes!! + 1

                    holder.likeBtn.setImageResource(R.drawable.tree_selected)
                    item.isLikingPost = true
                    if (item.postLikes == 0 ){
                        holder.tvCountLikes.visibility = View.GONE
                        holder.tvCountLikes.text = null
                    } else {
                        holder.tvCountLikes.visibility = View.VISIBLE
                        holder.tvCountLikes.text = "좋아요 ${item.postLikes}개"
                    }

                }
            })

        }

        fun unlikePost(email: String, postId: Int){

            val call: Call<ServerResonse> = apiInterface.unLikePost(email, postId )
            call.enqueue(object : Callback<ServerResonse>{
                override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                }

                override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {


                    item.postLikes = item.postLikes!! - 1
                    holder.likeBtn.setImageResource(R.drawable.tree)
                    item.isLikingPost = false

                    if (item.postLikes == 0 ){
                        holder.tvCountLikes.visibility = View.GONE
                        holder.tvCountLikes.text = null
                    } else {
                        holder.tvCountLikes.visibility = View.VISIBLE
                        holder.tvCountLikes.text = "좋아요 ${item.postLikes}개"
                    }
                }
            })

        }

        fun likeComment(email: String, commentId: Int){

            val call: Call<ServerResonse> = apiInterface.likeComment(email, commentId )
            call.enqueue(object : Callback<ServerResonse>{
                override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                }

                override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {


                }
            })

        }

        fun unlikeComment(email: String, commentId: Int){

            val call: Call<ServerResonse> = apiInterface.unLikeComment(email, commentId )
            call.enqueue(object : Callback<ServerResonse>{
                override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                }

                override fun onResponse(call: Call<ServerResonse>, response: Response<ServerResonse>) {

                }
            })



        }


        if (item.postLikes == 0 ){
            holder.tvCountLikes.visibility = View.GONE
            holder.tvCountLikes.text = null
        } else {
            holder.tvCountLikes.visibility = View.VISIBLE
            holder.tvCountLikes.text = "좋아요 ${item.postLikes}개"
        }









        fun startOtherUserPage(){
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




        holder.likeBtn.setOnClickListener {
            if(!item.isLikingPost!!){
                likePost(email!!, item.postId!!)
            } else {
                unlikePost(email!!, item.postId!!)
            }
        }


        if(!item.isLikingPost!!){

            holder.likeBtn.setImageResource(R.drawable.tree)

        } else {

            holder.likeBtn.setImageResource(R.drawable.tree_selected)

        }


        if(!isLikingComment){

            holder.ivMainComentLike.setImageResource(R.drawable.tree)
            holder.ivMainComentLike.setOnClickListener {
                likeComment(email!!, item.mainCommentId!!)
                holder.ivMainComentLike.setImageResource(R.drawable.tree_selected)
            }

        } else {

            holder.ivMainComentLike.setImageResource(R.drawable.tree_selected)
            holder.ivMainComentLike.setOnClickListener {
                unlikeComment(email!!, item.mainCommentId!!)
                holder.ivMainComentLike.setImageResource(R.drawable.tree)
            }
        }

        // FILL IMAGE VIEW

        Glide.with(context).load(profilePhoto)
            .centerCrop()
            .into(holder.ivMyProfile)
        Glide.with(context).load(item.pageProfilePhoto)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ivPageProfilePhoto)

        Glide.with(context).load(item.postImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ivPostImage)

        Glide.with(context).load(item.userProfilePhoto)
            .thumbnail(0.3f)
            .into(holder.ivUserProfilePhoto)



        fun openPage(){

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




        holder.etWriteComment.setOnClickListener {

            onWriteCommentClicked?.onWriteCommentClicked()

        }




        if (item.commentNumber!! < 2) {
            holder.tvShowAllComents.visibility = View.GONE
            holder.tvShowAllComents.text = null
        } else {
            holder.tvShowAllComents.text = "댓글 ${item.commentNumber}개 모두 보기"
            holder.tvShowAllComents.visibility = View.VISIBLE
            holder.tvShowAllComents.setOnClickListener {

                val intent = Intent(context, Comment::class.java)
                context.startActivity(intent)


            }
        }

        holder.tvMainComentUserName.text = item.mainCommentUserName
        holder.tvMainCommentContents.text = item.mainComment

        if (holder.tvMainCommentContents.text.isNullOrBlank()) {
            holder.commentContainer.visibility = View.GONE
        }else {
            holder.commentContainer.visibility = View.VISIBLE

        }


        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = simpleDateFormat.parse(item.postDate)
        val ago = prettyTime.format(date)


        holder.tvPostedDate.text = ago



        holder.ivMainComentLike.setOnClickListener {

            if (!commentLikeClicked) {
                commentLikeClicked = true
                val call: Call<ServerResonse> = this.apiInterface.commentLikeAdd(
                    item.mainCommentId!!,
                    sharedPreference.getString(context, "email")!!
                )
                call.enqueue(object : Callback<ServerResonse> {
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
                        Log.d("좋아요 에러 ", t.message)

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {

                        holder.ivMainComentLike.setImageResource(R.drawable.tree_selected)
                    }


                })
            } else {
                commentLikeClicked = false
                val call: Call<ServerResonse> = this.apiInterface.commentUnlikeAdd(
                    item.mainCommentId!!,
                    sharedPreference.getString(context, "email")!!
                )
                call.enqueue(object : Callback<ServerResonse> {
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
                        Log.d("좋아요 에러 ", t.message)
                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {

                        holder.ivMainComentLike.setImageResource(R.drawable.tree)
                    }


                })


            }

        }




//        holder.etWriteComment.clearFocus()
//        holder.etWriteComment.isFocusable = false
//        holder.etWriteComment.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                holder.etWriteComment.isFocusableInTouchMode = true
//                return false
//            }
//
//
//        })


//        holder.etWriteComment.setOnEditorActionListener(object : TextView.OnEditorActionListener {
//            override fun onEditorAction(
//                v: TextView?,
//                actionId: Int,
//                event: KeyEvent?
//            ): Boolean {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//
////                    holder.etWriteComment.isFocusable = false
//                    hideKeyboard(activity)
//                }
//                return false
//            }
//
//        })
        holder.etWriteComment.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(holder.etWriteComment.text.isEmpty()){
                    holder.uploadComment.setTextColor(naviColor)
                }else {
                    holder.uploadComment.setTextColor(backgroundColor)
                    holder.uploadComment.setOnClickListener {

                        holder.commentContainer.visibility = View.VISIBLE
                        holder.tvMainComentUserName.text = sharedPreference.getString(context, "name")
                        holder.tvMainCommentContents.text = holder.etWriteComment.text.toString()





                        val call: Call<ServerResonse> = this@HomeRecyclerAdapter.apiInterface
                            .writeComment(sharedPreference.getString(context, "email")!!,
                                item.postId!!,
                                sharedPreference.getString(context, "name")!!,
                                holder.etWriteComment.text.toString()
                            )

                        call.enqueue(object: Callback<ServerResonse>{
                            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                            }

                            override fun onResponse(
                                call: Call<ServerResonse>,
                                response: Response<ServerResonse>
                            ) {

                            }


                        })

                        if (item.commentNumber!! > 0 ) {
                            holder.tvShowAllComents.visibility = View.VISIBLE
                            holder.tvShowAllComents.text = "댓글 ${item.commentNumber!! + 1}개 모두 보기"
                            holder.tvShowAllComents.setOnClickListener {



                                val intent = Intent(context, Comment::class.java)
                                context.startActivity(intent)


                            }
                        }

                        holder.etWriteComment.clearFocus()
                        hideKeyboard(activity)

                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }


        })


        holder.ivPostImage.setOnClickListener {

            holder.etWriteComment.clearFocus()
//            holder.etWriteComment.isFocusable = false
            hideKeyboard(activity)
        }



    }

    override fun getItemCount(): Int {


        return postList.size

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

}