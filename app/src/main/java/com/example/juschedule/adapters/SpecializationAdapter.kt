package com.example.juschedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.juschedule.pojo.Courses
import com.example.juschedule.R
import com.example.juschedule.activities.SpecializationRequirementsActivity
import java.util.ArrayList


class SpecializationAdapter(private val mList: ArrayList<Courses>, private val listener: SpecializationRequirementsActivity)
    : RecyclerView.Adapter<SpecializationAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {


        val textView: TextView = itemView.findViewById(R.id.course_name)
        val card =itemView.findViewById<CardView>(R.id.card)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        holder.textView.text = item.coursesName.toString()



        holder.card.setOnClickListener {
            listener.onClickListener(item)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

}