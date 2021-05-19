package com.kenyrim.mvvm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*

class NoteRecyclerAdapter(private val viewModel: MainViewModel, private val arrayList: ArrayList<String>, private val context: Context): RecyclerView.Adapter<NoteRecyclerAdapter.NotesVoewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteRecyclerAdapter.NotesVoewHolder {
       val root = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return NotesVoewHolder(root)
    }

    override fun onBindViewHolder(holder: NoteRecyclerAdapter.NotesVoewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {
       if (arrayList.size == 0){
           Toast.makeText(context,"List is empty",Toast.LENGTH_LONG).show()
       }
        return arrayList.size
    }

    inner class NotesVoewHolder(private val binding: View):RecyclerView.ViewHolder(binding) {
        fun bind(model:String){
            binding.title.text = model
            binding.delete.setOnClickListener {
                viewModel.remove(model)
                notifyItemRemoved(arrayList.indexOf(model))
            }
        }

    }
}