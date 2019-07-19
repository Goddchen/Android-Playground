package de.goddchen.android.playground.playgrounds.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import de.goddchen.android.playground.R
import kotlinx.android.synthetic.main.fragment_playground_room.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("unused")
class RoomFragment : Fragment() {

    private var db: Database? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        db = context?.let {
            Room.inMemoryDatabaseBuilder(it, Database::class.java)
                .build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db?.close()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_playground_room, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.adapter = DogAdapter()

        liveData { db?.dogDao()?.getAll()?.let { emitSource(it) } }.observe(this@RoomFragment, Observer {
            it?.let { dogs -> (recycler_view.adapter as? DogAdapter)?.setData(dogs) }
        })
        add.setOnClickListener {
            GlobalScope.launch {
                db?.dogDao()?.insert(generateRandomDog())
            }
        }
    }

    private fun generateRandomDog(): Dog {
        val names = listOf("Linux", "Sleven", "Aaros", "Xandor", "Sunray")
        val breeds = listOf("Terrier", "Schäferhund", "Retriever")
        return Dog(id = 0, name = names.random(), breed = breeds.random())
    }

    class DogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var data: List<Dog>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = object :
            RecyclerView.ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    android.R.layout.simple_list_item_2,
                    parent,
                    false
                )
            ) {}

        override fun getItemCount(): Int = data?.size ?: 0

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val dog = data?.get(position)
            holder.itemView.findViewById<TextView>(android.R.id.text1)?.text = dog?.name
            holder.itemView.findViewById<TextView>(android.R.id.text2)?.text = dog?.breed
        }

        fun setData(dogs: List<Dog>) {
            data = dogs
            notifyDataSetChanged()
        }

    }
}

@Entity
data class Dog(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val breed: String
)

@Dao
interface DogDao {
    @Query("SELECT * FROM Dog")
    fun getAll(): LiveData<List<Dog>>

    @Query("SELECT * FROM Dog WHERE id=:id")
    suspend fun findById(id: Int): Dog?

    @Insert
    suspend fun insert(dog: Dog)

    @Delete
    suspend fun delete(dog: Dog)
}

@androidx.room.Database(entities = [Dog::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dogDao(): DogDao
}