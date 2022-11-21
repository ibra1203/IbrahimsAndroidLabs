package algonquin.cst2335.mahf0040;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private ChatMessageDAO mDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String currentDateAndTime = sdf.format(new Date());

        if(messages == null)
        {
            chatModel.messages.setValue( messages = new ArrayList<ChatMessage>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
                mDAO = db.cmDAO();
                List<ChatMessage> allMessages = mDAO.getAllMessages();
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database
//                binding.recycleView.setAdapter( myAdapter ); //You can then load the RecyclerView
            });
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

    public class MyRowHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk ->{

                int position = getAbsoluteAdapterPosition();

                RecyclerView.ViewHolder newRow = myAdapter.createViewHolder(null, myAdapter.getItemViewType(position));

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );

                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question: ")
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .setPositiveButton("Yes", (dialog, cl) -> {

                            ChatMessage removedMessage = messages.get(position);
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            Snackbar.make(messageText,"You deleted message #"+ position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk1 -> {

                                        messages.add(position, removedMessage);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .create().show();


            });

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);


        }
    }
}