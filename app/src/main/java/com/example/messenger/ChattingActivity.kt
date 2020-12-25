package com.example.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.android.synthetic.main.chat_row.view.*
import kotlinx.android.synthetic.main.chat_row2.view.*
import kotlinx.android.synthetic.main.edit_user.view.*

class ChattingActivity : AppCompatActivity() {
    companion object{
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<ViewHolder>()
    var touser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        recview_chat.adapter = adapter

        touser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = touser?.username
      //  setupmessages()  //displays all fake messages

        listenForMessage()

        send_chat.setOnClickListener{
            Log.d(TAG, "Attempt to send message")
            sendMessage()
        }

    }

    private fun listenForMessage() {
        val fromId= FirebaseAuth.getInstance().currentUser?.uid
        val toId = touser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/messages/")

        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if(chatMessage != null){
                    Log.d(TAG, chatMessage.text)
                }
                if (chatMessage?.fromId == FirebaseAuth.getInstance().uid) {

                    adapter.add(ChatItem(chatMessage!!.text))
                }
                else{
                    adapter.add(ChatItem2(chatMessage!!.text))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })

    }


    private fun sendMessage(){

       val text = edittext_chat.text.toString()
        val fromId= FirebaseAuth.getInstance()?.uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user?.uid.toString()
     val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        if(fromId==null) return
     //   val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
      //  val toreference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved chat messge: ${reference.key}")
                edittext_chat.text.clear()
                recview_chat.scrollToPosition(adapter.itemCount - 1)
            }
      //  toreference.setValue(chatMessage)
    }
//    private fun setupmessages(){
//        val adapter = GroupAdapter<ViewHolder>()
//        adapter.add(ChatItem("HELLO IT IS FROM"))
//        adapter.add(ChatItem2("HI LALLLALLALALALLALAL"))
//        adapter.add(ChatItem("HELLO IT IS FROM"))
//        adapter.add(ChatItem2("HI LALLLALLALALALLALAL"))
//
//        recview_chat.adapter = adapter
//    }
}

class ChatItem(val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from.text = text

    }

    override fun getLayout(): Int {
        return R.layout.chat_row

    }
}

class ChatItem2(val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_row2

    }
}