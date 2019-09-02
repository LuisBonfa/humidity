package com.humidity.domain;

import com.humidity.domain.error.DeviceAcquiringException;
import com.humidity.domain.error.DeviceInsertException;
import com.humidity.domain.error.ListDevicesAcquiringException;
import com.humidity.entity.Device;
import com.humidity.entity.Model;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.persistence.EntityUtil;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.UUID;

public class DeviceManagement {

    public static Device insertDevice(User u, UUID modelId, UUID areaId, String name) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            Device device = new Device();
            device.setUserId(u.getId());
            device.setName(name);
            device.setModelId(modelId);
            device.setAreaId(areaId);
            device.setStatus("enabled");
            session.persist(device);

            session.getTransaction().commit();

            return device;
        } catch (PersistenceException pe) {
            throw new DeviceInsertException("Something went wrong", pe);
        }
    }

    public static List<Device> loadDevices(User u) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {

            List<Device> devices = session.createQuery("from Device where userId = :uid", Device.class)
                    .setParameter("uid", u.getId())
                    .list();

            if (devices.size() == 0) {
                throw new ListDevicesAcquiringException("Any Device Found", null);
            }

            return devices;

        } catch (PersistenceException pe) {
            throw new ListDevicesAcquiringException("Something went wrong", pe);
        }
    }

    public static List<Device> loadDevicesByStatus(User u, String status) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {

            List<Device> devices = session.createQuery("from Device where userId = :uid and status = :status", Device.class)
                    .setParameter("uid", u.getId())
                    .setParameter("status", status)
                    .list();

            if (devices.size() == 0) {
                throw new ListDevicesAcquiringException("Any Device Found", null);
            }

            return devices;

        } catch (PersistenceException pe) {
            throw new ListDevicesAcquiringException("Something went wrong", pe);
        }
    }

    public static List<Device> loadDevicesByModel(User u, String name) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {

            Model model = session.createQuery("from Model where id = :id", Model.class)
                    .setParameter("model", name)
                    .getSingleResult();

            List<Device> devices = session.createQuery("from Device where userId = :uid and modelId = :model", Device.class)
                    .setParameter("uid", u.getId())
                    .setParameter("model", model.getId())
                    .list();

            if (devices.size() == 0) {
                throw new ListDevicesAcquiringException("Any Device Found", null);
            }

            return devices;

        } catch (PersistenceException pe) {
            throw new ListDevicesAcquiringException("Something went wrong", pe);
        }
    }

    public static Device loadDeviceById(User u, String deviceIdOrName) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            List<Device> device = session.createQuery("from Device where userId = :uid and id = :id", Device.class)
                    .setParameter("uid", u.getId())
                    .setParameter("id", UUID.fromString(deviceIdOrName))
                    .setMaxResults(1)
                    .list();

            if (device.size() == 0) {
                throw new DeviceAcquiringException("Device not Found");
            }

            return device.get(0);
        }
    }

    public static Device loadDeviceByName(User u, String deviceIdOrName) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            List<Device> device = session.createQuery("from Device where userId = :uid and name = :name", Device.class)
                    .setParameter("uid", u.getId())
                    .setParameter("name", deviceIdOrName)
                    .setMaxResults(1)
                    .list();

            if (device.size() == 0) {
                throw new DeviceAcquiringException("Device not Found");
            }

            return device.get(0);
        }
    }

    public static Device enableDevice(User u, UUID deviceId) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            List<Device> device = session.createQuery("from Device where userId = :uid and id = :id", Device.class)
                    .setParameter("uid", u.getId())
                    .setParameter("id", deviceId)
                    .setMaxResults(1)
                    .list();

            if (device.size() == 0) {
                throw new DeviceAcquiringException("Device not Found");
            }

            device.get(0).setStatus("enabled");
            session.update(device.get(0));

            return device.get(0);
        }
    }

    public static Device disableDevice(User u, UUID deviceId) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            List<Device> device = session.createQuery("from Device where userId = :uid and id = :id", Device.class)
                    .setParameter("uid", u.getId())
                    .setParameter("id", deviceId)
                    .setMaxResults(1)
                    .list();

            if (device.size() == 0) {
                throw new DeviceAcquiringException("Device not Found");
            }

            device.get(0).setStatus("disabled");
            session.update(device.get(0));

            return device.get(0);
        }
    }

    public static Device linkDeviceToArea(User u, UUID deviceId, UUID areaId) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {
            List<Device> device = session.createQuery("from Device where userId = :uid and id = :id", Device.class)
                    .setParameter("uid", u.getId())
                    .setParameter("id", deviceId)
                    .setMaxResults(1)
                    .list();

            if (device.size() == 0) {
                throw new DeviceAcquiringException("Device not Found");
            }

            device.get(0).setStatus("linked");
            device.get(0).setAreaId(areaId);
            session.update(device.get(0));

            return device.get(0);
        }
    }
}
