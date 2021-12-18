package com.tvmedicine



import RecyclerItemClickListener
import android.content.DialogInterface
import android.content.SharedPreferences
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.tvmedicine.RetrifitService.Common
import com.tvmedicine.models.TreatmentModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class TreatmentActivity : AppCompatActivity() {
    private lateinit var listener: OnBottomSheetCallbacks

    //val output = File(getExternalFilesDir(null), "/recording.mp3")
    var mediaRecorder = MediaRecorder()


    val data: Array<Array<String?>> = Array(10) { Array(3) { "" } }
    val symptomsArray: Array<Array<String?>> = Array(6) { Array(2) { "" } }
    private var viewSize: Int = 0
    var startDate: String? = ""
    var patientSurename: String? = ""
    var doctorSurename: String? = ""
    var spinner: Spinner? = null
    var position = 0
    lateinit var sPref: SharedPreferences
    lateinit var indicator: LinearProgressIndicator
    lateinit var recyclerView: RecyclerView
    val scope = CoroutineScope(Dispatchers.Main + Job())
    val result: List<TreatmentModel?>? = null
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)

    }

    /**Метод для запроса через Корутину*/
    private fun patientRequest(): List<TreatmentModel?> {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.getTreatmentByUser("getTreatment.php", sPref.getString("login", ""))
        println(sPref.getString("login", ""))
        val result = call?.execute()?.body()
        //viewSize = result!!.size
        println(result!![0]?.patientId)
        return result
    }

    private fun addConclusionRequest(position: Int, concText: String?): Boolean {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.addConclusion(
            "addConclusion.php",
            position,
            concText,
            sPref.getString("login", "")
        )
        call?.execute()?.body()
        return call!!.isExecuted
    }

    private fun getSymptomsForAlertRequest(position: Int): String? {
        val mService = Common.retrofitService
        val call = mService.getSymptomsForUser("getSymptoms.php", position)
        val result = call?.execute()?.body()
        return result?.get(0)?.symptomsName
    }

    private fun deleteTreatmentRequest(position: Int): Boolean {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call =
            mService.deleteTreatment("deleteTreatment.php", position, sPref.getString("login", ""))
        call?.execute()?.body()
        return call!!.isExecuted
    }

    private fun treatmentAdding(symptomsId: Int, soundServerLinkId: Int): Boolean {
        val mService = Common.retrofitService
        val sPref = getSharedPreferences("User", MODE_PRIVATE)
        val call = mService.addTreatment(
            "addTreatment.php",
            sPref.getString("login", ""),
            Calendar.getInstance().time.toString("yyyy/MM/dd HH:mm:ss"),
            symptomsId,
            soundServerLinkId
        )
        val result = call?.execute()?.body()
        return result?.get(0)?.response != "false"
    }

    private fun symptomsRequest(): ArrayAdapter<String> {
        val mService = Common.retrofitService
        val call = mService.getAllSymptoms("getSymptoms.php")
        val result = call?.execute()?.body()
        val symptoms: Array<String?> = arrayOf(
            result?.get(0)?.symptomsName,
            result?.get(1)?.symptomsName,
            result?.get(2)?.symptomsName,
            result?.get(3)?.symptomsName,
            result?.get(4)?.symptomsName,
            result?.get(5)?.symptomsName
        )
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, symptoms)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        return adapter
    }

//    private fun getPatientName(result: List<TreatmentModel?>?) {
//        val mService = Common.retrofitService
//        val call = mService.getPatientFromId("getPatient.php", result?.get(0)?.patientId)
//        val result2 = call?.execute()?.body()
//        patientSurename = result2?.get(0)?.surename
//    }

//    private fun getDoctorName(result: List<TreatmentModel?>?) {
//        val mService = Common.retrofitService
//        val call = mService.getPatientFromId("getDoctor.php", result?.get(0)?.doctorId)
//        val result3 = call?.execute()?.body()
//        doctorSurename = result3?.get(0)?.surename
//    }

    private fun doctorRequest(): List<TreatmentModel?>? {
        val mService = Common.retrofitService
        val call = mService.getAllTreatment("getTreatment.php")
        val result = call?.execute()?.body()
        //viewSize = result!!.size
        return result
    }

    private fun getPatientNameForDoctor(patientId: Int?) {
        val mService = Common.retrofitService
        val call = mService.getPatientFromId("getPatient.php", patientId)
        val result2 = call?.execute()?.body()
        patientSurename = result2?.get(0)?.surename
    }

    private fun getDoctorNameForDoctor(doctorId: Int?) {
        if (doctorId == 0) {
            return
        }
        val mService = Common.retrofitService
        val call = mService.getDoctorFromId("getDoctor.php", doctorId)
        val result3 = call?.execute()?.body()
        doctorSurename = result3?.get(0)?.surename
    }

//    private fun startRecording() {
//        try {
//            mediaRecorder.prepare()
//            mediaRecorder.start()
//            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
//        } catch (e: IllegalStateException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

    fun setOnBottomSheetCallbacks(onBottomSheetCallbacks: OnBottomSheetCallbacks) {
        this.listener = onBottomSheetCallbacks
    }

    fun closeBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun openBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    private fun configureBackdrop() {
        val fragment = supportFragmentManager.findFragmentById(R.id.filter_fragment)

        fragment?.view?.let {
            BottomSheetBehavior.from(it).let { bs ->
                bs.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {/*Тут должно быть пусто*/}

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        listener.onStateChanged(bottomSheet, newState)
                    }
                })

                bs.state = BottomSheetBehavior.STATE_EXPANDED
                mBottomSheetBehavior = bs
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)
        supportActionBar?.elevation = 0f
        configureBackdrop()
        //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        //mediaRecorder.setOutputFile(output)
        sPref = getSharedPreferences("User", MODE_PRIVATE)
        indicator = findViewById(R.id.ProgressIndicator)
        indicator.showAnimationBehavior = BaseProgressIndicator.SHOW_OUTWARD
        indicator.hideAnimationBehavior = BaseProgressIndicator.HIDE_OUTWARD
        indicator.show()
        recyclerView = findViewById(R.id.rv_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val li: LayoutInflater = LayoutInflater.from(this)
        val alertView: View = li.inflate(R.layout.alert, null)
        val loadingView: View = li.inflate(R.layout.loading, null)
        val addView: View = li.inflate(R.layout.add_layout, null)
        val addConclusion: View = li.inflate(R.layout.conclusion, null)
        val deleteAlert: View = li.inflate(R.layout.delete_alert, null)
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val mDialogBuilder2: AlertDialog.Builder = AlertDialog.Builder(this)
        val mDialogBuilder3: AlertDialog.Builder = AlertDialog.Builder(this)
        val mDialogBuilder4: AlertDialog.Builder = AlertDialog.Builder(this)
        val mDialogBuilder5: AlertDialog.Builder = AlertDialog.Builder(this)
        val fab1 = findViewById<FloatingActionButton>(R.id.out_btn)
        val spinner = addView.findViewById<View>(R.id.symptoms_spinner) as Spinner
        val fab2 = findViewById<FloatingActionButton>(R.id.add_btn)
        //val fab3 = findViewById<FloatingActionButton>(R.id.fab3)
        //lateinit var firstButtonListener:View.OnClickListener
        var rep = false
        mDialogBuilder.setView(alertView)
        mDialogBuilder2.setView(loadingView)
        mDialogBuilder3.setView(addView)
        mDialogBuilder4.setView(addConclusion)
        mDialogBuilder5.setView(deleteAlert)
        load(sPref, scope, result, recyclerView, indicator)
        fab1.setOnClickListener {
            val sPref = getSharedPreferences("User", MODE_PRIVATE)
            val ed: SharedPreferences.Editor = sPref.edit()
            ed.clear()
            ed.apply()
            finish()
        }

//        val secondButtonListener:View.OnClickListener = View.OnClickListener() {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                ActivityCompat.requestPermissions(this, permissions,0)
//            } else {
//                stopRecording()
//                fab3.setOnClickListener(fisrtButtonListener)
//            }
//        }
//        firstButtonListener = View.OnClickListener() {
//            if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                ActivityCompat.requestPermissions(this, permissions,0)
//            } else {
//                startRecording()
//                fab3.setOnClickListener(secondButtonListener)
//            }
//        }

        //fab3.setOnClickListener(firstButtonListener)
        if (sPref.getString("user_type", "") == "doctor") {
            fab2.visibility = View.GONE
        }
        fab2.setOnClickListener {
            var adapter: ArrayAdapter<String>? = null
            scope.launch {
                val def = scope.asyncIO { adapter = symptomsRequest() }
                def.await()
                spinner.adapter = adapter
            }
            mDialogBuilder3
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.add_treatment)) { _: DialogInterface, _: Int ->
                        scope.launch {

                            val def = scope.asyncIO { treatmentAdding(spinner.selectedItemPosition + 1, soundServerLinkId = 1) }
                            def.await()
                            load(sPref, scope, result, recyclerView, indicator)
                        }

                    }
                    .setNegativeButton(getString(R.string.cancel_btn)) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.cancel()
                    }

           val alert3 = mDialogBuilder3.create()
            alert3.show()

        }
        mDialogBuilder4
               .setCancelable(false)
               .setPositiveButton(getString(R.string.add_conclusion)) { _: DialogInterface, _: Int ->

                       scope.launch {
                           val m: EditText = addConclusion.findViewById(R.id.conclusion_add_text_field)

                           if (sPref.getString("user_type", "") == "doctor") {
                           val def = scope.asyncIO { rep = addConclusionRequest(position,m.text.toString()) }
                           def.await()
                           if (rep) {
                               val toast = Toast.makeText(
                                       applicationContext,
                                       getString(R.string.good_add),
                                       Toast.LENGTH_SHORT
                               )
                               toast.show()
                               load(sPref, scope, result, recyclerView, indicator)
                           }
                       }
                   }
               }
               .setNegativeButton(getString(R.string.cancel_btn)) { dialogInterface: DialogInterface, _: Int ->
                   dialogInterface.cancel()
               }
        mDialogBuilder5
                .setCancelable(false)
                .setPositiveButton(getString(R.string.delete_string)) { _: DialogInterface, _: Int ->
                    if (sPref.getString("user_type", "") == "patient") {
                        scope.launch {
                            addConclusion.findViewById<EditText>(R.id.conclusion_add_text_field)
                            val def = scope.asyncIO { rep = deleteTreatmentRequest(position) }
                            def.await()
                            if (rep) {
                                val toast = Toast.makeText(
                                        applicationContext,
                                        "Удалено",
                                        Toast.LENGTH_SHORT
                                )
                                toast.show()
                                load(sPref, scope, result, recyclerView, indicator)
                            }
                        }
                    }
                }
                .setNegativeButton(getString(R.string.cancel_btn)) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.cancel()
                }
       val alert2 = mDialogBuilder4.create()
        val alert1 = mDialogBuilder5.create()
        val recyclerView = findViewById<RecyclerView>(R.id.rv_view)
        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(this, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        this@TreatmentActivity.position = position+1
                        if(sPref.getString("user_type","")=="doctor") {
                            loadSymptomsName(addConclusion)
                           alert2.show()
                        }
                    }
                    override fun onLongItemClick(view: View?, position: Int) {
                        this@TreatmentActivity.position = position
                        if(sPref.getString("user_type","")=="patient") {
                            loadSymptomsName(addConclusion)
                           alert1.show()
                        }
                    }
                })
        )
    }

    private fun loadSymptomsName(addConclusion: View) {
        val m2: TextView = addConclusion.findViewById(R.id.symptoms_field)
        scope.launch {
            var symptoms: String? = ""
            val def2 = scope.asyncIO { symptoms = getSymptomsForAlertRequest(position) }
            def2.await()
            m2.text = symptoms
        }
    }

    private fun load(sPref: SharedPreferences, scope: CoroutineScope, result: List<TreatmentModel?>?, recyclerView: RecyclerView, indicator: LinearProgressIndicator) {
        var result1 = result
        indicator.show()
        if (sPref.getString("user_type", "") == "patient") {
            scope.launch {
                val def = scope.asyncIO { result1 = patientRequest() }
                //result1 = patientRequest()
                def.await()
                println(result1)
                viewSize = result1!!.size
                println(viewSize)
                for (i in 0 until viewSize) {
                    val def1 = scope.asyncIO { getPatientNameForDoctor(result1?.get(i)?.patientId) }
                    def1.await()
                    val def2 = scope.asyncIO { getDoctorNameForDoctor(result1?.get(i)?.doctorId) }
                    def2.await()
                    startDate = result1?.get(i)?.startDate
                    if(doctorSurename==""){
                        doctorSurename = getString(R.string.doctor_assign)
                    }
                    data[i][0] = patientSurename
                    data[i][1] = doctorSurename
                    data[i][2] = startDate
                    recyclerView.adapter = RvAdapter(data, viewSize)
                    println(i)
                    recyclerView.adapter?.notifyDataSetChanged()
                    patientSurename = ""
                    doctorSurename = ""
                    startDate = ""
                }
                indicator.hide()
            }
        }
        if (sPref.getString("user_type", "") == "doctor") {
            scope.launch {
                val def = scope.asyncIO { result1 = doctorRequest() }
                def.await()
                viewSize = result1!!.size
                println(viewSize)
                for (i in 0 until viewSize) {
                    val def1 = scope.asyncIO { getPatientNameForDoctor(result1?.get(i)?.patientId) }
                    def1.await()
                    val def2 = scope.asyncIO { getDoctorNameForDoctor(result1?.get(i)?.doctorId) }
                    def2.await()
                    startDate = result1?.get(i)?.startDate
                    if(doctorSurename==""){
                        doctorSurename = getString(R.string.doctor_assign)
                    }
                    data[i][0] = patientSurename
                    data[i][1] = doctorSurename
                    data[i][2] = startDate
                    recyclerView.adapter = RvAdapter(data, viewSize)
                    println(i)
                    recyclerView.adapter?.notifyDataSetChanged()
                    patientSurename = ""
                    doctorSurename = ""
                    startDate = ""
                }
                indicator.hide()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Job().cancel()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_button -> load(sPref, scope, result, recyclerView, indicator)
        }
        return true
    }

}


