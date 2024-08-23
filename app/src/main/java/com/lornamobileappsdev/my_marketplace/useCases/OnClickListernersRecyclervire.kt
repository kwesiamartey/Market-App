package com.lornamobileappsdev.my_marketplace.useCases

import com.lornamobileappsdev.my_marketplace.models.PostLikesData
import com.lornamobileappsdev.my_marketplace.responses.FeedbackData


interface OnClickListernersRecyclerMySavedItem {
    fun onClickListernersRecyclerMySavedItem(id:Int)
}

interface OnClickListernersRecyclervire {
    fun onReplyCellClickListener(data: FeedbackData)
}
interface UpdateProductViewTOServer {
    fun UpdateProductViewTOServer(view_numver:Int, post_id: Int):Boolean
}

interface OnCommentLikeListernersRecyclervire {
    suspend fun onCommentLikeListernersRecyclervire(comment_id: Int, user_id:Int,comment_user_id: Int , my_id: Int, like:Int) : Long
}

interface OnReplyLikeListernersRecyclervire {
    suspend fun onReplyLikeListernersRecyclervire(comment_id: Int, user_id:Int,comment_user_id: Int , my_id: Int, like:Int) : Long
}

interface CommentLikesCounts {
    suspend fun commentLikesCounts(post_id: Int, comment_id_user_id: Int, user_id: Int) : List<PostLikesData>
}

interface ReplyLikesCounts {
    suspend fun replyLikesCounts(post_id: Int, comment_id_user_id: Int, user_id: Int) : List<PostLikesData>
}