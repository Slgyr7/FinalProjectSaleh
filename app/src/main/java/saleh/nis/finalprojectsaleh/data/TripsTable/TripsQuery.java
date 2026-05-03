package saleh.nis.finalprojectsaleh.data.TripsTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripsQuery {
    @Query("SELECT * FROM Trips ORDER BY title DESC")
    List<Trips> getAllTrips();

    @Query("SELECT * FROM Trips WHERE title = :name")
    List<Trips> getTripsByName(String name);

    @Query("SELECT * FROM Trips WHERE category = :category")
    List<Trips> getTripsByCategory(String category);

    @Query("SELECT * FROM Trips WHERE status = :status")
    List<Trips> getTripsByStatus(String status);

    @Query("SELECT * FROM Trips WHERE rating >= :minRating")
    List<Trips> getTripsByMinRating(double minRating);

    @Query("SELECT * FROM Trips WHERE price BETWEEN :minPrice AND :maxPrice")
    List<Trips> getTripsByPriceRange(double minPrice, double maxPrice);

    @Query("SELECT * FROM Trips WHERE price <= :maxPrice")
    List<Trips> getTripsCheaperThan(double maxPrice);

    @Query("SELECT * FROM Trips WHERE price >= :minPrice")
    List<Trips> getTripsMoreExpensiveThan(double minPrice);

    @Query("SELECT * FROM Trips ORDER BY price ASC")
    List<Trips> getTripsByPriceAscending();

    @Query("SELECT * FROM Trips ORDER BY price DESC")
    List<Trips> getTripsByPriceDescending();

    @Query("SELECT AVG(price) FROM Trips")
    double getAverageTripPrice();
    @Query("DELETE FROM Trips WHERE keyid = :tripId")
    void deleteTripById(long tripId);
    @Query("SELECT * FROM Trips WHERE price >= :minPrice")
    List<Trips> getTripsByMinPrice(double minPrice);

    @Query("SELECT * FROM Trips WHERE address = :address")
    List<Trips> getTripsByAddress(String address);
    @Insert
    void insertAll(Trips... trips);

    @Update
    void update(Trips trip);

    @Delete
    void delete(Trips trip);


}
