package com.benhan.bluegreen

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeRecyclerAdapter(val context: Context ): RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>() {

    val postList = ArrayList<PostData>()

    val apiClient = ApiClient()
    val apiInterface = apiClient.getApiClient().create(ApiInterface::class.java)
    val sharedPreference = SharedPreference()
    val backgroundColor = ContextCompat.getColor(context, R.color.background)
    val naviColor = ContextCompat.getColor(context, R.color.navi)
    val getCurrentTime = Calendar.getInstance().time
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val currentTime = sdf.format(getCurrentTime)


    class MyViewHolder(val layout: RelativeLayout): RecyclerView.ViewHolder(layout){

        val ivPageProfilePhoto = layout.findViewById<ImageView>(R.id.pageProfilePhoto)
        val tvPageName = layout.findViewById<TextView>(R.id.pageName)
        val tvPageType = layout.findViewById<TextView>(R.id.pageType)
        val ivPostImage = layout.findViewById<ImageView>(R.id.postImage)
        val ivUserProfilePhoto = layout.findViewById<ImageView>(R.id.userProfilePhoto)
        val tvUserName = layout.findViewById<TextView>(R.id.userName)
        val tvDescription = layout.findViewById<TextView>(R.id.postDescription)
        val tvCountLikes = layout.findViewById<TextView>(R.id.countLikes)
        val tvShowAllComents = layout.findViewById<TextView>(R.id.showAllComents)
        val containerMainComment = layout.findViewById<RelativeLayout>(R.id.mainComentContainer)
        val tvMainComentUserName = layout.findViewById<TextView>(R.id.mainCommentUserName)
        val ivMainComentLike = layout.findViewById<ImageView>(R.id.mainCommentLike)
        val tvMainCommentContents = layout.findViewById<TextView>(R.id.mainCommentContents)
        val containerReply = layout.findViewById<RelativeLayout>(R.id.commentReplyContainer)
        val tvReplyUserName = layout.findViewById<TextView>(R.id.replyUserName)
        val tvReplyContents = layout.findViewById<TextView>(R.id.replyContents)
        val ivCommentReplyLike = layout.findViewById<ImageView>(R.id.replyLikes)
        val etWriteComment = layout.findViewById<EditText>(R.id.writeComment)
        val tvPostedDate = layout.findViewById<TextView>(R.id.postedDate)
        val uploadComment = layout.findViewById<TextView>(R.id.uploadComment)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val treeRecyclerRow = LayoutInflater.from(parent.context)
            .inflate(R.layout.tree_recycler_row, parent, false) as RelativeLayout


        return MyViewHolder(treeRecyclerRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = postList[position]
        var commentLikeClicked = false
        var replyLikeClicked = false

        Glide.with(context).load(item.pageProfilePhoto).
            thumbnail(0.3f)
            .into(holder.ivPageProfilePhoto)

        holder.tvPageName.setText(item.pageName)
        holder.tvPageType.setText(item.pageType)

        Glide.with(context).load(item.postImage)
            .thumbnail(0.3f)
            .into(holder.ivPostImage)

        Glide.with(context).load(item.userProfilePhoto)
            .thumbnail(0.3f)
            .into(holder.ivUserProfilePhoto)

        holder.tvUserName.setText(item.userName)
        holder.tvDescription.setText(item.postDescription)
        if(item.postLikes!! > 0) {
            holder.tvCountLikes.setText("좋아요 ${item.postLikes}개")
        }

        if(item.commentNumber == 0){
            holder.tvShowAllComents.visibility = View.GONE
        } else {
            holder.tvShowAllComents.visibility = View.VISIBLE
            holder.tvShowAllComents.setText("댓글 ${item.commentNumber}개 모두 보기")
            holder.tvShowAllComents.setOnClickListener {

                val intent = Intent(context, Comment::class.java)
                context.startActivity(intent)


            }
        }


        if(item.mainComment!!.isEmpty()){
            holder.containerMainComment.visibility = View.GONE
        } else {
            holder.containerMainComment.visibility = View.VISIBLE
            holder.tvMainComentUserName.setText(item.mainCommentUserName)
            holder.tvMainCommentContents.setText(item.mainComment)
        }

        if(item.mainCommentReply!!.isEmpty()){
            holder.containerReply.visibility = View.GONE
        } else {
            holder.containerReply.visibility = View.VISIBLE
            holder.tvReplyContents.setText(item.mainCommentReply)
            holder.tvReplyUserName.setText(item.replyUserName)
        }


        holder.tvPostedDate.setText(item.postDate)



        holder.ivMainComentLike.setOnClickListener {

            if(!commentLikeClicked) {
                commentLikeClicked = true
                val call: Call<ServerResonse> = this.apiInterface.commentLikeAdd(item.mainCommentId!!, sharedPreference.getString(context, "email")!!)
                call.enqueue(object : Callback<ServerResonse>{
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
                val call: Call<ServerResonse> = this.apiInterface.commentUnlikeAdd(item.mainCommentId!!, sharedPreference.getString(context, "email")!!)
                call.enqueue(object : Callback<ServerResonse>{
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


        holder.ivCommentReplyLike.setOnClickListener {

            if(!replyLikeClicked) {
                replyLikeClicked = true
                val call: Call<ServerResonse> = this.apiInterface.replyLikeAdd(item.mainCommentId!!, sharedPreference.getString(context, "email")!!)
                call.enqueue(object : Callback<ServerResonse>{
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
                        Log.d("좋아요 에러 ", t.message)

                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {

                        holder.ivCommentReplyLike.setImageResource(R.drawable.tree_selected)
                    }


                })
            } else {
                replyLikeClicked = false
                val call: Call<ServerResonse> = this.apiInterface.replyUnlikeAdd(item.mainCommentId!!, sharedPreference.getString(context, "email")!!)
                call.enqueue(object : Callback<ServerResonse>{
                    override fun onFailure(call: Call<ServerResonse>, t: Throwable) {
                        Log.d("좋아요 에러 ", t.message)
                    }

                    override fun onResponse(
                        call: Call<ServerResonse>,
                        response: Response<ServerResonse>
                    ) {

                        holder.ivCommentReplyLike.setImageResource(R.drawable.tree)
                    }


                })


            }

        }


        holder.etWriteComment.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(holder.etWriteComment.text.isEmpty()){
                    holder.uploadComment.setTextColor(naviColor)
                }else {
                    holder.uploadComment.setTextColor(backgroundColor)
                    holder.uploadComment.setOnClickListener {

                        val call: Call<ServerResonse> = this@HomeRecyclerAdapter.apiInterface
                            .uploadComment(sharedPreference.getString(context, "email")!!,
                            sharedPreference.getString(context, "name")!!,
                                currentTime,
                                holder.etWriteComment.text.toString(),
                                item.postId!!
                            )

                        call.enqueue(object: Callback<ServerResonse>{
                            override fun onFailure(call: Call<ServerResonse>, t: Throwable) {

                            }

                            override fun onResponse(
                                call: Call<ServerResonse>,
                                response: Response<ServerResonse>
                            ) {
                                holder.tvMainComentUserName.setText(sharedPreference.getString(context, "name"))
                                holder.tvMainCommentContents.setText(holder.etWriteComment.text.toString())
                            }


                        })


                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }


        })


    }

    override fun getItemCount(): Int {


        return postList.size

    }



}