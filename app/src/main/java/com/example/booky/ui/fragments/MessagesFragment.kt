import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booky.R
import com.example.booky.data.api.ChatAPIService
import com.example.booky.data.api.ChatService
import com.example.booky.data.api.RestApiService
import com.example.booky.data.api.RetrofitInstance
import com.example.booky.data.models.Conversation
import com.example.booky.ui.adapter.ConversationAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessagesFragment : Fragment() {
    private var conversationsRV: RecyclerView? = null
    private var shimmerFrameLayout: ShimmerFrameLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        // VIEW BINDING
        shimmerFrameLayout = view?.findViewById(R.id.shimmerLayout)
        conversationsRV = view?.findViewById(R.id.conversationsRV)


        shimmerFrameLayout!!.startShimmer();
        conversationsRV!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val sharedPreferences  = this.requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("userId", null).toString()

        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)

        ChatAPIService.chatService.getMyConversations(
            ChatService.MyConversationsBody(
                userID
            )
        ).enqueue(
            object : Callback<ChatService.ConversationsResponse> {
                override fun onResponse(
                    call: Call<ChatService.ConversationsResponse>,
                    response: Response<ChatService.ConversationsResponse>
                ) {
                    if (response.code() == 200) {
                        conversationsRV!!.adapter =
                            ConversationAdapter(response.body()?.conversations as MutableList<Conversation>)

                        shimmerFrameLayout!!.stopShimmer()
                        shimmerFrameLayout!!.visibility = View.GONE
                    } else {
                        println("status code is " + response.code())
                    }
                }

                override fun onFailure(
                    call: Call<ChatService.ConversationsResponse>,
                    t: Throwable
                ) {
                    println("HTTP ERROR")
                    t.printStackTrace()
                }
            }
        )
        return view





    }


}