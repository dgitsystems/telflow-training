package com.telflow.process.training.db;

import com.inomial.secore.sql.SessionFactory;
import org.activiti.engine.ProcessEngine;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * A helper class to allow listeners to interface with the TEL_MP_ORCHESTRATE table
 */
public class ExecutionControlManager {

    private static ExecutionControlManager instance;

    private ProcessEngine processEngine;

    private SqlSessionFactory sessionFactory;

    /**
     * Create a new instance of the Execution Control Manager
     * @param engine The process engine to hook into - to piggy back on the database configuration.
     */
    public ExecutionControlManager(ProcessEngine engine) {
        this.processEngine = engine;
        configure();
        instance = this;
    }

    private void configure() {
        // Initialise DB table if not exists
        this.sessionFactory = SessionFactory.getFactory(processEngine.getProcessEngineConfiguration().getDataSource());
        this.sessionFactory.getConfiguration().addMapper(ExecutionControlMapper.class);
        createTablesIfNeeded();
    }

    /**
     * Build schema on startup if needed.
     */
    private void createTablesIfNeeded() {
        try (SqlSession session = this.sessionFactory.openSession()) {
            ExecutionControlMapper mapper = session.getMapper(ExecutionControlMapper.class);
            mapper.createTable();
            session.commit();
        } catch (PersistenceException pex) {
            throw new RuntimeException("Unable To create Execution Control Table", pex);
        }
    }

    /**
     * Insert a new entry into the TEL_MP_ORCHESTRATE table
     * @param controlEntry The object to be inserted.
     */
    public void createEntry(ControlEntry controlEntry) {
        try (SqlSession session = this.sessionFactory.openSession()) {
            ExecutionControlMapper mapper = session.getMapper(ExecutionControlMapper.class);
            mapper.insert(controlEntry);
            session.commit();
        } catch (PersistenceException pex) {
            throw new RuntimeException("Unable To insert Execution Control Entry", pex);
        }
    }

    /**
     * Insert a list of new entries into the TEL_MP_ORCHESTRATE TABLE
     * @param entries The objects to be inserted.
     */
    public void createEntry(List<ControlEntry> entries) {
        try (SqlSession session = this.sessionFactory.openSession()) {
            ExecutionControlMapper mapper = session.getMapper(ExecutionControlMapper.class);
            for (ControlEntry controlEntry : entries) {
                mapper.insert(controlEntry);
            }
            session.commit();
        } catch (PersistenceException pex) {
            throw new RuntimeException("Unable To insert Execution Control Entries", pex);
        }
    }

    /**
     * Update the fields of an existing entry.  Only the Process Narrative, Execution If Running, Status and Outcome
     * fields will be updated
     * @param entry The entry to be updated.
     */
    public void updateEntry(ControlEntry entry) {
        try (SqlSession session = this.sessionFactory.openSession()) {
            ExecutionControlMapper mapper = session.getMapper(ExecutionControlMapper.class);
            mapper.update(entry);
            session.commit();
        } catch (PersistenceException pex) {
            throw new RuntimeException("Unable To update Execution Control Entries", pex);
        }
    }

    /**
     * Update the status of an existing entry.
     * @param businessInteractionId The businessInteractionId value of the entry, used for identification purposes
     * @param processId The processId value of the entry, used for identification purposes.
     * @param mappingKey The mappingKey value of the entry, used for identification purposes.
     * @param newStatus The new status of the entry.
     */
    public void updateEntryStatus(String businessInteractionId, String processId, String mappingKey,
                                  ControlEntry.Status newStatus) {
        try (SqlSession session = this.sessionFactory.openSession()) {
            ExecutionControlMapper mapper = session.getMapper(ExecutionControlMapper.class);
            mapper.updateStatus(businessInteractionId, processId, mappingKey, newStatus);
            session.commit();
        } catch (PersistenceException pex) {
            throw new RuntimeException("Unable To update Execution Control Entry status", pex);
        }
    }

    /**
     * Retrieve an entry by its unique key - business interaction ID, process ID, mapping key combination
     * @param businessInteractionId The businessInteractionId value of the entry, used for identification purposes
     * @param processId The processId value of the entry, used for identification purposes.
     * @param mappingKey The mappingKey value of the entry, used for identification purposes.
     * @return The entry from the database.
     */
    public ControlEntry getEntry(String businessInteractionId, String processId, String mappingKey) {
        try (SqlSession session = this.sessionFactory.openSession()) {
            ExecutionControlMapper mapper = session.getMapper(ExecutionControlMapper.class);
            ControlEntry entry = mapper.get(businessInteractionId, processId, mappingKey);
            session.commit();
            return entry;
        } catch (PersistenceException pex) {
            throw new RuntimeException("Unable To get Execution Control Entries", pex);
        }
    }

    /**
     * Retrieve all entries by their unique key - business interaction ID and process ID
     * @param businessInteractionId The businessInteractionId value of the entry, used for identification purposes
     * @param processId The processId value of the entry, used for identification purposes.
     * @return The entries from the database.
     */
    public List<ControlEntry> getAllEntries(String businessInteractionId, String processId) {
        try (SqlSession session = this.sessionFactory.openSession()) {
            ExecutionControlMapper mapper = session.getMapper(ExecutionControlMapper.class);
            List<ControlEntry> entryList = mapper.getAll(businessInteractionId, processId);
            session.commit();
            return entryList;
        } catch (PersistenceException pex) {
            throw new RuntimeException("Unable To get all Execution Control Entries", pex);
        }
    }

    /**
     * Retrieve the singleton instance of the ExecutionControlManager.  Used to minimise DB connections
     * @return The ExecutionControlManager instance.
     */
    public static ExecutionControlManager getInstance() {
        return instance;
    }

}
