package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_newmessage.view.*
import java.text.FieldPosition

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title="Select User"

//
//        val adapter = GroupAdapter<ViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        recview_newmessage.adapter =  adapter

        fetchUsers()
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }
    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {

                    Log.d("NewMessage", it.toString() )

                    val user = it.getValue(User::class.java)
                    val curr_user = FirebaseAuth.getInstance().currentUser

                    if (user != null && user.email != curr_user?.email.toString() ) {
                        adapter.add(UserItem(user))
                    }
                }
            adapter.setOnItemClickListener { item, view ->

                val userItem = item as UserItem
                val intent = Intent(view.context, ChattingActivity::class.java)
                //intent.putExtra(USER_KEY, userItem.user.username)
                intent.putExtra(USER_KEY, userItem.user)
                startActivity(intent)
                finish()
            }
                recview_newmessage.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}
class UserItem(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int){
    viewHolder.itemView.username_textView_newmessage.text = user.username

    }
    override fun getLayout(): Int{
        return R.layout.user_row_newmessage
    }
}
//class CustomAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>{

