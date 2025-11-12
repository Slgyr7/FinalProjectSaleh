package saleh.nis.finalprojectsaleh.data.UserProfileTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface MyUserQuery {
    @Query("SELECT * FROM MyUser")
    List<MyUser> getAll();  //استخراج جميع المستعملين

    @Query("SELECT * FROM MyUser WHERE keyid IN (:userIds)")
    List<MyUser> loadAllByIds(int[] userIds); // استخراج مستعمل حسب رقم المميز لهid

    @Query("SELECT * FROM MyUser WHERE email = :myEmail AND passw = :myPassw LIMIT 1")
    MyUser checkEmailPassw(String myEmail, String myPassw);// هل المستعمل موجود حسب الايميل وكلمة السر

    @Query("SELECT * FROM MyUser WHERE email = :myEmail LIMIT 1")
    MyUser checkEmail(String myEmail);   //فحص هل الايميل موجود من قبل

    @Insert// اضافة مستعمل او مجموعة مستعملين
    void insertAll(MyUser... users);// اضافة مستعمل او مجموعة مستعملين

    @Delete// حذف
    void delete(MyUser user);// حذف

    @Query("Delete From MyUser WHERE keyid=:id ")
    void delete(int id);//حذف حسب الرقم المميز id

    @Insert//اضافة مستعمل واحد
    void insert(MyUser myUser);//اضافة مستعمل واحد

    @Update//تعديل مستعمل او قائمة مستعملين
    void update(MyUser...values);//تعديل مستعمل او قائمة مستعملين


}
