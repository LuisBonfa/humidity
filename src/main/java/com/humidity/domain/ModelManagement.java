package com.humidity.domain;

import com.humidity.domain.error.ListModelAcquiringException;
import com.humidity.entity.Model;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.persistence.EntityUtil;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.util.List;

public class ModelManagement {

    public static List<Model> loadModels(User u) throws StandardException {
        try (Session session = EntityUtil.openTransactionSession()) {

            List<Model> models = session.createQuery("from Model", Model.class)
                    .list();

            if(models.size() == 0)
            {
                throw new ListModelAcquiringException("Any model found", null);
            }

            return models;

        } catch (PersistenceException pe) {
            throw new ListModelAcquiringException("Something went wrong",pe);
        }
    }
}
