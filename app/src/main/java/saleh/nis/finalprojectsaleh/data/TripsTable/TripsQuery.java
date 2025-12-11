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

    @Query("SELECT * FROM Trips WHERE Category = :category")
    List<Trips> getTripsByCategory(String category);

    @Query("SELECT * FROM Trips WHERE Status = :status")
    List<Trips> getTripsByStatus(String status);

    @Query("SELECT * FROM Trips WHERE Rating >= :minRating")
    List<Trips> getTripsByMinRating(double minRating);

    @Query("SELECT * FROM Trips WHERE Price BETWEEN :minPrice AND :maxPrice")
    List<Trips> getTripsByPriceRange(double minPrice, double maxPrice);

    @Query("SELECT * FROM Trips WHERE Price <= :maxPrice")
    List<Trips> getTripsCheaperThan(double maxPrice);

    @Query("SELECT * FROM Trips WHERE Price >= :minPrice")
    List<Trips> getTripsMoreExpensiveThan(double minPrice);

    @Query("SELECT * FROM Trips ORDER BY Price ASC")
    List<Trips> getTripsByPriceAscending();

    @Query("SELECT * FROM Trips ORDER BY Price DESC")
    List<Trips> getTripsByPriceDescending();

    @Query("SELECT AVG(Price) FROM Trips")
    double getAverageTripPrice();
    @Query("DELETE FROM Trips WHERE keyid = :tripId")
    void deleteTripById(long tripId);
    @Query("SELECT * FROM Trips WHERE Price >= :minPrice")
    List<Trips> getTripsByMinPrice(double minPrice);

    @Query("SELECT * FROM Trips WHERE Address = :address")
    List<Trips> getTripsByAddress(String address);
    @Insert
    void insertAll(Trips... trips);

    @Update
    void update(Trips trip);

    @Delete
    void delete(Trips trip);


}
