package com.example.juschedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.juschedule.R
import com.example.juschedule.activities.CompletedCoursesActivity
import com.example.juschedule.pojo.Courses


    class CompletedAdapter(private val coursesList2: ArrayList<Courses>, private val listener: CompletedCoursesActivity)
        : RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>(){

        class CompletedViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
            val textView: TextView= itemView.findViewById(R.id.course_name)
            val card = itemView.findViewById<CardView>(R.id.card)!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedViewHolder {
            var ItemView =
                LayoutInflater.from(parent.context).inflate(R.layout.course_item,parent,false)
            return CompletedViewHolder(ItemView)
        }

        override fun getItemCount(): Int {
            return coursesList2.size
        }

        override fun onBindViewHolder(holder: CompletedViewHolder, position: Int) {
            val course =coursesList2[position]
            holder.textView.text=course.coursesName.toString()
            holder.card.setOnClickListener {
                listener.onClickListener(course)
            }
        }
    }
