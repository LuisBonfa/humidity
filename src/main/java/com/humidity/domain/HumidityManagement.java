package com.humidity.domain;

import com.humidity.domain.error.*;
import com.humidity.entity.Area;
import com.humidity.entity.Device;
import com.humidity.entity.Humidity;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.persistence.EntityUtil;
import io.vertx.core.json.JsonObject;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;


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
}
