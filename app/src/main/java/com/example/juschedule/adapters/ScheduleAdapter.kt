package com.example.juschedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.juschedule.pojo.Courses
import com.example.juschedule.R
import com.example.juschedule.activities.FacultyRequirementsActivity
import com.example.juschedule.activities.ScheduleActivity
import java.util.*


class ScheduleAdapter(
    private var scheduleList: ArrayList<Courses>,
    private var listener: clickOnSchedule
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    interface clickOnSchedule {
        fun setOnClickReplace(item: Courses, position: Int)
        fun setonClickCard(item: Courses,position: Int)
    }

    class ScheduleViewHolder(itemVew: View) : RecyclerView.ViewHolder(itemVew) {
        val houreNum: TextView=itemVew.findViewById(R.id.hoursNum)
        val couresNameSchedule: TextView=itemVew.findViewById(R.id.courseName)
        val teacher:TextView=itemVew.findViewById(R.id.teacher1)
        val time:TextView=itemVew.findViewById(R.id.time1)
        val today:TextView=itemVew.findViewById(R.id.today1)
        val Divison:TextView=itemVew.findViewById(R.id.Division1)
        val view1:View=itemView.findViewById(R.id.view1)
        val view2:View=itemView.findViewById(R.id.view2)
        val view3:View=itemView.findViewById(R.id.view3)
        val Replace: Button=itemVew.findViewById(R.id.replace)
        val card = itemView.findViewById<CardView>(R.id.card)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val itemView1=
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ScheduleViewHolder(itemView1)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {

        val scheduleCourse=scheduleList[position]
        holder.card.setOnClickListener {
                listener.setonClickCard(scheduleCourse,position)
        }
        holder.teacher.text=scheduleCourse.teacher.toString()
        holder.time.text=scheduleCourse.time.toString()
        holder.today.text=scheduleCourse.today.toString()
        holder.Divison.text=scheduleCourse.Division.toString()
        if(holder.time.text =="00:00 - 00:00" ){
            holder.time.text=""
            holder.view1.visibility=GONE
            holder.view2.visibility=GONE
            holder.view3.visibility=GONE
            holder.today.visibility=GONE
            holder.time.visibility=GONE
            holder.Divison.visibility=GONE
            holder.teacher.visibility=GONE
        }
        holder.houreNum.text=scheduleCourse.hourNum.toString()
        holder.couresNameSchedule.text=scheduleCourse.coursesName.toString()
        holder.Replace.setOnClickListener {
            listener.setOnClickReplace(scheduleCourse, position)
        }

    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }


}