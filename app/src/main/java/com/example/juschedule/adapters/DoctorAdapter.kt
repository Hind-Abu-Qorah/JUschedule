package com.example.juschedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.juschedule.R
import com.example.juschedule.pojo.Division

class DoctorAdapter(private var doctorsList: ArrayList<Division>, var listener: ClickOnDoctor) :
    RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {


    interface ClickOnDoctor {
        fun setOnClickCheck(item: Division, position: Int)
    }

    class DoctorViewHolder(itemVew: View) : ViewHolder(itemVew) {
        val divison: TextView=itemVew.findViewById(R.id.Division)
        val teacher: TextView=itemVew.findViewById(R.id.teacher)
        val time: TextView=itemVew.findViewById(R.id.time)
        val today: TextView=itemVew.findViewById(R.id.today)
        val check: AppCompatImageView=itemVew.findViewById(R.id.choose_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val itemVew=LayoutInflater.from(parent.context).inflate(R.layout.doctor_item, parent, false)
        return DoctorViewHolder(itemVew)

    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctorl=doctorsList[position]
        holder.divison.text=doctorl.Division
        holder.teacher.text=doctorl.teacher
        holder.time.text=doctorl.time
        holder.today.text=doctorl.days
        holder.check.setOnClickListener {
            listener.setOnClickCheck(doctorl, position)
        }


    }

    override fun getItemCount(): Int {
        return doctorsList.size
    }


}