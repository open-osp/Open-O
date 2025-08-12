package org.oscarehr.PMmodule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;

@Service
public class ProgramProviderService {
    @Autowired
    private ProgramProviderDAO programProviderDao;

    @Transactional(readOnly = false)
    public void deleteProgramProvider(Long id) {
        programProviderDao.deleteProgramProvider(id);
    }
}
