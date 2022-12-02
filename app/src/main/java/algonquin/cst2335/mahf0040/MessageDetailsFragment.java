package algonquin.cst2335.mahf0040;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.mahf0040.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    String isSentButton;
    ChatMessage selected;
    public MessageDetailsFragment(ChatMessage message){
        selected = message;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        if (selected.isSentButton){
            isSentButton = "Sent";
        }else {
            isSentButton = "Recieved";
        }



        binding.messageInfo.setText(selected.message);
        binding.timeInfo.setText(selected.timeSent);
        binding.sendMessageInfo.setText(isSentButton);
        binding.IdDatabase.setText("Id = " + selected.id);

        return binding.getRoot();

    }
}
