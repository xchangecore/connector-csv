package com.leidos.xchangecore.adapter.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.leidos.xchangecore.adapter.model.MappedRecord;

public class MappedRecordDao extends GenericHibernateDAO<MappedRecord, Integer> {

    private static final String C_Creator = "creator";
    private static final String C_Url = "coreUri";

    public List<MappedRecord> findByCreator(String creator, String url) {

        return findByCriteria(Restrictions.eq(C_Creator, creator), Restrictions.eq(C_Url, url));
    }

    @Override
    public void makeTransient(MappedRecord record) {

        super.makeTransient(findById(record.getId()));
    }
}
