package saleh.nis.finalprojectsaleh.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import saleh.nis.finalprojectsaleh.data.MySiteTable.site;
import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;

public class AppDatabase {
    @Database(entities = {MyUser.class, site.class},version =1)
    public abstract class AppDataBase extends RoomDatabase {

    }
}
