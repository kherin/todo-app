import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.format.Time
import android.util.Log
import android.util.TimeUtils
import android.view.*
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.kherindo.R
import com.example.kherindo.entities.Task
import com.example.kherindo.respositories.TaskRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.lang.Exception
import java.util.*
import java.util.Calendar.HOUR


class NewTodoDialogFragment(
) : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    lateinit var titleFieldInput: TextInputEditText
    lateinit var notesFieldInput: TextInputEditText
    lateinit var taskRepository: TaskRepository

    var reminderHour: Int = 0
    var reminderMin: Int = 0
    var reminderYear: Int = 0
    var reminderMonth: Int = 0
    var reminderDay: Int = 0

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_new_todo_dialog, container, false)
    }

    override fun onStart() {
        taskRepository = TaskRepository()
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }


    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        reminderHour = hourOfDay
        reminderMin = minute
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        reminderYear = year
        reminderMonth = monthOfYear
        reminderDay = dayOfMonth
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleFieldInput = view.findViewById(R.id.title_textInputEditText)
        notesFieldInput = view.findViewById(R.id.notes_textInputEditText)

        // clicks date button
        view.findViewById<MaterialButton>(R.id.select_date_button).setOnClickListener {
            Log.d("datepicker", "button clicked")
            val now: Calendar = Calendar.getInstance()
            val dialog: DatePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show(childFragmentManager, "DatePickerDialog")
        }

        // clicks time button
        view.findViewById<MaterialButton>(R.id.select_time_button).setOnClickListener {
            Log.d("timepicker", "button clicked")
            val now: Calendar = Calendar.getInstance()
            val dialog: TimePickerDialog = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true
            )
            dialog.show(childFragmentManager, "TimePickerDialog")
        }

        // clicks add button
        view.findViewById<Button>(R.id.button_new_task).setOnClickListener {
            if (validateTitleFieldInput(view)) {
                titleFieldInput.error = null

                // collect values from input
                val title: String = titleFieldInput.text.toString()
                val notes: String = notesFieldInput.text.toString()

                Log.d(
                    "new task",
                    "title: $title notes: $notes reminder Hour: $reminderHour reminder min: $reminderMin reminder Year: $reminderYear reminder month: $reminderMonth reminderDay: $reminderDay"
                )

                val reminderDate: Date = instantiateCalendar(
                    reminderMin,
                    reminderHour,
                    reminderDay,
                    reminderMonth,
                    reminderYear
                )

                // get current user id
                val currentUserId: String = FirebaseAuth.getInstance().currentUser.uid

                // instantiate task object
                val newTask = Task(
                    uid = UUID.randomUUID().toString(),
                    userId = currentUserId,
                    title = title,
                    notes = notes,
                    reminderDate = reminderDate.toString()
                )

                val createTaskSuccessful = createTask(currentUserId, newTask)
                var createTaskMessage = "Task created !"

                if (!createTaskSuccessful) {
                  createTaskMessage = "Could not create Task"
                }
                Snackbar.make(requireView().rootView, createTaskMessage, Snackbar.LENGTH_SHORT).show()
                dialog?.dismiss()

            } else {
                titleFieldInput.error = "Title cannot be empty"
            }
        }

        // clicks cancel button
        view.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog?.dismiss()
        }

    }

    private fun instantiateCalendar(minute: Int, hour: Int, day: Int, month: Int, year: Int): Date {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        return calendar.time
    }

    private fun validateTitleFieldInput(view: View): Boolean {
        val value: String = titleFieldInput.text.toString()
        return value.isNotBlank()
    }

    private fun createTask(userId: String, newTask: Task): Boolean {
        return try {
            taskRepository.createTask(userId, newTask)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}