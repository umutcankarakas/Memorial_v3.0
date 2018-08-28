package com.memorial;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;

public class AddTaskDialog extends AppCompatDialogFragment{
    //@BindView(R.id.edit_title) EditText editTextTitle;
    //@BindView(R.id.edit_detail) EditText editTextDetail;

    private EditText editTextTitle;
    private EditText editTextDetail;
    private AddTaskDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("New Task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = editTextTitle.getText().toString();
                        String detail = editTextDetail.getText().toString();
                        listener.applyTexts(title, detail);
                    }
                });

        editTextTitle = view.findViewById(R.id.edit_title);
        editTextDetail = view.findViewById(R.id.edit_detail);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddTaskDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
            "Must implement AddTaskDialogListener");
        }
    }

    public interface AddTaskDialogListener{
        void applyTexts(String title, String detail);

    }
}
