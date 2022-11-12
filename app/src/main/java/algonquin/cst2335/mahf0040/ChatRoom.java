package algonquin.cst2335.mahf0040;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.mahf0040.data.ChatRoomViewModel;
import algonquin.cst2335.mahf0040.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.mahf0040.databinding.ReceiveMessageBinding;
import algonquin.cst2335.mahf0040.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {


    ActivityChatRoomBinding binding;

    //ArrayList<String> messages = new ArrayList<>();
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatRoomViewModel chatModel ;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        messages = chatModel.messages.getValue();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String currentDateAndTime = sdf.format(new Date());

        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendBtn.setOnClickListener(click -> {
            ChatMessage chat = new ChatMessage(binding.textInput.getText().toString(),currentDateAndTime,true);
            messages.add(chat);
            myAdapter.notifyItemInserted(messages.size()-1);

            //clear the previous text
            binding.textInput.setText("");

        });

        binding.receiveBtn.setOnClickListener( click -> {
            ChatMessage chat = new ChatMessage(binding.textInput.getText().toString(),currentDateAndTime,false);
            messages.add(chat);
            myAdapter.notifyItemInserted(messages.size()-1);

            //clear the previous text:
            binding.textInput.setText("");
        });



        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
                else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                //holder.messageText.setText("");

                holder.messageText.setText("");
                holder.timeText.setText("");
                ChatMessage obj = messages.get(position);
                holder.timeText.setText(obj.getTimeSent());
                holder.messageText.setText(obj.getMessage());



            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position){
                ChatMessage chat = messages.get(position);
                int viewType = 0;
                if(chat.isSentButton()){
                    viewType = 0;
                }else{
                    viewType = 1;
                }
                return viewType;
            }


        });



    }

    class MyRowHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);



        }
    }
}