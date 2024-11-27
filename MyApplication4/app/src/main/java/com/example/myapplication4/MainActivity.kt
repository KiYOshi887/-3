package com.example.myapplication4

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val notes = mutableListOf<Note>()
    private val archivedNotes = mutableListOf<Note>()
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val searchView = findViewById<SearchView>(R.id.searchView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Настройка Spinner для выбора категории
        val categories = listOf("Работа", "Дом", "Личное")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter

        // Настройка RecyclerView
        adapter = NotesAdapter(notes, ::archiveNote)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Добавление новой заметки
        btnAdd.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()
            val category = spinnerCategory.selectedItem.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            } else {
                notes.add(Note(title, content, category, System.currentTimeMillis()))
                notes.sortByDescending { it.dateCreated } // Сортировка по дате
                adapter.notifyDataSetChanged()
                etTitle.text.clear()
                etContent.text.clear()
                Toast.makeText(this, "Заметка добавлена!", Toast.LENGTH_SHORT).show()
            }
        }

        // Поиск по заметкам
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredNotes = notes.filter { it.title.contains(newText ?: "", ignoreCase = true) }
                adapter.updateNotes(filteredNotes)
                return true
            }
        })
    }

    private fun archiveNote(note: Note) {
        notes.remove(note)
        archivedNotes.add(note)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Заметка архивирована", Toast.LENGTH_SHORT).show()
    }
}

data class Note(
    val title: String,
    val content: String,
    val category: String,
    val dateCreated: Long
)

class NotesAdapter(
    private var notes: List<Note>,
    private val onArchive: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): NoteViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.btnArchive.setOnClickListener { onArchive(note) }
    }

    override fun getItemCount(): Int = notes.size

    class NoteViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val btnArchive: Button = itemView.findViewById(R.id.btnArchive)

        fun bind(note: Note) {
            tvTitle.text = note.title
            tvContent.text = note.content
            tvCategory.text = "Категория: ${note.category}"
        }
    }
}
