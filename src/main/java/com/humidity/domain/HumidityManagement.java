package com.humidity.domain;

import com.google.common.base.Ticker;
import com.humidity.domain.error.*;
import com.humidity.entity.Area;
import com.humidity.entity.Device;
import com.humidity.entity.Humidity;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.persistence.EntityUtil;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class HumidityManagement {

    public static List<Humidity> loadHumiditiesByArea(User u, UUID areaId) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            Area area = session.createQuery("from Area where id = :id", Area.class)
                    .setParameter("id", areaId)
                    .getSingleResult();

            if (!area.getUserId().equals(u.getId())) {
                throw new AreaAcquiringException("Area not found", null);
            }

            List<Humidity> humidities = session.createQuery("from Humidity where areaId = :aid", Humidity.class)
                    .setParameter("aid", areaId)
                    .list();

            if (humidities.size() == 0) {
                throw new ListHumiditiesAcquiringException("Any humidity found");
            }

            return humidities;

        } catch (PersistenceException pe) {
            throw new HumidityAcquiringException("Something went wrong", pe);
        }
    }

    public static List<Humidity> loadHumiditiesByDevice(User u, UUID deviceId) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            Device area = session.createQuery("from Device where id = :id", Device.class)
                    .setParameter("id", deviceId)
                    .getSingleResult();

            if (!area.getUserId().equals(u.getId())) {
                throw new AreaAcquiringException("Area not found", null);
            }

            List<Humidity> humidities = session.createQuery("from Humidity where deviceId = :deviceId", Humidity.class)
                    .setParameter("deviceId", deviceId)
                    .list();

            if (humidities.size() == 0) {
                throw new ListHumiditiesAcquiringException("Any humidity found");
            }

            return humidities;

        } catch (PersistenceException pe) {
            throw new HumidityAcquiringException("Something went wrong", pe);
        }
    }

    public static JsonObject loadHumidities(User u) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            List<Area> areas = session.createQuery("from Area where userId = :uid", Area.class)
                    .setParameter("uid", u.getId())
                    .list();

            if (areas.size() == 0) {
                throw new AreaAcquiringException("Any area found", null);
            }

            JsonObject areaResults = new JsonObject();

            for (Area area : areas) {

                List<Humidity> humidities = session.createQuery("from Humidity where areaId = :aid", Humidity.class)
                        .setParameter("aid", area.getId())
                        .list();

                if (humidities.size() == 0)
                    areaResults.put(area.getName(), "Any humidity found");
                else
                    areaResults.put(area.getName(), humidities);
            }

            return areaResults;
        } catch (PersistenceException pe) {
            throw new HumidityAcquiringException("Something went wrong", pe);
        }
    }

    public static Humidity insertHumidity(User u, UUID areaId, UUID deviceId, BigInteger value) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {

            Humidity humidity = new Humidity();
            humidity.setAreaId(areaId);
            humidity.setDeviceId(deviceId);
            humidity.setDescription("Solo Umido");
            humidity.setValue(value);
            session.persist(humidity);
            session.getTransaction().commit();

            return humidity;

        } catch (PersistenceException pe) {
            throw new InsertHumidityException("Something went wrong", pe);
        }
    }

    public static List<Humidity> loadHumiditiesByAreaAndDate(User u, UUID areaId, String begin, String end) throws StandardException, ParseException {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = sdf.parse(begin);
        Date endDate = sdf.parse(end);

        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE, 1);

        Long timestampEnd = cal.getTimeInMillis();

        try (Session session = EntityUtil.openTransactionSession()) {

            Area area = session.createQuery("from Area where userId = :uid and id = :id", Area.class)
                    .setParameter("uid", u.getId())
                    .setParameter("id", areaId)
                    .getSingleResult();

            if(!Optional.ofNullable(area).isPresent())
            {
                throw new AreaAcquiringException("Area Not Found", null);
            }

            List<Humidity> humidities = session.createQuery("from Humidity where areaId = :aid and createdAt between " +
                    ":beginDate and :endDate order by createdAt desc", Humidity.class)
                    .setParameter("aid", area.getId())
                    .setParameter("beginDate", beginDate)
                    .setParameter("endDate", sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestampEnd))))
                    .list();

            return humidities;

        } catch (ParseException pa) {
            throw new HumidityDateException("Date Convertion error", pa);
        } catch (PersistenceException pe) {
            throw new HumidityAcquiringException("Something went wrong", pe);
        }
    }

    public static List<Humidity> loadHumiditiesByDeviceAndDate(User u, UUID deviceId, String begin, String end) throws StandardException, ParseException {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = sdf.parse(begin);
        Date endDate = sdf.parse(end);

        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE, 1);

        Long timestampEnd = cal.getTimeInMillis();

        try (Session session = EntityUtil.openTransactionSession()) {

            Device device = session.createQuery("from Device where userId = :uid and id = :id", Device.class)
                    .setParameter("uid", u.getId())
                    .setParameter("id", deviceId)
                    .getSingleResult();

            if(!Optional.ofNullable(device).isPresent())
            {
                throw new DeviceAcquiringException("Device Not Found");
            }

            List<Humidity> humidities = session.createQuery("from Humidity where deviceId = :did and createdAt between " +
                    ":beginDate and :endDate order by createdAt desc", Humidity.class)
                    .setParameter("did", device.getId())
                    .setParameter("beginDate", beginDate)
                    .setParameter("endDate", sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestampEnd))))
                    .list();

            return humidities;

        } catch (ParseException pa) {
            throw new HumidityDateException("Date Convertion error", pa);
        } catch (PersistenceException pe) {
            throw new HumidityAcquiringException("Something went wrong", pe);
        }
    }

    public static JsonObject loadHumiditiesByDate(User u, String begin, String end) throws StandardException, ParseException {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = sdf.parse(begin);
        Date endDate = sdf.parse(end);

        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE, 1);

        Long timestampEnd = cal.getTimeInMillis();

        try (Session session = EntityUtil.openTransactionSession()) {

            List<Area> areas = session.createQuery("from Area where userId = :uid", Area.class)
                    .setParameter("uid", u.getId())
                    .list();

            JsonObject areasJson = new JsonObject();
            for (Area area : areas) {
                List<Humidity> humidities = session.createQuery("from Humidity where areaId = :aid and createdAt between " +
                        ":beginDate and :endDate order by createdAt desc", Humidity.class)
                        .setParameter("aid", area.getId())
                        .setParameter("beginDate", beginDate)
                        .setParameter("endDate", sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestampEnd))))
                        .list();

                areasJson.put(area.getName(), humidities);
            }

            return areasJson;

        } catch (ParseException pa) {
            throw new HumidityDateException("Date Convertion error", pa);
        } catch (PersistenceException pe) {
            throw new HumidityAcquiringException("Balance not found", pe);
        }
    }
}
