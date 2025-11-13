package saleh.nis.finalprojectsaleh.data.MySiteTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SiteQuery {
    @Query("SELECT * FROM site order by name desc")
    List<site> getAllSite();

    @Query("SELECT * FROM site WHERE name =:N")
    List<site> getAllSiteByReview(String N);

    @Query("SELECT * FROM site WHERE name =:N AND Cost =:Cost ")
    List<site> getAllSiteByReview(String N, Integer Cost);
    @Insert
    void insertAll(site... sites);
    @Delete
    void delete(site site);
    @Query("DELETE FROM site WHERE KeyId=:keyid")
    void deletesite(long keyid);
    @Update
    void update(site site);

    @Query("SELECT * FROM site WHERE Category =:keyid"+" ORDER BY Category desc")
List<site> getAllSiteByType(long keyid);
}
