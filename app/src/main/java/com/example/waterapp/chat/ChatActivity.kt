package com.example.waterapp.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.waterapp.BuildConfig
import com.example.waterapp.R
import com.example.waterapp.chat.chatadapter.ChatAdapter
import com.example.waterapp.chatModel.AllClearChatViewModel
import com.example.waterapp.chatModel.DoChatBody
import com.example.waterapp.chatModel.DoChatViewModel
import com.example.waterapp.chatModel.GetDoChatResponse
import com.example.waterapp.chatModel.GetDoChatViewModel
import com.example.waterapp.chatModel.SingleChatDeleteViewModel
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityChatBinding
import com.example.waterapp.updateProfileModel.GetUpdateProfileViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var messageList: List<GetDoChatResponse.Response> = ArrayList()
    private val doChatViewModel: DoChatViewModel by viewModels()
    private val getDoChatViewModel: GetDoChatViewModel by viewModels()
    private val getUpdateProfileViewModel: GetUpdateProfileViewModel by viewModels()
    private val allClearChatViewModel: AllClearChatViewModel by viewModels()
    private val singleChatDeleteViewModel: SingleChatDeleteViewModel by viewModels()
    private val IMAGE_PICK_CODE = 1000
    private var userId = ""
    private var adminId = ""
    private lateinit var chatAdapter: ChatAdapter
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var handler: Handler

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()
        adminId = sharedPreferences.getString("adminId", adminId).toString().trim()

        //observer and api
        getUpdateProfileApi(userId)
        getUpdateProfileObserver()
        getChatListApi(userId)
        allChatClearObserver()

        handler = Handler(Looper.getMainLooper())

        binding.arrowBack.setOnClickListener { finish() }
        binding.sendBtn.setOnClickListener   { sendMessage() }
        binding.selectImg.setOnClickListener { pickImageFromGallery() }
        binding.threedots.setOnClickListener { menuPopup() }

        chatAdapter = ChatAdapter(this, singleChatDeleteViewModel ,messageList)
        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
        binding.chatRecycler.adapter = chatAdapter

        handler.postDelayed(object : Runnable {
            override fun run() {
                getChatListApi(userId)
                handler.postDelayed(this, 2000)
            }
        }, 2000)
        doChatObserver()
        getChatListObserver()
    }
    private fun allChatClearObserver() {
        allClearChatViewModel.progressIndicator.observe(this){

        }
        allClearChatViewModel.mRejectResponse.observe(this){
            val status = it.peekContent().success
            val message = it.peekContent().message
            if (status == true){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                getChatListApi(userId)
            }
        }
        allClearChatViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@ChatActivity, it)
        }
    }
    private fun menuPopup() {
        val popupMenu = PopupMenu(this, binding.threedots)

        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_popup, popupMenu.menu)

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.clear_data -> {
                    //call here api of clear data
                    clearAllChatApi(userId)
                    true
                }
                else -> false
            }
        }
    }

    private fun clearAllChatApi(userId: String) {
        allClearChatViewModel.clearAllChat(userId, this)

    }
    private fun getUpdateProfileApi(userId: String) {
        getUpdateProfileViewModel.getUpdateProfile(userId, progressDialog, this)
    }
    private fun getUpdateProfileObserver() {
        getUpdateProfileViewModel.progressIndicator.observe(this) {

        }
        getUpdateProfileViewModel.mCustomerResponse.observe(this) {
            val status = it.peekContent().success
            val userData = it.peekContent().data

            if (status == true) {
                if (userData?.userName == null) {

                } else {
                    binding.nameTextView.text = Editable.Factory.getInstance().newEditable(userData.userName.toString())
                }
                if (userData?.profileImage == null) {

                } else {
                    val url = it.peekContent().data?.profileImage
                    Glide.with(this).load(BuildConfig.IMAGE_KEY + url).into(binding.userProfile)

                }
            }
        }
        getUpdateProfileViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun getChatListObserver() {
        getDoChatViewModel.progressIndicator.observe(this) {

        }
        getDoChatViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success
            val response = it.peekContent().response

            if (status == true) {
                messageList = response!!
                if (messageList.isNotEmpty()) {
                    binding.chatRecycler.smoothScrollToPosition(messageList.size - 1)
                    chatAdapter!!.notifyDataSetChanged()
                    chatAdapter?.addChat(messageList)
                }
            }
        }
        getDoChatViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@ChatActivity, it)
        }
    }

    private fun doChatObserver() {
        doChatViewModel.progressIndicator.observe(this) {

        }
        doChatViewModel.mRejectResponse.observe(this) {
            val status = it.peekContent().success

            if (status == true) {
                getChatListApi(userId)
            }
        }
        doChatViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@ChatActivity, it)
        }
    }

    private fun getChatListApi(userId: String) {
        getDoChatViewModel.getDoChat(userId, this)
    }

    private fun sendMessage() {
        binding.sendBtn.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()
            if (messageText.isEmpty()) {
                binding.messageInput.error = getString(R.string.enter_mesage)
                return@setOnClickListener
            }

            send_Message(messageText)
            binding.messageInput.text.clear()
            binding.messageInput.requestFocus()

        }
    }

    private fun send_Message(messages: String) {
        val doChatBody = DoChatBody(
            supportAgentId = adminId,
            userId = userId,
            message = messages,

            )
        doChatViewModel.doChat(doChatBody, this)
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onResume() {
        super.onResume()
        getChatListApi(userId)
    }
}