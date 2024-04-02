import android.content.Context
import androidx.room.Room
import com.example.projecta2.Dao.UserDB

class DatabaseInitializer private constructor() {

    companion object {
        @Volatile private var INSTANCE: UserDB? = null

        fun initDatabase(context: Context): UserDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDB::class.java, "UserDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
