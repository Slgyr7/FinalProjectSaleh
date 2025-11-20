package saleh.nis.finalprojectsaleh.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import saleh.nis.finalprojectsaleh.data.MySiteTable.site;
import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;
import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUserQuery;

public class AppDatabase {
    @Database(entities = {MyUser.class, site.class},version =1)
    public abstract class AppDataBase extends RoomDatabase {
private static AppDatabase dp;
public abstract MyUserQuery getMyUserQuery();
public abstract siteQuery getSiteQuery();
public static AppDataBase getDB(Context context)
    if(db==null)
    {
        dp= Room.databaseBuilder(context,AppDatabase.class,"saleh.DataBase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
    return dp;

    }
}
