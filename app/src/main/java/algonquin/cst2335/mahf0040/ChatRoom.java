package algonquin.cst2335.mahf0040;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    int position;
    TextView selectedmessage;
    View itemView;
    boolean doBackPressed = false;

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() )
        {
            case R.id.delete:

                ChatMessage thisMessage = messages.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage(  thisMessage.message );

                builder.setTitle("Do you want to delete this? ");

                builder.setNegativeButton("No", (dialog, cl)->{   });
                builder.setPositiveButton("Yes", (dialog, cl)->{

                    Snackbar.make( selectedmessage, "You deleted position #" + position, Snackbar.LENGTH_LONG)
                            .setAction( "Undo", click-> {

                                Executor thread = Executors.newSingleThreadExecutor();
                                thread.execute( () -> {
                                    mDAO.insertMessage(thisMessage);
                                });
                                chatModel.messages.getValue().add(thisMessage);
                                myAdapter.notifyItemInserted( position );

                            } )  .show();

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute( () -> {
                        mDAO.deleteMessage(thisMessage);
                    });

                    myAdapter.notifyItemRemoved( position );
                    chatModel.messages.getValue().remove(position);
                    if (doBackPressed){
                        doBackPressed = false;
                        this.onBackPressed();
                    }
                });
                builder.create().show();
                break;
            case R.id.info:
                Toast toast = Toast.makeText(getApplicationContext(), "Version 1.0 created by Ibrahim Mahfouz", Toast.LENGTH_LONG);
                toast.show();
                break;
        }

        return true;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);



        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").allowMainThreadQueries().build();
        mDAO = db.cmDAO();


        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();


        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String currentDateAndTime = sdf.format(new Date());



        if(messages == null)
        {
            chatModel.messages.setValue( messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {

                // List<ChatMessage> allMessages = mDAO.getAllMessages();
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database
                binding.recycleView.setAdapter( myAdapter );
            });
        }



        binding.sendBtn.setOnClickListener(click -> {


            int position;

            if (messages.size() == 0){
                position = 0;
            }else{
                position = messages.get(messages.size() - 1).getId() + 1;
            }

            ChatMessage chat = new ChatMessage(position, binding.textInput.getText().toString(),currentDateAndTime,true);
            messages.add(chat);

            mDAO.insertMessage(chat);

            myAdapter.notifyItemInserted(position);

            //clear the previous text
            binding.textInput.setText("");

        });



        binding.receiveBtn.setOnClickListener( click -> {

            int position;
            if (messages.size() == 0){
                position = 0;
            }else{
                position = messages.get(messages.size() - 1).getId() + 1;
            }

            ChatMessage chat = new ChatMessage(position, binding.textInput.getText().toString(),currentDateAndTime,false);
            messages.add(chat);

            mDAO.insertMessage(chat);

            myAdapter.notifyItemInserted(position);

            //clear the previous text:
            binding.textInput.setText("");
        });



        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                SentMessageBinding sBinding = SentMessageBinding.inflate(getLayoutInflater());
                ReceiveMessageBinding rBinding = ReceiveMessageBinding.inflate(getLayoutInflater());

                if (viewType == 0) {

                    return new MyRowHolder(sBinding.getRoot(),messages,mDAO,myAdapter);
                }
                else if (viewType == 1){

                    return new MyRowHolder(rBinding.getRoot(), messages, mDAO, myAdapter);
                }else{
                    return null;
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

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {

            MessageDetailsFragment  chatFragment = new MessageDetailsFragment( newMessageValue );

            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.add(R.id.fragmentLocation,chatFragment);
            tx.addToBackStack("Back to previous activity");
            tx.commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, chatFragment)
                    .commit();
        });


    }
    public class MyRowHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;


        public MyRowHolder(@NonNull View itemView, ArrayList<ChatMessage> messages, ChatMessageDAO mDAO, RecyclerView.Adapter myAdapter) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            selectedmessage = messageText;
            timeText = itemView.findViewById(R.id.timeText);

            itemView.setOnClickListener(clk -> {
                doBackPressed = true;
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);


                //   RecyclerView.ViewHolder newRow = ChatRoom.this.myAdapter.createViewHolder(null, ChatRoom.this.myAdapter.getItemViewType(position));

//                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
//
//                builder.setMessage("Do you want to delete the message: " + messageText.getText())
//                        .setTitle("Question: ")
//                        .setNegativeButton("No", (dialog, cl) -> {})
//                        .setPositiveButton("Yes", (dialog, cl) -> {
//
//                            ChatMessage removedMessage = ChatRoom.this.messages.get(position);
//                            ChatRoom.this.messages.remove(position);
//                            ChatRoom.this.myAdapter.notifyItemRemoved(position);
//
//                            Snackbar.make(messageText,"You deleted message #"+ position, Snackbar.LENGTH_LONG)
//                                    .setAction("Undo", clk1 -> {
//
//                                        ChatRoom.this.messages.add(position, removedMessage);
//                                        ChatRoom.this.myAdapter.notifyItemInserted(position);
//                                    })
//                                    .show();
//                        })
//                        .create().show();
//
//
//            });




            });
        }
    }
}