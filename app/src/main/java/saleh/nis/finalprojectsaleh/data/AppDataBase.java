package saleh.nis.finalprojectsaleh.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import saleh.nis.finalprojectsaleh.data.MySiteTable.SiteQuery;
import saleh.nis.finalprojectsaleh.data.MySiteTable.site;
import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;
import saleh.nis.finalprojectsaleh.data.TripsTable.TripsQuery;
import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;
import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUserQuery;

// Updated version to 5 because we changed the MyUser table structure
@Database(entities = {MyUser.class, site.class , Trips.class}, version = 7, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase dp;

    public abstract MyUserQuery getMyUserQuery();
    public abstract SiteQuery getSiteQuery();
    public abstract TripsQuery getTripsQuery();

    public static AppDataBase getDB(Context context) {
        if (dp == null) {
            dp = Room.databaseBuilder(context, AppDataBase.class, "saleh.DataBase")
                    .fallbackToDestructiveMigration() // This wipes the DB when version changes - good for development
                    .allowMainThreadQueries()
                    .build();
        }
        return dp;
    }
}
