package com.example.juschedule.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.juschedule.R
import com.example.juschedule.adapters.DoctorAdapter
import com.example.juschedule.adapters.ScheduleAdapter
import com.example.juschedule.pojo.Courses
import com.example.juschedule.pojo.Division
import com.example.juschedule.pojo.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import timber.log.Timber


class ScheduleActivity : AppCompatActivity(), ScheduleAdapter.clickOnSchedule,
    DoctorAdapter.ClickOnDoctor {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: user
    lateinit var ScheduleList: ArrayList<Courses>
    lateinit var facultyList: ArrayList<Courses>
    lateinit var compulsoryList: ArrayList<Courses>
    lateinit var FinishedList: ArrayList<Courses>
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var nineCredits: LinearLayout
    private lateinit var twelveCredits: LinearLayout
    private lateinit var fifteenCredits: LinearLayout
    private lateinit var replace: Button
    private lateinit var nine: TextView
    private lateinit var twelve: TextView
    private lateinit var fifteen: TextView
    private lateinit var textView: TextView
    private lateinit var teacher: TextView


    private lateinit var ElectiveList: ArrayList<Courses>
    private lateinit var PrerequisiteList: ArrayList<Courses>
    private lateinit var opendList: ArrayList<Courses>
    private lateinit var FirstYearList: ArrayList<Courses>
    private lateinit var YearList: ArrayList<Courses>
    private lateinit var mathList: ArrayList<Courses>
    private lateinit var codeList: ArrayList<Courses>
    private lateinit var MemorizeList: ArrayList<Courses>
    private lateinit var BusinessList: ArrayList<Courses>
    private lateinit var AllCoursesList: ArrayList<Courses>
    private lateinit var chooseList: ArrayList<Courses>
    private lateinit var FinalList: ArrayList<Courses>
    private lateinit var ProjectList: ArrayList<Courses>
    private lateinit var TrainingList: ArrayList<Courses>
    private lateinit var availableElective: ArrayList<Courses>
    private lateinit var doctorsList: ArrayList<Division>
    private lateinit var divisionlist: ArrayList<Division>


    var intent1: Intent?=null
    var courseName=""
    var num: Int?=null
    var isFinished=false
    var isCalled=false
    var h=0


    lateinit var listener: ScheduleAdapter.clickOnSchedule

    lateinit var doctorRecyclerView: RecyclerView
    lateinit var ScheduleRecyclerview: RecyclerView

    private lateinit var dialog: Dialog
    private lateinit var alertDialog: Dialog


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        listener=this
        num=0
        user=user()
        intent1=Intent(this, DoctorsActivity::class.java)

        auth=FirebaseAuth.getInstance()
        uid=auth.currentUser?.uid.toString()

        ScheduleRecyclerview=findViewById(R.id.recyclerview)
        ScheduleRecyclerview.layoutManager=LinearLayoutManager(this)
        ScheduleRecyclerview.setHasFixedSize(true)



        ScheduleList=arrayListOf<Courses>()
        facultyList=arrayListOf<Courses>()
        compulsoryList=arrayListOf<Courses>()
        ElectiveList=arrayListOf<Courses>()
        FinishedList=arrayListOf<Courses>()
        PrerequisiteList=arrayListOf<Courses>()
        opendList=arrayListOf<Courses>()
        FirstYearList=arrayListOf<Courses>()
        YearList=arrayListOf()
        mathList=ArrayList()
        codeList=ArrayList()
        MemorizeList=ArrayList()
        BusinessList=ArrayList()
        AllCoursesList=arrayListOf<Courses>()
        chooseList=arrayListOf<Courses>()
        FinalList=arrayListOf<Courses>()
        ProjectList=arrayListOf()
        TrainingList=arrayListOf()
        availableElective=ArrayList()
        doctorsList=arrayListOf()
        divisionlist=arrayListOf<Division>()


        val view=layoutInflater.inflate(R.layout.schedule_item, null)
        replace=view.findViewById(R.id.replace)

        nineCredits=findViewById(R.id.nineCredits)
        twelveCredits=findViewById(R.id.twelveCredits)
        fifteenCredits=findViewById(R.id.fifteenCredits)
        nine=findViewById(R.id.nine)
        twelve=findViewById(R.id.twelve)
        fifteen=findViewById(R.id.fifteen)
        textView=findViewById(R.id.textView)

        val hh=intent.getIntExtra("creditNum", h)
        apply {
            h=hh

        }
        if (h == 9) {
            nineCredits.setBackgroundColor(resources.getColor(R.color.primaryDark))
            nine.setTextColor(resources.getColor(R.color.white))
            twelveCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
            fifteenCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
            twelve.setTextColor(resources.getColor(R.color.primaryDark))
            fifteen.setTextColor(resources.getColor(R.color.primaryDark))
        } else if (h == 12) {
            twelveCredits.setBackgroundColor(resources.getColor(R.color.primaryDark))
            twelve.setTextColor(resources.getColor(R.color.white))

            nineCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
            fifteenCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))

            nine.setTextColor(resources.getColor(R.color.primaryDark))
            fifteen.setTextColor(resources.getColor(R.color.primaryDark))
        } else if (h == 15) {
            fifteenCredits.setBackgroundColor(resources.getColor(R.color.primaryDark))
            fifteen.setTextColor(resources.getColor(R.color.white))

            nineCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
            twelveCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))

            nine.setTextColor(resources.getColor(R.color.primaryDark))
            twelve.setTextColor(resources.getColor(R.color.primaryDark))
        }
        click()

        getScheduledata()

        var arrowBack=findViewById<LinearLayout>(R.id.arrow_back)

        arrowBack.setOnClickListener {
            finish()
        }


        databaseReference=FirebaseDatabase.getInstance().getReference("divisons")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (courssnapshot in snapshot.children) {
                        val doctors=courssnapshot.getValue(Division::class.java)
                        doctorsList.add(doctors!!)

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
            }

        })
    }


    private fun getScheduledata() {
        showProgressBar()
        databaseReference=FirebaseDatabase.getInstance().getReference("courses")
            .child("FacultyRequirements")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (courssnapshot in snapshot.children) {
                        val userCourses=courssnapshot.getValue(Courses::class.java)
                        facultyList.add(userCourses!!)
                    }
                }
                hideProgressBar()
            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
            }
        })

        databaseReference=FirebaseDatabase.getInstance().getReference("courses")
            .child("CompulsorySpecialization")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (courssnapshot in snapshot.children) {
                        val course=courssnapshot.getValue(Courses::class.java)
                        compulsoryList.add(course!!)
                    }
                    for (CompulsorySpecialization in compulsoryList) {
                    }
                }
                hideProgressBar()

            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
            }
        })
        databaseReference=FirebaseDatabase.getInstance().getReference("userCourses")
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (courssnapshot in snapshot.children) {
                        val course=courssnapshot.getValue(Courses::class.java)
                        FinishedList.add(course!!)
                    }
                }
                hideProgressBar()
            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
            }
        })
        databaseReference=FirebaseDatabase.getInstance().getReference("courses")
            .child("SpecializationRequirements")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (courssnapshot in snapshot.children) {
                        val course=courssnapshot.getValue(Courses::class.java)
                        ElectiveList.add(course!!)
                    }

                }


                var numOfFinishedElective=0



                for (item in FinishedList) {
                    for (elective in ElectiveList) {
                        if (item.coursesName == elective.coursesName) {
                            numOfFinishedElective=numOfFinishedElective + 1
                        }
                    }
                }

                for (i in 0..3 - numOfFinishedElective) {

                    availableElective.add(ElectiveList[i])
                }
                hideProgressBar()
                ScheduleList=
                    (facultyList + compulsoryList + availableElective) as ArrayList<Courses>



                AllCoursesList=ScheduleList.minus(FinishedList) as ArrayList<Courses>
                var numOfchooseList=0
                var numOfFinishedList=0


                for (item in AllCoursesList) {
                    numOfchooseList=numOfchooseList + Integer.valueOf(item.hourNum)
                }

                for (item in FinishedList) {
                    numOfFinishedList=numOfFinishedList + Integer.valueOf(item.hourNum)
                }

                if (numOfchooseList <= 18) {
                    nineCredits.visibility=GONE
                    twelveCredits.visibility=GONE
                    fifteenCredits.visibility=GONE
                    replace.visibility=GONE
                    textView.visibility=GONE
                    Toast.makeText(applicationContext.getApplicationContext(),
                        "Congratulation this is your last semester",
                        Toast.LENGTH_LONG).show();
                    ScheduleRecyclerview.adapter=
                        ScheduleAdapter(AllCoursesList as ArrayList, listener)

                } else {
                    remainingCourses(ScheduleList)
                }
                Timber.e("numOfFinishedList  $numOfFinishedList")
                databaseReference=FirebaseDatabase.getInstance().getReference("users")
                databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                        user=dataSnapshot.getValue(user::class.java)!!
                        num=Integer.valueOf(user.ufinished)
                        if ((Integer.valueOf(user.ucurrent) + Integer.valueOf(user.ufinished)) <= 3) {
                            for (item in ScheduleList) {
                                when (item.coursesName) {
                                    "Discrete Mathematics" -> FirstYearList.add(item)
                                    "Fundamentals of Information Technology" -> FirstYearList.add(
                                        item)
                                    "Calculus I" -> FirstYearList.add(item)

                                }
                            }
                            nineCredits.visibility=GONE
                            twelveCredits.visibility=GONE
                            fifteenCredits.visibility=GONE
                            replace.visibility=GONE
                            textView.visibility=GONE

                            ScheduleRecyclerview.adapter=
                                ScheduleAdapter(FirstYearList as ArrayList, listener)


                        } else {
                            remainingCourses(ScheduleList)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        hideProgressBar()
                    }

                })

            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
            }
        })

    }


    private fun remainingCourses(scheduleList: ArrayList<Courses>) {

        ScheduleList=scheduleList.minus(FinishedList) as ArrayList<Courses>
        getOpendCourses(ScheduleList)

    }

    private fun getOpendCourses(ScheduleList: ArrayList<Courses>) {

        for (ScheduleItem in ScheduleList) {
            if (ScheduleItem.Prerequisite == "") {
                opendList.add(ScheduleItem)
            }
            if (ScheduleItem.Prerequisite != "") {
                for (FinishedItem in FinishedList) {
                    if (ScheduleItem.Prerequisite.equals(FinishedItem.coursesName)) {
                        opendList.add(ScheduleItem)
                    }
                }
            }

            if (ScheduleItem.Prerequisite == "90h") {
                databaseReference=FirebaseDatabase.getInstance().getReference("users")
                databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                        user=dataSnapshot.getValue(user::class.java)!!
                        num=Integer.valueOf(user.ufinished)
                        if (num!! > 90) {
                            opendList.add(ScheduleItem)
                        }
                        isFinished=true
                        if (!isCalled) {
                            getSchedule(opendList)
                            isCalled=false
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        hideProgressBar()
                    }

                })
            }
        }
        if (isFinished) {
            getSchedule(opendList)
        }
    }


    private fun getSchedule(OpendList: ArrayList<Courses>) {
        isCalled=true

        var yearArray=arrayOf("First Year", "Second Year", "Third Year", "Fourth Year")

        for (year in yearArray) {
            for (opendItem in OpendList) {
                if (creditcount(FinalList) < h - 3) {
                    if (opendItem.year == year) {
                        YearList.add(opendItem)
                    }
                }
            }
        }
        if (h != 0) {
            FinalList=getCourseByType(YearList, h)

            opendList=opendList.minus(FinalList.distinct()) as ArrayList<Courses>

        }
    }

    private fun click() {
        val color=resources.getColor(R.color.primaryDark)

        nineCredits.setOnClickListener {
            nineCredits.setBackgroundColor(resources.getColor(R.color.primaryDark))
            nine.setTextColor(resources.getColor(R.color.white))
            twelveCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
            fifteenCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
            twelve.setTextColor(resources.getColor(R.color.primaryDark))
            fifteen.setTextColor(resources.getColor(R.color.primaryDark))
            h=9
            val intent=Intent(this, ScheduleActivity::class.java).also {
                it.putExtra("creditNum", h)
                startActivity(it)
                finish()
            }
            getScheduledata()
        }
        twelveCredits.setOnClickListener {

            twelveCredits.setBackgroundColor(color)
            twelve.setTextColor(resources.getColor(R.color.white))

            nineCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
            fifteenCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))

            nine.setTextColor(resources.getColor(R.color.primaryDark))
            fifteen.setTextColor(resources.getColor(R.color.primaryDark))

            h=12
            val intent=Intent(this, ScheduleActivity::class.java).also {
                it.putExtra("creditNum", h)
                startActivity(it)
                finish()
            }
            getScheduledata()
        }
        fifteenCredits.setOnClickListener {

            fifteenCredits.setBackgroundColor(color)
            fifteen.setTextColor(resources.getColor(R.color.white))

            nineCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
            twelveCredits.setBackgroundDrawable(resources.getDrawable(R.drawable.border))

            nine.setTextColor(resources.getColor(R.color.primaryDark))
            twelve.setTextColor(resources.getColor(R.color.primaryDark))

            h=15
            val intent=Intent(this, ScheduleActivity::class.java).also {
                it.putExtra("creditNum", h)
                startActivity(it)
                finish()
            }
            getScheduledata()
        }
    }

    private fun creditcount(finalList: ArrayList<Courses>): Int {
        var creditSum=0
        for (finalItem in finalList) {
            creditSum=creditSum.plus(Integer.valueOf(finalItem.hourNum))
        }
        return creditSum
    }

    private fun getCourseByType(YearList: ArrayList<Courses>, ss: Int): ArrayList<Courses> {

        for (yearItem in YearList) {
            when (yearItem.CourseType) {
                "Project" -> {
                    ProjectList.add(yearItem)
                }
                "Training" -> {
                    TrainingList.add(yearItem)
                }
                "Math" -> {
                    mathList.add(yearItem)
                }
                "Code" -> {
                    codeList.add(yearItem)
                }
                "Memorize" -> {
                    MemorizeList.add(yearItem)
                }
                "Business" -> {
                    BusinessList.add(yearItem)
                }
            }
        }

        var arrayh=arrayOf(ProjectList, TrainingList, mathList, codeList, MemorizeList)
        var e=0
        for (i in 0..h - 3 step 3) {
            for (type in arrayh) {
                if (creditcount(chooseList) <= h - 3)
                    if (type.isNotEmpty()) {
                        if ((type.size) - 1 > e) {
                            if (type[e] != null) {
                                chooseList.add(type[e])
                            }
                        }
                    }
            }
            e++
        }

        ScheduleRecyclerview.adapter=
            ScheduleAdapter(chooseList.distinct() as ArrayList, listener)

        return chooseList

    }


    fun showProgressBar() {
        dialog=Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun hideProgressBar() {
        dialog.dismiss()
    }

    override fun setOnClickReplace(item: Courses, position: Int) {
        if (user.status == "expected to graduate") {
            Toast.makeText(applicationContext.getApplicationContext(),
                "There is no another course",
                Toast.LENGTH_SHORT).show()
        } else if (FinishedList.size <= 3) {
            Toast.makeText(applicationContext.getApplicationContext(),
                "There is no another course",
                Toast.LENGTH_SHORT).show()
        } else {
            chooseList.removeAt(position)
            ScheduleRecyclerview.adapter!!.notifyItemRemoved(position)

            var inserted=false

            do {
                var course=checkIfInList()
                if (course != null) {

                    chooseList.add(position, course)
                    when (h) {
                        9 -> {
                            if (chooseList.size >= 4) {
                                chooseList.removeLast()
                            }
                        }
                        12 -> {
                            if (chooseList.size >= 5) {
                                chooseList.removeLast()
                            }

                        }
                        15 -> {
                            if (chooseList.size >= 6) {
                                chooseList.removeLast()
                            }

                        }
                    }
                    if (chooseList.size == 0) {

                        Toast.makeText(applicationContext.getApplicationContext(),
                            "There is no another course",
                            Toast.LENGTH_SHORT).show()
                    }

                    for (chosec in chooseList) {
                        if (chosec.coursesName == course.coursesName) {
                        }
                    }
                    ScheduleRecyclerview.adapter=
                        ScheduleAdapter(chooseList.distinct() as java.util.ArrayList<Courses>,
                            listener)
                    inserted=true
                } else {
                    checkIfInList()
                }
            } while (!inserted)
        }

    }


    override fun setonClickCard(item: Courses, position: Int) {
        val alertBuilder=AlertDialog.Builder(this)
        val inf=layoutInflater

        val view=inf.inflate(R.layout.activity_doctors, null)

        alertBuilder.setView(view)
        doctorRecyclerView=view.findViewById(R.id.doctorRecyclerView)
        doctorRecyclerView.layoutManager=LinearLayoutManager(this)
        doctorRecyclerView.setHasFixedSize(true)

        alertDialog=alertBuilder.create()
        alertDialog.show()
        val course_name: TextView=view.findViewById(R.id.course_name)
        divisionlist.clear()
        for (doctorsItem in doctorsList) {
            if (item.coursesName == doctorsItem.coursesName) {
                divisionlist.add(doctorsItem)
                course_name.setText(doctorsItem.coursesName)
            }
        }
        doctorRecyclerView.adapter=
            DoctorAdapter(divisionlist, this)
        hideProgressBar()


    }

    private fun checkIfInList(): Courses? {

        val opendCourse=opendList.random()
        var notInList=true

        for (scheduleItem in chooseList) {
            if (opendCourse.coursesName == scheduleItem.coursesName) {
                notInList=false
            }
        }
        if (notInList) {
            return opendCourse
        } else {
            return null
        }
    }

    override fun setOnClickCheck(doctor: Division, position: Int) {

        var timeExist=false
        var numOfFinishedList=0

        for (item in FinishedList) {
            numOfFinishedList=numOfFinishedList + Integer.valueOf(item.hourNum)
        }
        if (user.status == "expected to graduate") {

            for (item in AllCoursesList) {
                if (doctor.time == item.time) {
                    timeExist=true
                    courseName=item.coursesName.toString()

                }
            }
            if (!timeExist) {

                for (item in AllCoursesList) {
                    if (item.coursesName == doctor.coursesName) {
                        item.time=doctor.time.toString()
                        item.today=doctor.days.toString()
                        item.teacher=doctor.teacher.toString()
                        item.Division=doctor.Division.toString()

                        Toast.makeText(this, "Course booked successfully", Toast.LENGTH_SHORT)
                            .show()
                        alertDialog.dismiss()
                    }
                }
                ScheduleRecyclerview.adapter=
                    ScheduleAdapter(AllCoursesList, listener)
                ScheduleRecyclerview.adapter!!.notifyDataSetChanged()

            } else {

                Toast.makeText(this,
                    "you have booked the course '$courseName' at this time",
                    Toast.LENGTH_LONG)
                    .show()
            }


        } else if (numOfFinishedList <= 3) {
            Timber.e("ON click check")
            for (item in ScheduleList) {
                when (item.coursesName) {
                    "Discrete Mathematics" -> FirstYearList.add(item)
                    "Fundamentals of Information Technology" -> FirstYearList.add(item)
                    "Calculus I" -> FirstYearList.add(item)

                }
            }
            nineCredits.visibility=GONE
            twelveCredits.visibility=GONE
            fifteenCredits.visibility=GONE
            replace.visibility=GONE
            textView.visibility=GONE
            for (item in FirstYearList) {
                if (doctor.time == item.time) {
                    timeExist=true
                    courseName=item.coursesName.toString()
                }
            }
            if (!timeExist) {

                for (item in FirstYearList) {
                    if (item.coursesName == doctor.coursesName) {
                        item.time=doctor.time.toString()
                        item.today=doctor.days.toString()
                        item.teacher=doctor.teacher.toString()
                        item.Division=doctor.Division.toString()

                        Toast.makeText(this, "Course booked successfully", Toast.LENGTH_SHORT)
                            .show()
                        alertDialog.dismiss()

                    }
                }
                ScheduleRecyclerview.adapter=
                    ScheduleAdapter(FirstYearList.distinct() as java.util.ArrayList<Courses>,
                        listener)
                ScheduleRecyclerview.adapter!!.notifyDataSetChanged()

            } else {

                Toast.makeText(this,
                    "you have booked the course '$courseName' at this time",
                    Toast.LENGTH_LONG)
                    .show()
            }


        } else {
            for (item in chooseList) {
                if (doctor.time == item.time) {
                    timeExist=true
                    courseName=item.coursesName.toString()

                }
            }
            if (!timeExist) {

                for (item in chooseList) {
                    if (item.coursesName == doctor.coursesName) {
                        item.time=doctor.time.toString()
                        item.today=doctor.days.toString()
                        item.teacher=doctor.teacher.toString()
                        item.Division=doctor.Division.toString()

                        Toast.makeText(this, "Course booked successfully", Toast.LENGTH_SHORT)
                            .show()
                        alertDialog.dismiss()
                    }
                }
                ScheduleRecyclerview.adapter=
                    ScheduleAdapter(chooseList, listener)
                ScheduleRecyclerview.adapter!!.notifyDataSetChanged()

            } else {
                Toast.makeText(this,
                    "you have booked the course '$courseName' at this time",
                    Toast.LENGTH_LONG)
                    .show()
            }

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}





