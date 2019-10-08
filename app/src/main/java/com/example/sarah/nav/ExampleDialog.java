package com.example.sarah.nav;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.sarah.nav.AppNotification.CHANNEL_1_ID;

public class ExampleDialog extends AppCompatDialogFragment {
    EditText address;
    private ExampleDialogListener listener;

    private NotificationManagerCompat notificationManager;
    int flag=0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        address = view.findViewById(R.id.address);
        notificationManager = NotificationManagerCompat.from(getContext());


        builder.setView(view)
                .setTitle("Location Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getActivity().getApplicationContext(),"DAALA",Toast.LENGTH_SHORT).show();
                        Log.d("int","int is "+address.getText());
                        String location = String.valueOf(address.getText()).trim();
                        if(location.isEmpty()){
                            Toast.makeText(getActivity().getApplicationContext(),"Enter valid Location",Toast.LENGTH_SHORT).show();
                            flag=0;
                        }else {
                            flag=1;
                            Intent main = new Intent(getContext(), MainActivity.class);
                            startActivity(main);
                            //Toast.makeText(getContext(), "Refresh to load.", Toast.LENGTH_SHORT).show();
                            sendOnChannel1();
                        }
                    }
                });
        return builder.create();
    }
    public int flag2(){
        Log.d("flag","flag dialog is "+flag);
        return flag;
    }
    public void sendOnChannel1() {
        String title = "Findr";
        String message = "Order Placed";
        Log.d("enter notif",""+title+" "+message);

        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String username, String password);
    }
}
