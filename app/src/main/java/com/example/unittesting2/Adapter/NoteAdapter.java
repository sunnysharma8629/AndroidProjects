package com.example.unittesting2.Adapter;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unittesting2.Models.GetterSetter;
import com.example.unittesting2.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final String TAG = "NotesRecyclerAdapter";

    private List<GetterSetter> notes = new ArrayList<>();
    private OnNoteListener onNoteListener;

    public NoteAdapter(OnNoteListener onNoteListener) {
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterlayoutfile, parent, false);
        return new ViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try{
           // String month = notes.get(position).getTimestamp().substring(0, 2);
            //month = DateUtil.getMonthFromNumber(month);
           // String year = notes.get(position).getTimestamp().substring(3);
            //String timestamp = month + " " + year;
            ((ViewHolder)holder).timestamp.setText(notes.get(position).getTimestamp());
            ((ViewHolder)holder).title.setText(notes.get(position).getTitle());
        }catch (NullPointerException e){
            //Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage() );
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public GetterSetter getNote(int position){
        if(notes.size() > 0){
            return notes.get(position);
        }
        return null;
    }

    public void removeNote(GetterSetter note){
        notes.remove(note);
        notifyDataSetChanged();
    }

    public void setNotes(List<GetterSetter> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView timestamp, title;
        OnNoteListener mOnNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.note_timestamp);
            title = itemView.findViewById(R.id.note_title);
            mOnNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnNoteListener.onNoteClick(getNote(getAdapterPosition()));
        }
    }

    public interface OnNoteListener{
        void onNoteClick(GetterSetter note);
    }






}
