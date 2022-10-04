package com.example.juschedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.juschedule.activities.CompulsorySpecializationActivity
import com.example.juschedule.pojo.Courses
import com.example.juschedule.R
import java.util.ArrayList

class CompulsoryAdapter(private val coursesList3: ArrayList<Courses>, private val listener: CompulsorySpecializationActivity)
    : RecyclerView.Adapter<CompulsoryAdapter.comulsoryViewHolder>(){

        class comulsoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.course_name)
            val card = itemView.findViewById<CardView>(R.id.card)!!
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): comulsoryViewHolder {
        val listItemView=LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return comulsoryViewHolder(listItemView)
    }

    override fun getItemCount(): Int {
            return coursesList3.size
    }

    override fun onBindViewHolder(holder: comulsoryViewHolder, position: Int) {

        val course =coursesList3[position]
        holder.textView.text=course.coursesName.toString()
        holder.card.setOnClickListener {
            listener.onClickListener(course)
        }
    }
}