package com.humidity.domain;

import com.humidity.domain.error.*;
import com.humidity.entity.Area;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.persistence.EntityUtil;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.UUID;

public class AreaManagement {
    public static List<Area> loadAreas(User u) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {

            //load all areas available to the user
            //if status is set it gets all by the status defined
            List<Area> areas = session.createQuery("from Area where userId = :uid", Area.class)
                    .setParameter("uid", u.getId())
                    .list();

            if (areas.size() == 0) {
                throw new ListAreasAcquiringException("Any area found", null);
            }

            return areas;

        } catch (PersistenceException pe) {
            throw new ListAreasAcquiringException("Something went wrong", pe);
        }
    }

    public static List<Area> loadAreasByStatus(User u, String status) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {

            //load all areas with defined status available to the user
            List<Area> areas = session.createQuery("from Area where userId = :uid and status = :status", Area.class)
                    .setParameter("uid", u.getId())
                    .setParameter("status", status)
                    .list();

            if (areas.size() == 0) {
                throw new ListAreasAcquiringException("Any area found", null);
            }

            return areas;

        } catch (PersistenceException pe) {
            throw new ListAreasAcquiringException("Something went wrong", pe);
        }
    }

    public static Area loadAreaById(User u, String areaId) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            Area area = session.createQuery("from Area where userId = :uid and id = :id", Area.class)
                    .setParameter("uid", u.getId())
                    .setParameter("id", UUID.fromString(areaId))
                    .getSingleResult();

            if (!area.getUserId().equals(u.getId())) {
                throw new AreaAcquiringException("Area not available to the user", null);
            }
            return area;

        } catch (PersistenceException pe) {
            throw new AreaAcquiringException("Something went wrong", pe);
        }
    }

    public static Area loadAreaByName(User u, String name) throws StandardException{
        try (Session session = EntityUtil.openTransactionSession()) {
            Area area = session.createQuery("from Area where userId = :uid and name = :name", Area.class)
                    .setParameter("uid", u.getId())
                    .setParameter("name", name)
                    .getSingleResult();

            if (!area.getUserId().equals(u.getId())) {
                throw new AreaAcquiringException("Area not available to the user", null);
            }

            return area;

        } catch (PersistenceException pe) {
            throw new AreaAcquiringException("Something went wrong", pe);
        }
    }

    public static Area insertArea(User u, String name, String description) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {

            Area area = new Area();
            area.setUserId(u.getId());
            area.setName(name);
            area.setDescription(description);
            area.setStatus("enabled");
            session.persist(area);

            session.getTransaction().commit();

            return area;
        } catch (PersistenceException pe) {
            throw new InsertNewAreaException("Something went wrong", pe);
        }
    }

    public static Area disableArea(User u, UUID areaId) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            Area area = session.createQuery("from Area where id = :id", Area.class)
                    .setParameter("id", areaId)
                    .getSingleResult();

            if (!area.getUserId().equals(u.getId())) {
                throw new AreaAcquiringException("Area not available to the user", null);
            }

            area.setStatus("disabled");
            session.update(area);

            return area;
        } catch (PersistenceException pe) {
            throw new DisableAreaException("Something went wrong", pe);
        }
    }

    public static Area enableArea(User u, UUID areaId) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            Area area = session.createQuery("from Area where id = :id", Area.class)
                    .setParameter("id", areaId)
                    .getSingleResult();

            if (!area.getUserId().equals(u.getId())) {
                throw new AreaAcquiringException("Area not available to the user", null);
            }

            area.setStatus("enabled");
            session.update(area);

            return area;
        } catch (PersistenceException pe) {
            throw new EnableAreaException("Something went wrong", pe);
        }
    }
}
