package com.example.sarah.nav;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
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
    private EditText editTextPassword;
    private ExampleDialogListener listener;

    private NotificationManagerCompat notificationManager;


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
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity().getApplicationContext(),"DAALA",Toast.LENGTH_SHORT).show();
                        sendOnChannel1();

                    }
                });
        return builder.create();
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
