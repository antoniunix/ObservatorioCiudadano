package net.gshp.observatoriociudadano.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.gshp.observatoriociudadano.dto.DtoAgenda;
import net.gshp.observatoriociudadano.dto.DtoSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class DaoAgenda extends DAO {

    public static String TABLE_NAME = "agenda";
    public static String PK_FIELD = "id";
    private final String id = "id";
    private final String id_user = "id_user";
    private final String id_place = "id_place";
    private final String start_datetime = "start_datetime";
    private final String end_datetime = "end_datetime";
    private final String idRol = "id_rol";

    private SQLiteDatabase db;
    private Cursor cursor;

    public DaoAgenda() {
        super(TABLE_NAME, PK_FIELD);
    }

    /**
     * insert
     */
    public int insert(List<DtoAgenda> obj) {
        db = helper.getWritableDatabase();
        int resp = 0;
        try {
            SQLiteStatement insStatement = db.compileStatement("INSERT INTO "
                    + TABLE_NAME + " (" + id + "," + id_user + "," + id_place
                    + "," + start_datetime + "," + end_datetime + ")"
                    + "VALUES(?,?,?,?,?);");
            db.beginTransaction();
            for (DtoAgenda dto : obj) {

                try {
                    insStatement.bindLong(1, dto.getId_agenda());
                } catch (Exception e) {
                    insStatement.bindNull(1);
                }
                try {
                    insStatement.bindLong(2, dto.getId_user());
                } catch (Exception e) {
                    insStatement.bindNull(2);
                }
                try {
                    insStatement.bindLong(3, dto.getId_pdv());
                } catch (Exception e) {
                    insStatement.bindNull(3);
                }
                try {
                    insStatement.bindString(4, dto.getStart_datetime());
                } catch (Exception e) {
                    insStatement.bindNull(4);
                }
                try {
                    insStatement.bindString(5, dto.getEnd_datetime());
                } catch (Exception e) {
                    insStatement.bindNull(5);
                }
                insStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
        return resp;
    }

    public List<DtoSchedule> select(String dayOfWeek) {
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT\n" +
                "agenda.id,\n" +
                "agenda.id_place,\n" +
                "agenda.start_datetime,\n" +
                "agenda.end_datetime,\n" +
                "pdv.name,\n" +
                "c_client.value\n" +
                "FROM\n" +
                "agenda\n" +
                "INNER JOIN pdv ON pdv.id = agenda.id_place\n" +
                "INNER JOIN c_client ON c_client.id = pdv.id_client\n" +
                "LEFT JOIN report ON report.id_agenda = agenda.id\n" +
                "LEFT JOIN report_check ON report_check.id_report_local = report.id\n" +
                "WHERE  agenda.start_datetime LIKE '" + dayOfWeek + " %'\n" +
                "ORDER BY agenda.start_datetime", null);
        List<DtoSchedule> obj = new ArrayList<>(cursor.getCount());
        DtoSchedule catalogo;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int idPlace = cursor.getColumnIndexOrThrow("id_place");
            int startDate = cursor.getColumnIndexOrThrow("start_datetime");
            int endDate = cursor.getColumnIndexOrThrow("end_datetime");
            int name = cursor.getColumnIndexOrThrow("name");
            int client = cursor.getColumnIndexOrThrow("value");


            do {
                catalogo = new DtoSchedule();
                catalogo.setId_agenda(cursor.getInt(id));
                catalogo.setId_pdv(cursor.getInt(idPlace));
                catalogo.setStart_datetime(cursor.getString(startDate));
                catalogo.setEnd_datetime(cursor.getString(endDate));
                catalogo.setName(cursor.getString(name));
                catalogo.setClient(cursor.getString(client));
                obj.add(catalogo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obj;
    }

    /**
     * Select
     */
    public void select(List<DtoSchedule> lstMonday, List<DtoSchedule> lstTuesday,
                       List<DtoSchedule> lstWednesday, List<DtoSchedule> lstThursday,
                       List<DtoSchedule> lstFriday, List<DtoSchedule> lstSaturday,
                       List<DtoSchedule> lstSunday, long idUser) {
        db = helper.getReadableDatabase();
        String query = "SELECT DISTINCT\n" +
                "agenda.id,\n" +
                "agenda.id_place,\n" +
                "agenda.id_user,\n" +
                " strftime('%H:%M',agenda.start_datetime,'localtime') start,\n" +
                " strftime('%H:%M',agenda.end_datetime,'localtime') end,\n" +
                "agenda.start_datetime,\n" +
                "agenda.end_datetime,\n" +
                "pdv.name,\n" +
                "pdv.id_client,\n" +
                "pdv.type,\n" +
                "coop_pdv.total,  \n" +
                "count(report_task.id) numTask,\n" +
                "c_client.value,\n" +
                "CASE WHEN checkIn.id IS NOT NULL\n" +
                "       THEN 1\n" +
                "       ELSE 0\n" +
                "       END AS isCheckIn,\n" +
                "CASE WHEN checkOut.id IS NOT NULL\n" +
                "       THEN 1\n" +
                "       ELSE 0\n" +
                "       END AS ischeckOut\n" +
                "FROM\n" +
                "agenda\n" +
                "INNER JOIN pdv ON pdv.id = agenda.id_place\n" +
                "INNER JOIN c_client ON c_client.id = pdv.id_client\n" +
                "LEFT JOIN coop_pdv ON coop_pdv.id_pdv = agenda.id_place  \n" +
                "LEFT JOIN report_task ON report_task.id_pdv=agenda.id_place\n" +
                "LEFT JOIN report ON report.id_agenda = agenda.id\n" +
                "LEFT JOIN report_check  checkIn ON checkIn.id_report_local = report.id AND checkIn.type=1\n" +
                "LEFT JOIN report_check  checkOut ON checkOut.id_report_local = report.id AND checkOut.type=2\n" +
                (idUser!=0?"WHERE agenda.id_user="+idUser+"\n":"\n")+
                "  GROUP BY agenda.id \n"+
                "  ORDER BY agenda.start_datetime ";

        cursor = db.rawQuery(query, null);
        DtoSchedule catalogo;

        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int idPlace = cursor.getColumnIndexOrThrow("id_place");
            int id_user = cursor.getColumnIndexOrThrow("id_user");
            int startDate = cursor.getColumnIndexOrThrow("start_datetime");
            int endDate = cursor.getColumnIndexOrThrow("end_datetime");
            int start = cursor.getColumnIndexOrThrow("start");
            int end = cursor.getColumnIndexOrThrow("end");
            int name = cursor.getColumnIndexOrThrow("name");
            int client = cursor.getColumnIndexOrThrow("value");
            int isCheckIn = cursor.getColumnIndexOrThrow("isCheckIn");
            int ischeckOut = cursor.getColumnIndexOrThrow("ischeckOut");
            int idClient = cursor.getColumnIndexOrThrow("id_client");
            int typePdv = cursor.getColumnIndexOrThrow("type");
            int numTask = cursor.getColumnIndexOrThrow("numTask");
            int total = cursor.getColumnIndexOrThrow("total");


            do {
                catalogo = new DtoSchedule();
                catalogo.setId_agenda(cursor.getInt(id));
                catalogo.setId_pdv(cursor.getLong(idPlace));
                catalogo.setIdUser(cursor.getLong(id_user));
                catalogo.setId_client(cursor.getInt(idClient));
                catalogo.setTypePdv(cursor.getInt(typePdv));
                catalogo.setNumTask(cursor.getInt(numTask));
                catalogo.setQualification(cursor.getInt(total));
                catalogo.setStart_datetime(cursor.getString(startDate));
                catalogo.setEnd_datetime(cursor.getString(endDate));
                catalogo.setStart(cursor.getString(start));
                catalogo.setEnd(cursor.getString(end));
                catalogo.setName(cursor.getString(name));
                catalogo.setClient(cursor.getString(client));
                catalogo.setCheckIn(cursor.getInt(isCheckIn) == 1);
                catalogo.setCheckOut(cursor.getInt(ischeckOut) == 1);

                switch (getDayOfWeekFromDate(catalogo.getStart_datetime())) {
                    case Calendar.MONDAY:
                        lstMonday.add(catalogo);
                        break;
                    case Calendar.TUESDAY:
                        lstTuesday.add(catalogo);
                        break;
                    case Calendar.WEDNESDAY:
                        lstWednesday.add(catalogo);
                        break;
                    case Calendar.THURSDAY:
                        lstThursday.add(catalogo);
                        break;
                    case Calendar.FRIDAY:
                        lstFriday.add(catalogo);
                        break;
                    case Calendar.SATURDAY:
                        lstSaturday.add(catalogo);
                        break;
                    case Calendar.SUNDAY:
                        lstSunday.add(catalogo);
                        break;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    /**
     * Select
     */
    public List<DtoSchedule> select(long idUser) {
        db = helper.getReadableDatabase();
        String query = "SELECT  \n" +
                "agenda.id,  \n" +
                "agenda.id_place,  \n" +
                " strftime('%H:%M',agenda.start_datetime,'localtime') start,  \n" +
                " strftime('%H:%M',agenda.end_datetime,'localtime') end,  \n" +
                "agenda.start_datetime,  \n" +
                "agenda.end_datetime,  \n" +
                "pdv.name,  \n" +
                "pdv.id_client,  \n" +
                "pdv.type,  \n" +
                "coop_pdv.total,  \n" +
                "c_client.value,  \n" +
                "count(report_task.id) numTask,\n" +
                "CASE WHEN checkIn.id IS NOT NULL  \n" +
                "       THEN 1  \n" +
                "       ELSE 0  \n" +
                "       END AS isCheckIn,  \n" +
                "CASE WHEN checkOut.id IS NOT NULL  \n" +
                "       THEN 1  \n" +
                "       ELSE 0  \n" +
                "       END AS ischeckOut  \n" +
                "FROM  \n" +
                "agenda  \n" +
                "INNER JOIN pdv ON pdv.id = agenda.id_place  \n" +
                "INNER JOIN c_client ON c_client.id = pdv.id_client  \n" +
                "LEFT JOIN report ON report.id_agenda = agenda.id  \n" +
                "LEFT JOIN coop_pdv ON coop_pdv.id_pdv = agenda.id_place  \n" +
                "LEFT JOIN report_check  checkIn ON checkIn.id_report_local = report.id AND checkIn.type=1  \n" +
                "LEFT JOIN report_check  checkOut ON checkOut.id_report_local = report.id AND checkOut.type=2  \n" +
                "LEFT JOIN report_task ON report_task.id_pdv=agenda.id_place\n" +
                " WHERE strftime('%Y-%m-%d',agenda.start_datetime,'localtime') = strftime('%Y-%m-%d','now','localtime')  \n" +
                (idUser!=0?"AND agenda.id_user="+idUser+"\n":"\n")+
                "GROUP BY agenda.id\n" +
                "ORDER BY agenda.start_datetime";



        cursor = db.rawQuery(query, null);
        List<DtoSchedule> lstDtoSchedules = new ArrayList<>();
        DtoSchedule catalogo;

        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndexOrThrow("id");
            int idPlace = cursor.getColumnIndexOrThrow("id_place");
            int startDate = cursor.getColumnIndexOrThrow("start_datetime");
            int endDate = cursor.getColumnIndexOrThrow("end_datetime");
            int start = cursor.getColumnIndexOrThrow("start");
            int end = cursor.getColumnIndexOrThrow("end");
            int name = cursor.getColumnIndexOrThrow("name");
            int client = cursor.getColumnIndexOrThrow("value");
            int isCheckIn = cursor.getColumnIndexOrThrow("isCheckIn");
            int ischeckOut = cursor.getColumnIndexOrThrow("ischeckOut");
            int idClient = cursor.getColumnIndexOrThrow("id_client");
            int typePdv = cursor.getColumnIndexOrThrow("type");
            int numTask = cursor.getColumnIndexOrThrow("numTask");
            int total = cursor.getColumnIndexOrThrow("total");


            do {
                catalogo = new DtoSchedule();
                catalogo.setId_agenda(cursor.getInt(id));
                catalogo.setId_pdv(cursor.getInt(idPlace));
                catalogo.setId_client(cursor.getInt(idClient));
                catalogo.setTypePdv(cursor.getInt(typePdv));
                catalogo.setNumTask(cursor.getInt(numTask));
                catalogo.setQualification(cursor.getInt(total));
                catalogo.setStart_datetime(cursor.getString(startDate));
                catalogo.setEnd_datetime(cursor.getString(endDate));
                catalogo.setStart(cursor.getString(start));
                catalogo.setEnd(cursor.getString(end));
                catalogo.setName(cursor.getString(name));
                catalogo.setClient(cursor.getString(client));
                catalogo.setCheckIn(cursor.getInt(isCheckIn) == 1);
                catalogo.setCheckOut(cursor.getInt(ischeckOut) == 1);

                lstDtoSchedules.add(catalogo);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstDtoSchedules;
    }

    public int isSomeReportIncomplete() {
        db = helper.getReadableDatabase();
        int idReportIncomplete = 0;
        cursor = db.rawQuery("SELECT\n" +
                "report.id,\n" +
                "CASE WHEN chIn.id IS NOT NULL AND  chOut.id ISNULL\n" +
                "THEN 1\n" +
                "ELSE 0\n" +
                "END AS isCheckIn\n" +
                "FROM\n" +
                "report\n" +
               // "INNER JOIN agenda ON agenda.id=report.id_agenda\n" +
                "LEFT  JOIN report_check  chIn ON chIn.id_report_local = report.id AND chIn.type=1\n" +
                "LEFT  JOIN report_check  chOut ON chOut.id_report_local = report.id AND chOut.type=2", null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("isCheckIn")) == 1) {
                    idReportIncomplete = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return idReportIncomplete;
    }

    public boolean isMakeTodayThisSchedule(long idAgenda) {
        db = helper.getReadableDatabase();
        boolean isReportToday = false;
        cursor = db.rawQuery("SELECT\n" +
                "report.id,\n" +
                "CASE WHEN strftime('%Y-%m-%d','now','localtime') =strftime('%Y-%m-%d',datetime(report.date/1000, 'unixepoch','localtime'))\n" +
                "THEN 1\n" +
                "ELSE 0\n" +
                "END AS isTodayMakeSchedule\n" +
                "FROM\n" +
                "agenda\n" +
                "INNER JOIN report ON report.id_agenda = agenda.id\n" +
                "WHERE agenda.id=" + idAgenda, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("isTodayMakeSchedule")) == 1) {
                    isReportToday = true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return isReportToday;
    }

    private int getDayOfWeekFromDate(String dateOfDb) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date;
        Calendar c = Calendar.getInstance(Locale.GERMANY);

        try {
            date = formatter.parse(dateOfDb);
            c.setTime(date); // yourdate is an object of type Date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public  int howSchedulNext(long idPdv){
        int count=0;
        db=helper.getReadableDatabase();
        cursor=db.rawQuery("SELECT agenda.*  " +
                "FROM agenda  " +
                "WHERE strftime('%w', 'now','localtime')<strftime('%w', agenda.start_datetime) " +
                "and agenda.id_place="+idPdv,null);
        count=cursor.getCount();

        cursor.close();
        db.close();
        return count;
    }

    public int delete(int idRol) {

        int resp = 0;
        try {
            db = helper.getWritableDatabase();
            resp = db.delete(TABLE_NAME, "id_rol="+idRol, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return resp;
    }


}
